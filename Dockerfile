FROM openjdk:11
#RUN mvn clean package
COPY target/*.jar organizer.jar
ENTRYPOINT ["java", "-jar", "organizer.jar"]
EXPOSE 8080