package cn.soeasypay.log.client;

import com.google.common.base.Preconditions;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @author baixiaobin
 * okhttp client
 */
public class OperationLogClient {

    private OkHttpClient operationLogClient;

    /**
     * @param connectTimeout 0 表示不超时
     * @param readTimeout    0 表示不超时
     * @param writeTimeout   0 表示不超时
     */
    public OperationLogClient(int connectTimeout, int readTimeout, int writeTimeout) {
        Preconditions.checkArgument(connectTimeout < Integer.MAX_VALUE && connectTimeout >= 0);
        Preconditions.checkArgument(readTimeout < Integer.MAX_VALUE && readTimeout >= 0);
        Preconditions.checkArgument(writeTimeout < Integer.MAX_VALUE && writeTimeout >= 0);

        operationLogClient = new OkHttpClient();
        operationLogClient.newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS);
    }

    public OkHttpClient getInstance() {
        return operationLogClient;
    }

}
