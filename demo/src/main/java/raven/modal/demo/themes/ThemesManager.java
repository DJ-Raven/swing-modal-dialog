package raven.modal.demo.themes;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.json.Json;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.LoggingFacade;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThemesManager {
    final List<ThemesInfo> bundledThemes = new ArrayList<>();
    final List<ThemesInfo> coreThemes = new ArrayList<>();

    void loadThemes() {
        bundledThemes.clear();
        // create core themes

        coreThemes.add(new ThemesInfo("FlatLaf Light", null, false, null, null, null, null, FlatLightLaf.class.getName()));
        coreThemes.add(new ThemesInfo("FlatLaf Dark", null, true, null, null, null, null, FlatDarkLaf.class.getName()));
        coreThemes.add(new ThemesInfo("FlatLaf IntelliJ", null, false, null, null, null, null, FlatIntelliJLaf.class.getName()));
        coreThemes.add(new ThemesInfo("FlatLaf Darcula", null, true, null, null, null, null, FlatDarculaLaf.class.getName()));
        coreThemes.add(new ThemesInfo("FlatLaf macOS Light", null, false, null, null, null, null, FlatMacLightLaf.class.getName()));
        coreThemes.add(new ThemesInfo("FlatLaf macOS Dark", null, true, null, null, null, null, FlatMacDarkLaf.class.getName()));

        // load themes.json
        Map<String, Object> json;
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("themes.json"), StandardCharsets.UTF_8)) {
            json = (Map<String, Object>) Json.parse(reader);
        } catch (IOException e) {
            LoggingFacade.INSTANCE.logSevere(null, e);
            return;
        }

        // add themes info
        for (Map.Entry<String, Object> e : json.entrySet()) {
            String resourceName = e.getKey();
            Map<String, String> value = (Map<String, String>) e.getValue();
            String name = value.get("name");
            boolean dark = Boolean.parseBoolean(value.get("dark"));
            String license = value.get("license");
            String licenseFile = value.get("licenseFile");
            String sourceCodeUrl = value.get("sourceCodeUrl");
            String sourceCodePath = value.get("sourceCodePath");
            bundledThemes.add(new ThemesInfo(name, resourceName, dark, license, licenseFile, sourceCodeUrl, sourceCodePath, null));
        }
    }
}