FROM centos:7
RUN yum update upgrade -y && \
    yum install -y java-1.8.0-openjdk-devel && \
    yum -y clean all 
ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.242.b08-0.el7_7.x86_64
ADD qms-0.0.1-SNAPSHOT.jar qms.jar
ADD application.properties application.properties
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /qms.jar --spring.config.location=/application.properties" ]