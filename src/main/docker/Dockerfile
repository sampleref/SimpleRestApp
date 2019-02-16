FROM nas2docker/java8:latest


# create working directory
RUN mkdir -p /local/git
WORKDIR /local/git

RUN git clone https://github.com/sampleref/SimpleRestApp.git && \
    cd SimpleRestApp && \
    mvn clean install

CMD cd SimpleRestApp/target && \
    java -jar SimpleWebApp.jar


