public class Country {

    private String name;
    private String code; // π.χ. GR, FR, DE

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Country() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
