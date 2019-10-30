FROM openjdk:8u181-jdk  
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime
ADD idjava.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+08","-jar","/app.jar"]
EXPOSE 9011