FROM adoptopenjdk:11-jre-hotspot
#RUN mvn clean package
COPY target/*.jar organizer.jar
ENTRYPOINT ["java", "-jar", "organizer.jar"]
EXPOSE 8080