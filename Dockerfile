FROM eclipse-temurin:22-jdk AS build
WORKDIR /app
COPY dist/SistemaInventarioTF.war app.war

FROM tomcat:10.1-jdk21
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=build /app/app.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]