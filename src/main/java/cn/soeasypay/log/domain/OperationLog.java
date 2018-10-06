package cn.soeasypay.log.domain;


import java.io.Serializable;
import java.util.UUID;

public class OperationLog implements Serializable {


    private String index = UUID.randomUUID().toString();

    /**
     * 执行的方法名称  类名:+方法名称
     */
    private String method;

    /**
     * 用户名称
     */
    private String userId;

    /**
     * 学校id
     */
    private Long schoolId;

    /**
     * 服务名称
     *
     * @see cn.soeasypay.log.constants.SystemEnum
     */
    private String serverId;

    /**
     * 上报时间
     */
    private long uploadTime;

    /**
     * 耗时 单位毫秒
     */
    private long runtime;

    /**
     * 请求类型
     *
     * @see cn.soeasypay.log.constants.TypeEnum
     */
    private String type;

    /**
     * 请求参数
     */
    private String reqParams;

    /**
     * 软件版本
     */
    private String version;


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getReqParams() {
        return reqParams;
    }

    public void setReqParams(String reqParams) {
        this.reqParams = reqParams;
    }

    public OperationLog() {

    }

    public OperationLog(String method, String userId, Long schoolId, String serverId, long uploadTime, long runtime, String type, String reqParams, String version) {
        this.method = method;
        this.userId = userId;
        this.schoolId = schoolId;
        this.serverId = serverId;
        this.uploadTime = uploadTime;
        this.runtime = runtime;
        this.type = type;
        this.reqParams = reqParams;
        this.version = version;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationLog that = (OperationLog) o;

        if (uploadTime != that.uploadTime) return false;
        if (runtime != that.runtime) return false;
        if (!index.equals(that.index)) return false;
        if (!method.equals(that.method)) return false;
        if (!userId.equals(that.userId)) return false;
        if (!schoolId.equals(that.schoolId)) return false;
        if (!serverId.equals(that.serverId)) return false;
        if (!type.equals(that.type)) return false;
        if (!reqParams.equals(that.reqParams)) return false;
        return version.equals(that.version);
    }

    @Override
    public int hashCode() {
        int result = index.hashCode();
        result = 31 * result + method.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + schoolId.hashCode();
        result = 31 * result + serverId.hashCode();
        result = 31 * result + (int) (uploadTime ^ (uploadTime >>> 32));
        result = 31 * result + (int) (runtime ^ (runtime >>> 32));
        result = 31 * result + type.hashCode();
        result = 31 * result + reqParams.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }


    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }
}
