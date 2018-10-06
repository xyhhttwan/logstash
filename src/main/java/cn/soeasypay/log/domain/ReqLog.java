package cn.soeasypay.log.domain;

import java.io.Serializable;

public class ReqLog implements Serializable {


    /**
     * 请求类型
     *
     * @see cn.soeasypay.log.constants.TypeEnum
     */
    private String type;

    /**
     * 软件版本号
     */
    private String version;

    /**
     * 日志具体内容
     */
    private String content;

    public ReqLog() {
    }

    public ReqLog(String type, String version, String content) {
        this.type = type;
        this.version = version;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
