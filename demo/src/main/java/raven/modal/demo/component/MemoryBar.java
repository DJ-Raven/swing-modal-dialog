package raven.modal.demo.component;

import javax.swing.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

/**
 * @author Raven
 */
public class MemoryBar extends JProgressBar {

    private Timer timer;
    private int refreshRate = 1000;
    private String format = "%s of %s";
    private String value = "";

    public MemoryBar() {
        setStringPainted(true);
        updateMemoryUsage();
        installMemoryBar();
    }

    private void installMemoryBar() {
        uninstallMemoryBar();
        timer = new Timer(refreshRate, e -> updateMemoryUsage());
        timer.start();
    }

    private void uninstallMemoryBar() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    public void start() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    private void stop() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    private void updateMemoryUsage() {
        MemoryUsage memoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        setMaximum((int) memoryUsage.getCommitted());
        setValue((int) memoryUsage.getUsed());
        String max = formatSize(memoryUsage.getCommitted());
        String used = formatSize(memoryUsage.getUsed());
        value = String.format(format, used, max);
    }

    @Override
    public String getString() {
        return value;
    }

    protected String formatSize(long bytes) {
        int unit = 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(int refreshRate) {
        if (this.refreshRate != refreshRate) {
            this.refreshRate = refreshRate;
            timer.setDelay(refreshRate);
        }
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        if (this.format != format) {
            this.format = format;
            updateMemoryUsage();
        }
    }
}
