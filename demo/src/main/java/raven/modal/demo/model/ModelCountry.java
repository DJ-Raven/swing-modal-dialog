package raven.modal.demo.model;

public class ModelCountry {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso2Code() {
        return iso2Code;
    }

    public void setIso2Code(String iso2Code) {
        this.iso2Code = iso2Code;
    }

    public String getIos3Code() {
        return ios3Code;
    }

    public void setIos3Code(String ios3Code) {
        this.ios3Code = ios3Code;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public ModelCountry(String name, String iso2Code, String ios3Code, String capitalCity, String region) {
        this.name = name;
        this.iso2Code = iso2Code;
        this.ios3Code = ios3Code;
        this.capitalCity = capitalCity;
        this.region = region;
    }

    private String name;
    private String iso2Code;
    private String ios3Code;
    private String capitalCity;
    private String region;

    @Override
    public String toString() {
        return name;
    }
}
