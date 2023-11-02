FROM eclipse-temurin:17-alpine
EXPOSE 8080
RUN mkdir /recipebuddy
COPY target/recipebuddy-*.jar /recipebuddy/recipebuddy.jar
WORKDIR /recipebuddy
ENTRYPOINT ["java", "-jar", "/recipebuddy/recipebuddy.jar"]