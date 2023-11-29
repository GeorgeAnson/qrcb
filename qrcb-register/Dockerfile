FROM java:8-jre

MAINTAINER georgeanson@gmail.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /qrcb-register

WORKDIR /qrcb-register

EXPOSE 8848

ADD ./target/qrcb-register.jar ./

CMD sleep 30;java $JAVA_OPTS -jar qrcb-register.jar


