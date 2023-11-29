FROM java:8-jre

MAINTAINER georgeanson@gmail.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /qrcb-gateway

WORKDIR /qrcb-gateway

EXPOSE 9999

ADD ./target/qrcb-gateway.jar ./

CMD sleep 180;java $JAVA_OPTS -jar qrcb-gateway.jar


