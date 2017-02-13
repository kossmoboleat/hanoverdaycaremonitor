FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/daycaremap.jar /daycaremap/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/daycaremap/app.jar"]
