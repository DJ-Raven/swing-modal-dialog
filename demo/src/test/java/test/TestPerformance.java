package test;

import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.BorderOption;
import raven.modal.option.Option;
import test.base.BaseFrame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Benchmark frame for comparing snapshot animation ON/OFF with multiple workload
 * profiles and cache behaviors.
 *
 * @author Raven
 */
public class TestPerformance extends BaseFrame {

    private static final Color COLOR_GREEN = new Color(34, 139, 34);
    private static final Color COLOR_RED = new Color(180, 30, 30);
    private static final Color COLOR_NEUTRAL = new Color(100, 100, 100);
    private static final Color COLOR_PENDING = new Color(160, 160, 160);

    private final DecimalFormat fmt = new DecimalFormat("0.00");
    private final FrameTimeTracker frameTimeTracker = new FrameTimeTracker();

    private final JSpinner spinnerRows = new JSpinner(new SpinnerNumberModel(1500, 100, 10000, 100));
    private final JSpinner spinnerRuns = new JSpinner(new SpinnerNumberModel(5, 1, 30, 1));
    private final JSpinner spinnerSeed = new JSpinner(new SpinnerNumberModel(42, 1, Integer.MAX_VALUE, 1));

    private final JComboBox<WorkloadType> comboWorkload = new JComboBox<>(WorkloadType.values());
    private final JComboBox<CacheMode> comboCacheMode = new JComboBox<>(CacheMode.values());
    private final JCheckBox checkRandomRows = new JCheckBox("Randomize rows each run", true);

    private final JButton buttonCompare = new JButton("Run Full Comparison");
    private final JButton buttonPreviewBase = new JButton("Preview Baseline");
    private final JButton buttonPreviewOpt = new JButton("Preview Optimized");
    private final JButton buttonClearLog = new JButton("Clear Log");

    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel labelStatus = new JLabel("Ready. Run a full comparison to start.");

    // [0]=baseline [1]=optimized [2]=delta
    private final JLabel[] lblOpenAvg = makeTriple();
    private final JLabel[] lblOpenP95 = makeTriple();
    private final JLabel[] lblOpenMax = makeTriple();
    private final JLabel[] lblOpenMin = makeTriple();

    private final JLabel[] lblEdtBlockAvg = makeTriple();
    private final JLabel[] lblEdtBlockP95 = makeTriple();
    private final JLabel[] lblFirstPaintAvg = makeTriple();
    private final JLabel[] lblFirstPaintP95 = makeTriple();

    private final JLabel[] lblFrameAvg = makeTriple();
    private final JLabel[] lblFrameP95 = makeTriple();
    private final JLabel[] lblFrameMax = makeTriple();
    private final JLabel[] lblFrameCount = makeTriple();

    private final JTextArea textLog = new JTextArea();

    private boolean running;
    private ScenarioResult baselineResult;
    private ScenarioResult optimizedResult;

    public TestPerformance() {
        super("Modal Performance Benchmark");
        setSize(1320, 840);

        comboWorkload.setSelectedItem(WorkloadType.RANDOM_MIX);
        comboCacheMode.setSelectedItem(CacheMode.MIXED);

        RepaintManager.setCurrentManager(new BenchmarkRepaintManager(frameTimeTracker));

        JPanel content = new JPanel(new MigLayout("fill,insets 16,gap 14", "[grow,fill][360!,fill]", "[grow,fill]"));
        setContentPane(content);

        content.add(createResultPanel(), "grow");
        content.add(createControlPanel(), "growy");

        installActions();
    }

    private JComponent createResultPanel() {
        JPanel panel = new JPanel(new MigLayout("fill,wrap,insets 0,gap 10", "[grow,fill]", "[][shrink 0][grow,fill]"));

        JLabel title = new JLabel("Modal Performance Comparison");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        JLabel subtitle = new JLabel("Baseline uses snapshot animation ON. Optimized uses snapshot OFF."
                + " Workload and cache behavior are shared across both phases.");

        panel.add(title);
        panel.add(subtitle);
        panel.add(createComparisonTable(), "grow");
        return panel;
    }

    private JComponent createComparisonTable() {
        String cols = "[220!,fill][130!,fill][130!,fill][130!,fill][grow,fill]";
        JPanel p = new JPanel(new MigLayout("fillx,wrap 5,insets 12,gap 4", cols, ""));
        p.setBorder(new TitledBorder("Results"));

        addHeader(p, "Metric");
        addHeader(p, "Baseline");
        addHeader(p, "Optimized");
        addHeader(p, "Delta");
        p.add(new JLabel(), "");

        addSeparator(p, 5);

        addRow(p, "Open Time avg (ms)", lblOpenAvg);
        addRow(p, "Open Time P95 (ms)", lblOpenP95);
        addRow(p, "Open Time max (ms)", lblOpenMax);
        addRow(p, "Open Time min (ms)", lblOpenMin);

        addSeparator(p, 5);

        addRow(p, "EDT Block avg (ms)", lblEdtBlockAvg);
        addRow(p, "EDT Block P95 (ms)", lblEdtBlockP95);
        addRow(p, "First Paint avg (ms)", lblFirstPaintAvg);
        addRow(p, "First Paint P95 (ms)", lblFirstPaintP95);

        addSeparator(p, 5);

        addRow(p, "Frame Interval avg (ms)", lblFrameAvg);
        addRow(p, "Frame Interval P95 (ms)", lblFrameP95);
        addRow(p, "Frame Interval max (ms)", lblFrameMax);
        addRow(p, "Frames counted", lblFrameCount);

        textLog.setEditable(false);
        textLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        textLog.setLineWrap(true);
        textLog.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(textLog);
        scroll.setBorder(new TitledBorder("Run Log"));

        JPanel wrapper = new JPanel(new MigLayout("fill,wrap,insets 0", "[grow,fill]", "[shrink 0][grow,fill]"));
        wrapper.add(p, "growx");
        wrapper.add(scroll, "grow,pushy");
        return wrapper;
    }

    private void addHeader(JPanel p, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 12f));
        lbl.setForeground(COLOR_NEUTRAL);
        p.add(lbl);
    }

    private void addRow(JPanel p, String metric, JLabel[] triple) {
        p.add(new JLabel(metric));
        for (JLabel lbl : triple) {
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            p.add(lbl);
        }
        p.add(new JLabel(), "");
    }

    private void addSeparator(JPanel p, int span) {
        JSeparator sep = new JSeparator();
        p.add(sep, "span " + span + ",growx,gapy 2 2");
    }

    private JComponent createControlPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 14,gap 8", "[grow,fill]", "[][][][][][][][]push[][][][]"));
        panel.setBorder(new TitledBorder("Controls"));

        JPanel settings = new JPanel(new MigLayout("fillx,wrap 2,insets 0,gap 6", "[grow,fill][150!,fill]", "[][][][][]"));
        settings.add(new JLabel("Base rows"));
        settings.add(spinnerRows);
        settings.add(new JLabel("Runs per scenario"));
        settings.add(spinnerRuns);
        settings.add(new JLabel("Seed"));
        settings.add(spinnerSeed);
        settings.add(new JLabel("Workload profile"));
        settings.add(comboWorkload);
        settings.add(new JLabel("Cache behavior"));
        settings.add(comboCacheMode);

        progressBar.setStringPainted(true);
        progressBar.setString("");

        panel.add(settings, "growx");
        panel.add(checkRandomRows, "growx");
        panel.add(progressBar, "growx");
        panel.add(labelStatus, "growx");
        panel.add(buttonCompare, "growx,gapy 6 0");
        panel.add(buttonPreviewBase, "growx");
        panel.add(buttonPreviewOpt, "growx");
        panel.add(buttonClearLog, "growx");

        LightDarkButton ldb = new LightDarkButton();
        ldb.installAutoLafChangeListener();
        panel.add(ldb, "al right");

        return panel;
    }

    private void installActions() {
        buttonCompare.addActionListener(e -> runFullComparison());
        buttonPreviewBase.addActionListener(e -> openPreview(true, "Baseline preview"));
        buttonPreviewOpt.addActionListener(e -> openPreview(false, "Optimized preview"));
        buttonClearLog.addActionListener(e -> textLog.setText(""));
    }

    private void runFullComparison() {
        if (running) {
            return;
        }

        commitSpinnerEdits();

        int baseRows = (int) spinnerRows.getValue();
        int runs = (int) spinnerRuns.getValue();

        baselineResult = null;
        optimizedResult = null;
        resetTable();
        textLog.setText("");

        int totalSteps = runs * 2;

        appendLog("=== Full Comparison ===");
        appendLog("baseRows=" + baseRows + " runsPerScenario=" + runs + " seed=" + spinnerSeed.getValue());
        appendLog("workload=" + comboWorkload.getSelectedItem() + " cacheMode=" + comboCacheMode.getSelectedItem());
        appendLog("");
        appendLog("--- Phase 1: Baseline (snapshotAnimationEnabled=true) ---");

        setStatus("Phase 1 / 2 - baseline", totalSteps, 0);

        runScenario(baseRows, runs, true, totalSteps, 0, baseData -> {
            baselineResult = baseData;
            updateTable();
            appendScenarioSummary("Baseline", baseData);

            appendLog("");
            appendLog("--- Phase 2: Optimized (snapshotAnimationEnabled=false) ---");

            setStatus("Phase 2 / 2 - optimized", totalSteps, runs);

            runScenario(baseRows, runs, false, totalSteps, runs, optData -> {
                optimizedResult = optData;
                updateTable();
                appendScenarioSummary("Optimized", optData);
                appendLog("");
                appendComparisonDelta(baseData, optData);
                setStatus("Done", totalSteps, totalSteps);
                setRunningState(false);
            });
        });
    }

    private void runScenario(int baseRows, int runs, boolean snapshot,
                             int totalSteps, int completedBefore,
                             Consumer<ScenarioResult> onDone) {
        setRunningState(true);
        List<RunResult> results = new ArrayList<>();
        runScenarioAt(1, runs, baseRows, snapshot, totalSteps, completedBefore, results, onDone);
    }

    private void runScenarioAt(int index, int total, int baseRows, boolean snapshot,
                               int totalSteps, int completedBefore,
                               List<RunResult> results,
                               Consumer<ScenarioResult> onDone) {
        RunPlan plan = buildRunPlan(index, baseRows);

        runOneMeasuredModal(index, total, plan, snapshot, true, result -> {
            results.add(result);
            appendRunResult(result);
            setStatus((snapshot ? "Baseline" : "Optimized") + " run " + index + "/" + total,
                    totalSteps, completedBefore + index);

            if (index < total) {
                runScenarioAt(index + 1, total, baseRows, snapshot,
                        totalSteps, completedBefore, results, onDone);
            } else {
                onDone.accept(new ScenarioResult(results));
            }
        });
    }

    private void openPreview(boolean snapshot, String label) {
        if (running) {
            return;
        }

        commitSpinnerEdits();

        int baseRows = (int) spinnerRows.getValue();
        RunPlan plan = buildRunPlan(1, baseRows);

        appendLog("Opening preview: " + label + " | workload=" + plan.workload + " | cache=" + plan.cacheFlavor);

        runOneMeasuredModal(1, 1, plan, snapshot, false, result -> {
            appendRunResult(result);
            appendLog("Close the preview modal manually to continue.");
        });
    }

    private RunPlan buildRunPlan(int runIndex, int baseRows) {
        long seedBase = ((Number) spinnerSeed.getValue()).longValue();
        Random rng = new Random(seedBase + runIndex * 131L);

        CacheFlavor cacheFlavor = resolveCacheFlavor(runIndex);
        WorkloadType selectedWorkload = (WorkloadType) comboWorkload.getSelectedItem();
        WorkloadType actualWorkload = resolveWorkload(selectedWorkload, rng);

        int rows = baseRows;
        if (checkRandomRows.isSelected() || cacheFlavor == CacheFlavor.BUST || actualWorkload == WorkloadType.RANDOM_WIDGETS) {
            int minRows = Math.max(100, baseRows / 2);
            int maxRows = Math.max(minRows + 50, (int) (baseRows * 1.8));
            rows = randomRange(rng, minRows, maxRows);
        }

        Dimension preferredSize;
        if (cacheFlavor == CacheFlavor.BUST) {
            preferredSize = new Dimension(randomRange(rng, 760, 1120), randomRange(rng, 460, 740));
        } else {
            preferredSize = new Dimension(900, 560);
        }

        Color background = cacheFlavor == CacheFlavor.BUST
                ? new Color(randomRange(rng, 232, 255), randomRange(rng, 232, 255), randomRange(rng, 232, 255))
                : new Color(248, 250, 252);

        long planSeed = seedBase + runIndex * 9973L;
        return new RunPlan(runIndex, actualWorkload, cacheFlavor, rows, preferredSize, background, planSeed);
    }

    private WorkloadType resolveWorkload(WorkloadType selected, Random rng) {
        if (selected != WorkloadType.RANDOM_MIX) {
            return selected;
        }
        WorkloadType[] pool = {
                WorkloadType.TABLE_HEAVY,
                WorkloadType.TREE_TABLE_SPLIT,
                WorkloadType.FORM_DASHBOARD,
                WorkloadType.TABBED_MIXED,
                WorkloadType.RANDOM_WIDGETS
        };
        return pool[rng.nextInt(pool.length)];
    }

    private CacheFlavor resolveCacheFlavor(int runIndex) {
        CacheMode mode = (CacheMode) comboCacheMode.getSelectedItem();
        if (mode == CacheMode.CACHE_FRIENDLY) {
            return CacheFlavor.FRIENDLY;
        }
        if (mode == CacheMode.CACHE_BUST) {
            return CacheFlavor.BUST;
        }
        return (runIndex % 2 == 0) ? CacheFlavor.BUST : CacheFlavor.FRIENDLY;
    }

    private int randomRange(Random rng, int min, int max) {
        if (max <= min) {
            return min;
        }
        return min + rng.nextInt(max - min + 1);
    }

    private void runOneMeasuredModal(int runIndex, int totalRuns, RunPlan plan,
                                     boolean snapshot, boolean autoClose,
                                     Consumer<RunResult> onComplete) {
        Option option = createOption(snapshot, plan);
        String id = "bench_" + System.nanoTime();

        long startNs = System.nanoTime();
        frameTimeTracker.start(startNs);
        AtomicBoolean completed = new AtomicBoolean(false);

        final long[] edtDispatchNs = {-1L};
        EventQueue.invokeLater(() -> edtDispatchNs[0] = System.nanoTime());

        int measurementWindowMs = Math.max(700, option.getDuration() + 320);
        final Timer[] sampleTimerRef = new Timer[1];

        Timer sampleTimer = new Timer(measurementWindowMs, e -> {
            if (completed.get()) {
                return;
            }
            RunResult result = buildRunResult(
                    runIndex,
                    computeOpenMs(startNs),
                    startNs,
                    edtDispatchNs[0],
                    plan,
                    frameTimeTracker.stop()
            );

            if (autoClose) {
                requestCloseThenContinue(id, option.getDuration(), result, completed, onComplete, sampleTimerRef[0]);
            } else {
                completeRunOnce(completed, onComplete, result, sampleTimerRef[0]);
            }
        });
        sampleTimerRef[0] = sampleTimer;
        sampleTimer.setRepeats(false);
        sampleTimer.start();

        Timer watchdog = new Timer(Math.max(4000, option.getDuration() * 12), e -> {
            if (completed.get()) {
                return;
            }
            if (ModalDialog.isIdExist(id)) {
                ModalDialog.closeModal(id);
            }

            RunResult timeoutResult = buildRunResult(
                    runIndex,
                    (System.nanoTime() - startNs) / 1_000_000.0,
                    startNs,
                    edtDispatchNs[0],
                    plan,
                    frameTimeTracker.stop()
            );

            appendLog("  [warn] run " + runIndex + " timed out waiting for benchmark completion; continuing.");
            completeRunOnce(completed, onComplete, timeoutResult, sampleTimerRef[0]);
        });
        watchdog.setRepeats(false);
        watchdog.start();

        String title = autoClose
                ? (snapshot ? "[Baseline] " : "[Optimized] ") + "Run " + runIndex + "/" + totalRuns
                : (snapshot ? "Baseline preview" : "Optimized preview");

        JComponent content = createWorkloadContent(plan);

        SimpleModalBorder modal = new SimpleModalBorder(content, title,
                SimpleModalBorder.CLOSE_OPTION, (controller, action) -> {
            if (!autoClose && action == SimpleModalBorder.CLOSE_OPTION && !completed.get()) {
                RunResult result = buildRunResult(
                        runIndex,
                        computeOpenMs(startNs),
                        startNs,
                        edtDispatchNs[0],
                        plan,
                        frameTimeTracker.stop()
                );
                completeRunOnce(completed, onComplete, result, watchdog, sampleTimerRef[0]);
            }
        });

        ModalDialog.showModal(this, modal, option, id);
    }

    private void requestCloseThenContinue(String id, int duration, RunResult result,
                                          AtomicBoolean completed,
                                          Consumer<RunResult> onComplete,
                                          Timer... timers) {
        Timer closeTimer = new Timer(80, ev -> {
            if (ModalDialog.isIdExist(id)) {
                ModalDialog.closeModal(id);
            }
        });
        closeTimer.setRepeats(false);
        closeTimer.start();

        long waitStartNs = System.nanoTime();
        int timeoutMs = Math.max(1200, duration * 5);

        Timer closePoll = new Timer(40, null);
        closePoll.addActionListener(e -> {
            boolean exists = ModalDialog.isIdExist(id);
            long elapsedMs = (System.nanoTime() - waitStartNs) / 1_000_000L;
            if (!exists || elapsedMs >= timeoutMs) {
                closePoll.stop();
                completeRunOnce(completed, onComplete, result, timers);
            }
        });
        closePoll.start();
    }

    private void completeRunOnce(AtomicBoolean completed,
                                 Consumer<RunResult> onComplete,
                                 RunResult result,
                                 Timer... timers) {
        if (completed.compareAndSet(false, true)) {
            if (timers != null) {
                for (Timer timer : timers) {
                    if (timer != null && timer.isRunning()) {
                        timer.stop();
                    }
                }
            }
            onComplete.accept(result);
        }
    }

    private double computeOpenMs(long startNs) {
        double firstPaint = frameTimeTracker.getFirstPaintDelayMs();
        if (firstPaint >= 0) {
            return firstPaint;
        }
        return (System.nanoTime() - startNs) / 1_000_000.0;
    }

    private RunResult buildRunResult(int runIndex, double openMs, long startNs, long edtDispatchNs,
                                     RunPlan plan, List<Long> frames) {
        double edtBlockedMs = edtDispatchNs > 0
                ? (edtDispatchNs - startNs) / 1_000_000.0
                : -1;

        return new RunResult(
                runIndex,
                openMs,
                edtBlockedMs,
                frameTimeTracker.getFirstPaintDelayMs(),
                plan.workload,
                plan.cacheFlavor,
                plan.rows,
                plan.preferredSize,
                frames
        );
    }

    private void commitSpinnerEdits() {
        commitSpinner(spinnerRows);
        commitSpinner(spinnerRuns);
        commitSpinner(spinnerSeed);
    }

    private void commitSpinner(JSpinner spinner) {
        try {
            spinner.commitEdit();
        } catch (ParseException ignored) {
            // Keep the last valid value if user typed an incomplete number.
        }
    }

    private Option createOption(boolean snapshot, RunPlan plan) {
        Option opt = ModalDialog.createOption();
        opt.setSnapshotAnimationEnabled(snapshot)
                .setBackgroundClickType(Option.BackgroundClickType.BLOCK)
                .setOpacity(0.45f)
                .setDuration(220)
                .setAnimationEnabled(true)
                .setAnimationOnClose(true)
                .setHeavyWeight(true);

        opt.getLayoutOption()
                .setRelativeToOwner(true)
                .setMovable(false);

        int borderWidth = plan.cacheFlavor == CacheFlavor.BUST ? 1 + (plan.runIndex % 2) : 1;

        opt.getBorderOption()
                .setBorderWidth(borderWidth)
                .setShadow(BorderOption.Shadow.MEDIUM);

        return opt;
    }

    private JComponent createWorkloadContent(RunPlan plan) {
        Random rng = new Random(plan.seed);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setPreferredSize(plan.preferredSize);
        panel.setOpaque(true);
        panel.setBackground(plan.background);

        if (plan.cacheFlavor == CacheFlavor.BUST) {
            panel.setBorder(BorderFactory.createEmptyBorder(
                    randomRange(rng, 4, 14),
                    randomRange(rng, 4, 14),
                    randomRange(rng, 4, 14),
                    randomRange(rng, 4, 14)));
        } else {
            panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        }

        JLabel info = new JLabel("Workload=" + plan.workload
                + " | CacheMode=" + plan.cacheFlavor
                + " | rows=" + plan.rows
                + " | size=" + plan.preferredSize.width + "x" + plan.preferredSize.height);

        panel.add(info, BorderLayout.NORTH);
        panel.add(createWorkloadBody(plan, rng), BorderLayout.CENTER);
        panel.add(createFooterBar(plan, rng), BorderLayout.SOUTH);

        return panel;
    }

    private JComponent createWorkloadBody(RunPlan plan, Random rng) {
        switch (plan.workload) {
            case TABLE_HEAVY:
                return createTablePanel(plan.rows, rng);
            case TREE_TABLE_SPLIT:
                return createTreeTableSplitPanel(plan.rows, rng);
            case FORM_DASHBOARD:
                return createFormDashboardPanel(plan.rows, rng);
            case TABBED_MIXED:
                return createTabbedMixedPanel(plan.rows, rng);
            case RANDOM_WIDGETS:
                return createRandomWidgetsPanel(plan.rows, rng);
            default:
                return createTablePanel(plan.rows, rng);
        }
    }

    private JComponent createTablePanel(int rowCount, Random rng) {
        JTable table = buildHeavyTable(rowCount, rng);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        return scroll;
    }

    private JTable buildHeavyTable(int rowCount, Random rng) {
        String[] cols = {"ID", "User", "Region", "Latency", "CPU", "Status", "Errors", "Progress"};

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                switch (c) {
                    case 0:
                    case 3:
                    case 6:
                    case 7:
                        return Integer.class;
                    case 4:
                        return Double.class;
                    default:
                        return String.class;
                }
            }
        };

        String[] regions = {"US-East", "US-West", "EU-West", "AP-South", "SA-East", "ME-Central"};
        String[] statuses = {"OK", "WARN", "DEGRADED", "RETRY"};

        for (int i = 1; i <= rowCount; i++) {
            model.addRow(new Object[]{
                    i,
                    "user-" + i,
                    regions[rng.nextInt(regions.length)],
                    20 + rng.nextInt(500),
                    Math.round((5 + rng.nextDouble() * 90) * 100.0) / 100.0,
                    statuses[rng.nextInt(statuses.length)],
                    rng.nextInt(16),
                    rng.nextInt(101)
            });
        }

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        StressCellRenderer renderer = new StressCellRenderer();
        table.setDefaultRenderer(Object.class, renderer);
        table.setDefaultRenderer(Integer.class, renderer);
        table.setDefaultRenderer(Double.class, renderer);
        table.getColumnModel().getColumn(7).setCellRenderer(new ProgressCellRenderer());

        return table;
    }

    private JComponent createTreeTableSplitPanel(int rowCount, Random rng) {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.35);

        split.setLeftComponent(createTreePane(Math.max(120, rowCount / 6), rng));
        split.setRightComponent(createTablePanel(Math.max(300, rowCount), rng));

        return split;
    }

    private JComponent createTreePane(int nodeCount, Random rng) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("System");

        int groups = Math.max(20, nodeCount / 8);
        for (int g = 1; g <= groups; g++) {
            DefaultMutableTreeNode group = new DefaultMutableTreeNode("Group-" + g);
            int children = 4 + rng.nextInt(8);
            for (int c = 1; c <= children; c++) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode("Node-" + g + "." + c);
                int leaves = 2 + rng.nextInt(6);
                for (int i = 1; i <= leaves; i++) {
                    child.add(new DefaultMutableTreeNode("Leaf-" + g + "." + c + "." + i));
                }
                group.add(child);
            }
            root.add(group);
        }

        JTree tree = new JTree(root);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setRowHeight(24);

        return new JScrollPane(tree);
    }

    private JComponent createFormDashboardPanel(int rowCount, Random rng) {
        JPanel main = new JPanel(new BorderLayout(10, 10));

        JPanel form = new JPanel(new MigLayout("wrap 4,insets 6,gap 6", "[grow,fill][grow,fill][grow,fill][grow,fill]", ""));
        int fields = Math.max(24, rowCount / 60);
        for (int i = 1; i <= fields; i++) {
            form.add(new JLabel("Field " + i));
            form.add(new JTextField("value-" + i));
            JComboBox<String> combo = new JComboBox<>(new String[]{"A", "B", "C", "D"});
            combo.setSelectedIndex(rng.nextInt(4));
            form.add(combo);
            form.add(new JSpinner(new SpinnerNumberModel(rng.nextInt(1000), 0, 10000, 1)));
        }

        JScrollPane formScroll = new JScrollPane(form);

        JList<String> list = new JList<>(createListModel(Math.max(300, rowCount / 3), rng));
        JScrollPane listScroll = new JScrollPane(list);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formScroll, listScroll);
        split.setResizeWeight(0.65);

        main.add(split, BorderLayout.CENTER);
        main.add(createChartPanel(rng), BorderLayout.EAST);

        return main;
    }

    private JComponent createTabbedMixedPanel(int rowCount, Random rng) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Table", createTablePanel(Math.max(300, rowCount), rng));
        tabs.addTab("Tree", createTreePane(Math.max(120, rowCount / 8), rng));
        tabs.addTab("Form", createFormDashboardPanel(Math.max(200, rowCount / 2), rng));
        tabs.addTab("Widgets", createRandomWidgetsPanel(Math.max(200, rowCount / 3), rng));

        tabs.setSelectedIndex(rng.nextInt(tabs.getTabCount()));
        return tabs;
    }

    private JComponent createRandomWidgetsPanel(int rowCount, Random rng) {
        JPanel root = new JPanel(new BorderLayout(8, 8));

        JPanel grid = new JPanel(new GridLayout(0, 3, 8, 8));
        int components = Math.max(18, rowCount / 70);
        for (int i = 0; i < components; i++) {
            grid.add(createRandomWidget(rng, i));
        }

        JScrollPane scroll = new JScrollPane(grid);
        root.add(scroll, BorderLayout.CENTER);

        JTextArea logs = createLogArea(Math.max(500, rowCount / 2), rng);
        root.add(new JScrollPane(logs), BorderLayout.SOUTH);

        return root;
    }

    private JComponent createRandomWidget(Random rng, int index) {
        int type = rng.nextInt(8);
        switch (type) {
            case 0:
                return new JButton("Action " + index);
            case 1:
                return new JTextField("Input " + index);
            case 2:
                return new JCheckBox("Check " + index, rng.nextBoolean());
            case 3:
                JComboBox<String> combo = new JComboBox<>(new String[]{"North", "South", "West", "East"});
                combo.setSelectedIndex(rng.nextInt(combo.getItemCount()));
                return combo;
            case 4:
                JProgressBar progress = new JProgressBar(0, 100);
                progress.setValue(rng.nextInt(101));
                progress.setStringPainted(true);
                return progress;
            case 5:
                JSlider slider = new JSlider(0, 100, rng.nextInt(101));
                slider.setPaintTicks(true);
                slider.setMajorTickSpacing(25);
                return slider;
            case 6:
                return new JToggleButton("Toggle " + index, rng.nextBoolean());
            default:
                JTextArea area = new JTextArea(3, 8);
                area.setText(createRandomText(rng, 40));
                return new JScrollPane(area);
        }
    }

    private JTextArea createLogArea(int lines, Random rng) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        int count = Math.max(120, lines);
        for (int i = 1; i <= count; i++) {
            sb.append("log-").append(i)
                    .append(" :: code=").append(1000 + rng.nextInt(9000))
                    .append(" :: message=").append(createRandomText(rng, 24))
                    .append('\n');
        }
        textArea.setText(sb.toString());

        return textArea;
    }

    private String[] createListModel(int size, Random rng) {
        String[] data = new String[size];
        for (int i = 0; i < size; i++) {
            data[i] = "item-" + i + "  " + createRandomText(rng, 16);
        }
        return data;
    }

    private JComponent createChartPanel(Random rng) {
        final int[] values = new int[24];
        for (int i = 0; i < values.length; i++) {
            values[i] = 10 + rng.nextInt(90);
        }

        JComponent chart = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(246, 248, 251));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                    int padding = 10;
                    int w = Math.max(1, getWidth() - (padding * 2));
                    int h = Math.max(1, getHeight() - (padding * 2));
                    int barW = Math.max(2, w / (values.length * 2));

                    for (int i = 0; i < values.length; i++) {
                        int x = padding + (i * 2 * barW);
                        int barH = (int) ((values[i] / 100f) * h);
                        int y = padding + h - barH;
                        g2.setColor(i % 2 == 0 ? new Color(35, 121, 231) : new Color(13, 169, 124));
                        g2.fillRoundRect(x, y, barW, barH, 8, 8);
                    }
                } finally {
                    g2.dispose();
                }
            }
        };

        chart.setPreferredSize(new Dimension(220, 220));
        return chart;
    }

    private String createRandomText(Random rng, int maxLen) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        int length = Math.max(8, rng.nextInt(Math.max(10, maxLen)));
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rng.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private JComponent createFooterBar(RunPlan plan, Random rng) {
        JPanel panel = new JPanel(new MigLayout("insets 0,gap 8", "[grow,fill][100!,fill][100!,fill]", "[]"));

        JProgressBar p1 = new JProgressBar(0, 100);
        p1.setValue(rng.nextInt(101));
        p1.setStringPainted(true);

        JProgressBar p2 = new JProgressBar(0, 100);
        p2.setValue(rng.nextInt(101));
        p2.setStringPainted(true);

        JButton button = new JButton(plan.cacheFlavor == CacheFlavor.BUST ? "Dynamic mode" : "Stable mode");

        panel.add(new JLabel("Footer widgets to increase mixed component pressure"), "growx,pushx");
        panel.add(p1, "growx");
        panel.add(p2, "growx");
        panel.add(button, "span 3,al right");

        return panel;
    }

    private void resetTable() {
        JLabel[][] all = {
                lblOpenAvg, lblOpenP95, lblOpenMax, lblOpenMin,
                lblEdtBlockAvg, lblEdtBlockP95, lblFirstPaintAvg, lblFirstPaintP95,
                lblFrameAvg, lblFrameP95, lblFrameMax, lblFrameCount
        };
        for (JLabel[] triple : all) {
            for (JLabel l : triple) {
                l.setText("-");
                l.setForeground(COLOR_PENDING);
            }
        }
    }

    private void updateTable() {
        if (baselineResult != null) {
            fill(lblOpenAvg[0], baselineResult.openAvg);
            fill(lblOpenP95[0], baselineResult.openP95);
            fill(lblOpenMax[0], baselineResult.openMax);
            fill(lblOpenMin[0], baselineResult.openMin);

            fill(lblEdtBlockAvg[0], baselineResult.edtBlockAvg);
            fill(lblEdtBlockP95[0], baselineResult.edtBlockP95);
            fill(lblFirstPaintAvg[0], baselineResult.firstPaintAvg);
            fill(lblFirstPaintP95[0], baselineResult.firstPaintP95);

            fill(lblFrameAvg[0], baselineResult.frameAvg);
            fill(lblFrameP95[0], baselineResult.frameP95);
            fill(lblFrameMax[0], baselineResult.frameMax);
            fillCount(lblFrameCount[0], baselineResult.frameCount);
        }

        if (optimizedResult != null) {
            fill(lblOpenAvg[1], optimizedResult.openAvg);
            fill(lblOpenP95[1], optimizedResult.openP95);
            fill(lblOpenMax[1], optimizedResult.openMax);
            fill(lblOpenMin[1], optimizedResult.openMin);

            fill(lblEdtBlockAvg[1], optimizedResult.edtBlockAvg);
            fill(lblEdtBlockP95[1], optimizedResult.edtBlockP95);
            fill(lblFirstPaintAvg[1], optimizedResult.firstPaintAvg);
            fill(lblFirstPaintP95[1], optimizedResult.firstPaintP95);

            fill(lblFrameAvg[1], optimizedResult.frameAvg);
            fill(lblFrameP95[1], optimizedResult.frameP95);
            fill(lblFrameMax[1], optimizedResult.frameMax);
            fillCount(lblFrameCount[1], optimizedResult.frameCount);
        }

        if (baselineResult != null && optimizedResult != null) {
            fillDelta(lblOpenAvg[2], baselineResult.openAvg, optimizedResult.openAvg);
            fillDelta(lblOpenP95[2], baselineResult.openP95, optimizedResult.openP95);
            fillDelta(lblOpenMax[2], baselineResult.openMax, optimizedResult.openMax);
            fillDelta(lblOpenMin[2], baselineResult.openMin, optimizedResult.openMin);

            fillDelta(lblEdtBlockAvg[2], baselineResult.edtBlockAvg, optimizedResult.edtBlockAvg);
            fillDelta(lblEdtBlockP95[2], baselineResult.edtBlockP95, optimizedResult.edtBlockP95);
            fillDelta(lblFirstPaintAvg[2], baselineResult.firstPaintAvg, optimizedResult.firstPaintAvg);
            fillDelta(lblFirstPaintP95[2], baselineResult.firstPaintP95, optimizedResult.firstPaintP95);

            fillDelta(lblFrameAvg[2], baselineResult.frameAvg, optimizedResult.frameAvg);
            fillDelta(lblFrameP95[2], baselineResult.frameP95, optimizedResult.frameP95);
            fillDelta(lblFrameMax[2], baselineResult.frameMax, optimizedResult.frameMax);
            fillDeltaCount(lblFrameCount[2], baselineResult.frameCount, optimizedResult.frameCount);
        }
    }

    private void fill(JLabel lbl, double value) {
        lbl.setText(formatMetric(value) + " ms");
        lbl.setForeground(UIManager.getColor("Label.foreground"));
    }

    private void fillCount(JLabel lbl, int count) {
        lbl.setText(count + " frames");
        lbl.setForeground(UIManager.getColor("Label.foreground"));
    }

    private void fillDeltaCount(JLabel lbl, int baseline, int optimized) {
        if (baseline <= 0) {
            lbl.setText("n/a");
            lbl.setForeground(COLOR_NEUTRAL);
            return;
        }

        int diff = optimized - baseline;
        if (diff > 0) {
            lbl.setText("+" + diff + " frames");
            lbl.setForeground(COLOR_GREEN);
        } else if (diff < 0) {
            lbl.setText(diff + " frames");
            lbl.setForeground(COLOR_NEUTRAL);
        } else {
            lbl.setText("same");
            lbl.setForeground(COLOR_NEUTRAL);
        }
    }

    private void fillDelta(JLabel lbl, double baseline, double optimized) {
        if (baseline <= 0 || optimized < 0) {
            lbl.setText("n/a");
            lbl.setForeground(COLOR_NEUTRAL);
            return;
        }

        double pct = ((baseline - optimized) / baseline) * 100.0;
        if (pct > 0.05) {
            lbl.setText("-" + fmt.format(pct) + "% faster");
            lbl.setForeground(COLOR_GREEN);
        } else if (pct < -0.05) {
            lbl.setText("+" + fmt.format(-pct) + "% slower");
            lbl.setForeground(COLOR_RED);
        } else {
            lbl.setText("same");
            lbl.setForeground(COLOR_NEUTRAL);
        }
    }

    private void appendRunResult(RunResult r) {
        FrameMetrics fm = FrameMetrics.from(r.frameIntervalsNs);
        appendLog(String.format(
                "  run %2d | workload=%-16s cache=%-8s rows=%4d size=%4dx%-4d | open=%7s ms | edt=%7s ms | firstPaint=%7s ms | frames=%3d | frameAvg=%6s ms | frameP95=%6s ms | frameMax=%6s ms",
                r.runIndex,
                r.workload,
                r.cacheFlavor,
                r.rows,
                r.size.width,
                r.size.height,
                fmt.format(r.openMs),
                formatMetric(r.edtBlockedMs),
                formatMetric(r.firstPaintMs),
                fm.frameCount,
                fmt.format(fm.avgMs),
                fmt.format(fm.p95Ms),
                fmt.format(fm.maxMs)
        ));
    }

    private void appendScenarioSummary(String label, ScenarioResult sr) {
        appendLog(String.format(
                "  >> %s summary: open avg=%s p95=%s max=%s min=%s | edt avg=%s p95=%s | firstPaint avg=%s p95=%s | frame avg=%s p95=%s max=%s | frames=%d",
                label,
                fmt.format(sr.openAvg), fmt.format(sr.openP95),
                fmt.format(sr.openMax), fmt.format(sr.openMin),
                formatMetric(sr.edtBlockAvg), formatMetric(sr.edtBlockP95),
                formatMetric(sr.firstPaintAvg), formatMetric(sr.firstPaintP95),
                fmt.format(sr.frameAvg), fmt.format(sr.frameP95), fmt.format(sr.frameMax),
                sr.frameCount
        ));
    }

    private void appendComparisonDelta(ScenarioResult base, ScenarioResult opt) {
        appendLog("=== COMPARISON DELTA (baseline vs optimized) ===");

        appendDeltaLine("Open avg", base.openAvg, opt.openAvg);
        appendDeltaLine("Open P95", base.openP95, opt.openP95);
        appendDeltaLine("Open max", base.openMax, opt.openMax);

        appendLog(String.format("  %-15s: %s ms -> %s ms",
                "Open min", fmt.format(base.openMin), fmt.format(opt.openMin)));

        appendDeltaLine("EDT block avg", base.edtBlockAvg, opt.edtBlockAvg);
        appendDeltaLine("EDT block P95", base.edtBlockP95, opt.edtBlockP95);
        appendDeltaLine("FirstPaint avg", base.firstPaintAvg, opt.firstPaintAvg);
        appendDeltaLine("FirstPaint P95", base.firstPaintP95, opt.firstPaintP95);

        appendDeltaLine("Frame avg", base.frameAvg, opt.frameAvg);
        appendDeltaLine("Frame P95", base.frameP95, opt.frameP95);
        appendDeltaLine("Frame max", base.frameMax, opt.frameMax);

        appendLog(String.format("  %-15s: %d -> %d frames", "Frame count", base.frameCount, opt.frameCount));
    }

    private void appendDeltaLine(String metric, double base, double opt) {
        appendLog(String.format("  %-15s: %s ms -> %s ms   (%s)",
                metric,
                formatMetric(base),
                formatMetric(opt),
                deltaStr(base, opt)));
    }

    private String deltaStr(double base, double opt) {
        if (base <= 0 || opt < 0) {
            return "n/a";
        }
        double pct = ((base - opt) / base) * 100.0;
        if (pct > 0.05) {
            return fmt.format(pct) + "% faster";
        }
        if (pct < -0.05) {
            return fmt.format(-pct) + "% slower";
        }
        return "same";
    }

    private String formatMetric(double value) {
        if (value < 0) {
            return "n/a";
        }
        return fmt.format(value);
    }

    private void appendLog(String msg) {
        textLog.append(msg + "\n");
        textLog.setCaretPosition(textLog.getDocument().getLength());
    }

    private void setStatus(String msg, int total, int done) {
        labelStatus.setText(msg);
        progressBar.setMinimum(0);
        progressBar.setMaximum(Math.max(total, 1));
        progressBar.setValue(Math.min(done, total));
        progressBar.setString(done + " / " + total);
    }

    private void setRunningState(boolean state) {
        running = state;
        buttonCompare.setEnabled(!state);
        buttonPreviewBase.setEnabled(!state);
        buttonPreviewOpt.setEnabled(!state);
        spinnerRows.setEnabled(!state);
        spinnerRuns.setEnabled(!state);
        spinnerSeed.setEnabled(!state);
        comboWorkload.setEnabled(!state);
        comboCacheMode.setEnabled(!state);
        checkRandomRows.setEnabled(!state);
    }

    private static JLabel[] makeTriple() {
        JLabel a = new JLabel("-");
        JLabel b = new JLabel("-");
        JLabel c = new JLabel("-");

        a.setForeground(COLOR_PENDING);
        b.setForeground(COLOR_PENDING);
        c.setForeground(COLOR_PENDING);

        return new JLabel[]{a, b, c};
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestPerformance().setVisible(true));
    }

    private static class RunPlan {
        final int runIndex;
        final WorkloadType workload;
        final CacheFlavor cacheFlavor;
        final int rows;
        final Dimension preferredSize;
        final Color background;
        final long seed;

        RunPlan(int runIndex, WorkloadType workload, CacheFlavor cacheFlavor, int rows,
                Dimension preferredSize, Color background, long seed) {
            this.runIndex = runIndex;
            this.workload = workload;
            this.cacheFlavor = cacheFlavor;
            this.rows = rows;
            this.preferredSize = preferredSize;
            this.background = background;
            this.seed = seed;
        }
    }

    private static class RunResult {
        final int runIndex;
        final double openMs;
        final double edtBlockedMs;
        final double firstPaintMs;
        final WorkloadType workload;
        final CacheFlavor cacheFlavor;
        final int rows;
        final Dimension size;
        final List<Long> frameIntervalsNs;

        RunResult(int runIndex, double openMs, double edtBlockedMs, double firstPaintMs,
                  WorkloadType workload, CacheFlavor cacheFlavor, int rows, Dimension size,
                  List<Long> frameIntervalsNs) {
            this.runIndex = runIndex;
            this.openMs = openMs;
            this.edtBlockedMs = edtBlockedMs;
            this.firstPaintMs = firstPaintMs;
            this.workload = workload;
            this.cacheFlavor = cacheFlavor;
            this.rows = rows;
            this.size = size;
            this.frameIntervalsNs = frameIntervalsNs;
        }
    }

    private static class ScenarioResult {
        final double openAvg;
        final double openP95;
        final double openMax;
        final double openMin;

        final double edtBlockAvg;
        final double edtBlockP95;
        final double firstPaintAvg;
        final double firstPaintP95;

        final double frameAvg;
        final double frameP95;
        final double frameMax;
        final int frameCount;

        ScenarioResult(List<RunResult> runs) {
            List<Double> opens = new ArrayList<>();
            List<Double> edtBlocks = new ArrayList<>();
            List<Double> firstPaints = new ArrayList<>();
            List<Long> allFrames = new ArrayList<>();

            for (RunResult r : runs) {
                opens.add(r.openMs);
                if (r.edtBlockedMs >= 0) {
                    edtBlocks.add(r.edtBlockedMs);
                }
                if (r.firstPaintMs >= 0) {
                    firstPaints.add(r.firstPaintMs);
                }
                allFrames.addAll(r.frameIntervalsNs);
            }

            openAvg = avg(opens);
            openP95 = percentile(opens, 95);
            openMax = max(opens);
            openMin = min(opens);

            edtBlockAvg = avg(edtBlocks);
            edtBlockP95 = percentile(edtBlocks, 95);
            firstPaintAvg = avg(firstPaints);
            firstPaintP95 = percentile(firstPaints, 95);

            FrameMetrics fm = FrameMetrics.from(allFrames);
            frameAvg = fm.avgMs;
            frameP95 = fm.p95Ms;
            frameMax = fm.maxMs;
            frameCount = fm.frameCount;
        }
    }

    private static class FrameMetrics {
        final int frameCount;
        final double avgMs;
        final double p95Ms;
        final double maxMs;

        FrameMetrics(int frameCount, double avgMs, double p95Ms, double maxMs) {
            this.frameCount = frameCount;
            this.avgMs = avgMs;
            this.p95Ms = p95Ms;
            this.maxMs = maxMs;
        }

        static FrameMetrics from(List<Long> ns) {
            if (ns == null || ns.isEmpty()) {
                return new FrameMetrics(0, 0, 0, 0);
            }

            List<Double> ms = new ArrayList<>(ns.size());
            double total = 0;
            double max = 0;
            for (Long n : ns) {
                double value = n / 1_000_000.0;
                ms.add(value);
                total += value;
                max = Math.max(max, value);
            }

            Collections.sort(ms);
            int idx = Math.max(0, Math.min((int) Math.ceil(ms.size() * 0.95) - 1, ms.size() - 1));

            return new FrameMetrics(ms.size(), total / ms.size(), ms.get(idx), max);
        }
    }

    private static double min(List<Double> values) {
        if (values.isEmpty()) {
            return -1;
        }
        double min = Double.MAX_VALUE;
        for (double v : values) {
            min = Math.min(min, v);
        }
        return min;
    }

    private static double avg(List<Double> values) {
        if (values.isEmpty()) {
            return -1;
        }
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.size();
    }

    private static double max(List<Double> values) {
        if (values.isEmpty()) {
            return -1;
        }
        double max = Double.MIN_VALUE;
        for (double v : values) {
            max = Math.max(max, v);
        }
        return max;
    }

    private static double percentile(List<Double> values, int pct) {
        if (values.isEmpty()) {
            return -1;
        }
        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        int idx = (int) Math.ceil((pct / 100.0) * sorted.size()) - 1;
        idx = Math.max(0, Math.min(idx, sorted.size() - 1));
        return sorted.get(idx);
    }

    private static class FrameTimeTracker {
        private boolean tracking;
        private long runStartNs = -1;
        private long firstPaintNs = -1;
        private long lastPaintNs = -1;
        private final List<Long> intervals = new ArrayList<>();

        void start(long runStartNs) {
            this.tracking = true;
            this.runStartNs = runStartNs;
            this.firstPaintNs = -1;
            this.lastPaintNs = -1;
            this.intervals.clear();
        }

        List<Long> stop() {
            this.tracking = false;
            return new ArrayList<>(intervals);
        }

        double getFirstPaintDelayMs() {
            if (firstPaintNs <= 0 || runStartNs <= 0) {
                return -1;
            }
            return (firstPaintNs - runStartNs) / 1_000_000.0;
        }

        void onPaintCycle() {
            if (!tracking) {
                return;
            }
            long now = System.nanoTime();
            if (firstPaintNs < 0) {
                firstPaintNs = now;
            }
            if (lastPaintNs > 0) {
                intervals.add(now - lastPaintNs);
            }
            lastPaintNs = now;
        }
    }

    private static class BenchmarkRepaintManager extends RepaintManager {
        private final FrameTimeTracker tracker;

        BenchmarkRepaintManager(FrameTimeTracker tracker) {
            this.tracker = tracker;
        }

        @Override
        public void paintDirtyRegions() {
            tracker.onPaintCycle();
            super.paintDirtyRegions();
        }
    }

    private static class StressCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                                                       boolean selected, boolean focused, int row, int col) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, selected, focused, row, col);
            l.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            if (!selected) {
                l.setBackground(row % 2 == 0 ? new Color(248, 250, 252) : new Color(239, 244, 249));
                l.setForeground(new Color(31, 41, 55));
            }
            return l;
        }
    }

    private static class ProgressCellRenderer extends JProgressBar implements TableCellRenderer {
        ProgressCellRenderer() {
            setMinimum(0);
            setMaximum(100);
            setStringPainted(true);
            setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                                                       boolean selected, boolean focused, int row, int col) {
            int p = v instanceof Number ? ((Number) v).intValue() : 0;
            setValue(p);
            setString(p + "%");
            setBackground(selected ? t.getSelectionBackground()
                    : row % 2 == 0 ? new Color(248, 250, 252) : new Color(239, 244, 249));
            setForeground(selected ? t.getSelectionForeground() : new Color(24, 118, 242));
            return this;
        }
    }

    private enum WorkloadType {
        TABLE_HEAVY("Heavy JTable"),
        TREE_TABLE_SPLIT("Tree + Table Split"),
        FORM_DASHBOARD("Form Dashboard"),
        TABBED_MIXED("Tabbed Mixed"),
        RANDOM_WIDGETS("Random Widgets"),
        RANDOM_MIX("Random Mix (new each run)");

        private final String text;

        WorkloadType(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private enum CacheMode {
        CACHE_FRIENDLY("Use cache (stable size/colors)"),
        CACHE_BUST("Avoid cache (vary size/colors)"),
        MIXED("Mixed (alternate stable/bust)");

        private final String text;

        CacheMode(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private enum CacheFlavor {
        FRIENDLY,
        BUST
    }
}
