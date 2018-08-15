#!/bin/sh
export LANG=zh_CN.utf8
export CLASSPATH=./bin:./lib/ant.jar:./lib/aopalliance-1.0.jar:./lib/asm-all-5.0.3.jar:./lib/aspectjweaver.jar:./lib/cas-client-core-3.2.0.jar:./lib/CCP_REST_SDK_JAVA_v2.7r.jar:./lib/commons-beanutils-1.9.1.jar:./lib/commons-codec-1.9.jar:./lib/commons-collections-3.2.jar:./lib/commons-dbcp2-2.0.jar:./lib/commons-discovery-0.2.jar:./lib/commons-fileupload-1.3.1.jar:./lib/commons-io-2.4.jar:./lib/commons-logging-1.1.3.jar:./lib/commons-pool2-2.2.jar:./lib/dom4j-1.6.1.jar:./lib/fastjson1.1.40.jar:./lib/fsdp2_common_2.0.24.jar:./lib/fsdp2_server.jar:./lib/fsdp2_service_2.0.24.jar:./lib/gitective-core-0.9.9.jar:./lib/gitlab4j-api-4.6.2.jar:./lib/httpclient-4.5.jar:./lib/httpclient-cache-4.2.1-atlassian-2.jar:./lib/httpcore-4.4.1.jar:./lib/jackson-annotations-2.9.2.jar:./lib/jackson-core-2.9.1.jar:./lib/jackson-core-asl-1.9.4.jar:./lib/jackson-core-lgpl-1.9.6.jar:./lib/jackson-databind-2.9.2.jar:./lib/jackson-jaxrs-base-2.8.5.jar:./lib/jackson-jaxrs-json-provider-2.9.2.jar:./lib/jackson-mapper-asl-1.9.5.jar:./lib/jackson-mapper-lgpl-1.9.6.jar:./lib/java-gitlab-api-1.2.8.jar:./lib/javax.annotation-api-1.2.jar:./lib/javax.inject-1.jar:./lib/javax.ws.rs-api-2.0.1.jar:./lib/jax-rs-ri-2.0-rc1.jar:./lib/jaxb2-basics-annotate-1.0.2.jar:./lib/jedis-2.8.0.jar:./lib/json.jar:./lib/jsoup-1.8.1.jar:./lib/jstl-1.1.2.jar:./lib/log4j-api-2.0-rc1.jar:./lib/log4j-core-2.0-rc1.jar:./lib/log4j-slf4j-impl-2.0-rc1.jar:./lib/mail.jar:./lib/mimepull-1.4.jar:./lib/mysql-connector-java-5.1.30-bin.jar:./lib/ojdbc6.jar:./lib/okhttp-3.5.0.jar:./lib/okio-1.11.0.jar:./lib/org.eclipse.jgit-4.9.0.201710071750-r.jar:./lib/poi-3.10.jar:./lib/poi-ooxml-3.10.jar:./lib/poi-ooxml-schemas-3.10.jar:./lib/servlet-api.jar:./lib/simplecaptcha-1.2.1.jar:./lib/slf4j-api-1.7.21.jar:./lib/spring-aop-4.0.1.RELEASE.jar:./lib/spring-beans-4.0.1.RELEASE.jar:./lib/spring-context-4.0.1.RELEASE.jar:./lib/spring-core-4.0.1.RELEASE.jar:./lib/spring-data-redis-1.6.4.RELEASE.jar:./lib/spring-expression-4.0.1.RELEASE.jar:./lib/spring-jdbc-4.0.1.RELEASE.jar:./lib/spring-oxm-4.1.9.RELEASE.jar:./lib/spring-security-config-3.0.0.RC2.jar:./lib/spring-security-core-3.0.0.RC2.jar:./lib/spring-security-web-3.0.0.RC2.jar:./lib/spring-tx-4.0.1.RELEASE.jar:./lib/spring-web-4.0.1.RELEASE.jar:./lib/spring-webmvc-4.0.1.RELEASE.jar:./lib/standard-1.1.2.jar:./lib/tiger-types-1.2.jar:./lib/wom-0.9.0.jar:./lib/xmlbeans-2.3.0.jar:.
export JAVA_HOME=/datafine/local/jdk1.7.0_80
cd /datafine/local/gitlab
auditengine_pid=`ps -wef | grep gitlabdaemon | grep -v grep | awk '{print $2}'`
if test -z $auditengine_pid
then
        $JAVA_HOME/bin/java -Dgitlabdaemon -Xms512m -Xmx512m -classpath $CLASSPATH cn.finedo.daemon.gitlab.count.GitlabCountDaemon
else
        echo "The gitlabdaemon has running , pid=${auditengine_pid}"
fi
#01 0 01 */1 * /datafine/local/gitlab/gitlab.sh >> /datafine/local/gitlab/gitlab.log