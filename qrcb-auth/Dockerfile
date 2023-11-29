FROM java:8-jre

MAINTAINER georgeanson@gmail.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /qrcb-auth

WORKDIR /qrcb-auth

EXPOSE 3000

ADD ./target/qrcb-auth.jar ./

CMD sleep 120;java $JAVA_OPTS -jar qrcb-auth.jar


