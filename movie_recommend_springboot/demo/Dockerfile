FROM ubuntu:22.04

WORKDIR /my_boot

COPY . .

RUN apt update

RUN apt install openjdk-11-jre-headless -y

RUN apt install maven -y

RUN mvn package -Dmaven.test.skip=true

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/my_boot/target/demo-0.0.1-SNAPSHOT.jar"]