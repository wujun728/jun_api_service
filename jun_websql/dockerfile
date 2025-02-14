FROM  majiajue/jdk1.8

MAINTAINER cgy<boy_0214@sina.com>

USER root

RUN mkdir -p /websql

WORKDIR /websql

EXPOSE 80

COPY ./websql.jar /websql/app.jar

ENV TimeZone=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TimeZone /etc/localtime && echo $TimeZone > /etc/timezone

ENTRYPOINT ["java","-jar","app.jar"]