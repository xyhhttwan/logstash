package cn.soeasypay.log.constants;

public enum TypeEnum {

    //api 接口
    REQUEST_LOG("10002"),
    // 业务
    BUSINESS_LOG("10003");

    private String type;

    TypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
