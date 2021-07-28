FROM openjdk:11
ADD ./target/demo-todolist-0.0.1-SNAPSHOT.jar /usr/src/demo-todolist-0.0.1-SNAPSHOT.jar
WORKDIR usr/src
ENTRYPOINT ["java","-jar", "demo-todolist-0.0.1-SNAPSHOT.jar"]