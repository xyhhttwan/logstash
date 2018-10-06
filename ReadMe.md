#### 校易收-日志服务

##### 项目说明
* 提供一个公共的jar 方便集成
* 下载jar
   * gradle 
      *      compile group: 'cn.soeasypay.log', name:'logs', version:'0.0.0.3-SNAPSHOT'

   * maven  
      *      <dependency>
                <groupId>cn.soeasypay.log</groupId>
                <artifactId>logs</artifactId>
                <version>0.0.0.6-SNAPSHOT</version>
              </dependency>
          
         
* 关键参数说明  
   *  reqUrl 日志发送的完整路径
   *  maxQueueNum 存储待发送队列队列最大值
   *  corePoolSize 存储待发送队列核心线程数量
   *  maximumPoolSize 存储待发送队列最大线程
   *  sendLogMaxQueueNum 发送日志等待队列大小
   *  sendLogCorePoolSize 发送日志核心线程
   *  sendLogMaximumPoolSize 发送日志最大线程

* 开始发送日志
   * 初始化与调用   
    *     OperationLog log = new OperationLog(....);
          LogClient.newBuilder().setReqUrl(LOG_SEND_URL).setCorePoolSize(2).build().instance().put(log);

* OperationLog 参数说明
    *        private String index = UUID.randomUUID().toString();//唯一index 采用uuid
             private String method;  //调用的方法 className:menthodName
             private String userId;  //调用者(可选)
             private Long schoolId;  //调用学校(可选)
             private String serverId; //调用系统 @see cn.soeasypay.log.constants.SystemEnum
             private long uploadTime; //接口调用时间(开始时间毫秒)
             private long runtime;    //接口耗时(毫秒)
             private String type;     //接口类型 @see cn.soeasypay.log.constants.TypeEnum
             private String reqParams; // 请求的接口全部参数(可选)
             private String version;   //系统版本
             

* 特殊说明  本jar 自动引用第三方jar 注意使用的时候如果jar重复需要排除
   * 校易收saas排除示列
     *         <dependency>
                       <groupId>cn.soeasypay.log</groupId>
                       <artifactId>logs</artifactId>
                       <version>0.0.0.4-SNAPSHOT</version>
                       <exclusions>
                           <exclusion>
                               <groupId>org.apache.commons</groupId>
                               <artifactId>commons-lang3</artifactId>
                           </exclusion>
                           <exclusion>
                               <groupId>com.google.guava</groupId>
                               <artifactId>guava</artifactId>
                           </exclusion>
                           <exclusion>
                               <groupId>org.slf4j</groupId>
                               <artifactId>slf4j-api</artifactId>
                           </exclusion>
                           <exclusion>
                               <groupId>com.alibaba</groupId>
                               <artifactId>fastjson</artifactId>
                           </exclusion>
                       </exclusions>
                   </dependency>