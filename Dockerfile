FROM anolis-registry.cn-zhangjiakou.cr.aliyuncs.com/openanolis/openjdk:17-8.6 as build
LABEL authors="Yuzoi"

RUN mkdir -p "/workspace/app"

WORKDIR "/workspace/app"

ENV SPRING_PROFILES_ACTIVE=prod

COPY ./target/forum-0.0.1-SNAPSHOT.jar /workspace/app/forum.jar

CMD ["java","-jar","/workspace/app/forum.jar"]