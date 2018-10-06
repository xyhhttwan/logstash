package cn.soeasypay.log.constants;

public enum SystemEnum {

    SAAS("saas"),
    BOSS("boss"),
    ANGENT("agent"),
    CIP("cip"),
    NOTIFY("notify"),
    DC("dc"),
    CALLBACK("callback"),
    AUTH("auth");

    private String name;

    SystemEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
