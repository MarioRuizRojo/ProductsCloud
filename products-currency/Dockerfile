FROM gradle:7.6.1-jdk17-alpine
COPY . /defaultfolder
RUN cd /defaultfolder && gradle clean bootJar
EXPOSE 80
CMD java -jar /defaultfolder/build/libs/productscurrency-0.0.1-SNAPSHOT.jar
