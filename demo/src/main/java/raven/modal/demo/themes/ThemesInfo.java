package raven.modal.demo.themes;

public class ThemesInfo {

    final String name;
    final String resourceName;
    final boolean dark;
    final String license;
    final String licenseFile;
    final String sourceCodeUrl;
    final String sourceCodePath;
    final String lafClassName;


    public ThemesInfo(String name, String resourceName, boolean dark, String license, String licenseFile, String sourceCodeUrl, String sourceCodePath, String lafClassName) {
        this.name = name;
        this.resourceName = resourceName;
        this.dark = dark;
        this.license = license;
        this.licenseFile = licenseFile;
        this.sourceCodeUrl = sourceCodeUrl;
        this.sourceCodePath = sourceCodePath;
        this.lafClassName = lafClassName;
    }
}