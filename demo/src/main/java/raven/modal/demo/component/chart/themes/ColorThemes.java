package raven.modal.demo.component.chart.themes;

import java.awt.*;

public enum ColorThemes {
    DEFAULT(
            Color.decode("#fd7f6f"),
            Color.decode("#7eb0d5"),
            Color.decode("#b2e061"),
            Color.decode("#bd7ebe"),
            Color.decode("#ffb55a"),
            Color.decode("#ffee65"),
            Color.decode("#beb9db"),
            Color.decode("#fdcce5"),
            Color.decode("#8bd3c7")
    ),
    RETRO_METRO(
            Color.decode("#ea5545"),
            Color.decode("#f46a9b"),
            Color.decode("#ef9b20"),
            Color.decode("#edbf33"),
            Color.decode("#ede15b"),
            Color.decode("#bdcf32"),
            Color.decode("#87bc45"),
            Color.decode("#27aeef"),
            Color.decode("#b33dc6")
    ),
    BLUE_TO_YELLOW(
            Color.decode("#115f9a"),
            Color.decode("#1984c5"),
            Color.decode("#22a7f0"),
            Color.decode("#48b5c4"),
            Color.decode("#76c68f"),
            Color.decode("#a6d75b"),
            Color.decode("#c9e52f"),
            Color.decode("#d0ee11"),
            Color.decode("#d0f400")
    ),
    SALMON_TO_AQUA(
            Color.decode("#e27c7c"),
            Color.decode("#a86464"),
            Color.decode("#6d4b4b"),
            Color.decode("#503f3f"),
            Color.decode("#333333"),
            Color.decode("#3c4e4b"),
            Color.decode("#466964"),
            Color.decode("#599e94"),
            Color.decode("#6cd4c5")
    ),
    LIGHT(
            Color.decode("#ffffff"),
            Color.decode("#f0f0f0"),
            Color.decode("#d9d9d9"),
            Color.decode("#b3b3b3"),
            Color.decode("#999999"),
            Color.decode("#808080"),
            Color.decode("#666666"),
            Color.decode("#333333"),
            Color.decode("#000000")
    ),
    DARK(
            Color.decode("#444444"),
            Color.decode("#555555"),
            Color.decode("#666666"),
            Color.decode("#777777"),
            Color.decode("#888888"),
            Color.decode("#999999"),
            Color.decode("#aaaaaa"),
            Color.decode("#bbbbbb"),
            Color.decode("#cccccc")
    );

    private Color[] colors;

    ColorThemes(Color... colors) {
        this.colors = colors;
    }

    public Color[] getColors() {
        return colors;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
