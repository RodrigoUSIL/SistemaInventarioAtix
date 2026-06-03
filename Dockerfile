FROM tomcat:10-jdk22
COPY dist/SistemaInventarioTF.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]