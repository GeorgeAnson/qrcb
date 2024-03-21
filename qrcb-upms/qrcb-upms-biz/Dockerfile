FROM java:8-jre

LABEL maintainer="Anson <georgeanson@gmail.com>" version="1.0.3"

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/"$TZ" /etc/localtime && echo "$TZ" > /etc/timezone

RUN mkdir -p /qrcb-upms-biz

WORKDIR /qrcb-upms-biz

EXPOSE 4000

ADD ./target/qrcb-upms-biz.jar ./

CMD sleep 60;java "$JAVA_OPTS" -jar qrcb-upms-biz.jar


