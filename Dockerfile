FROM maven
COPY . /app/

WORKDIR /app

RUN mvn install

ENTRYPOINT ["mvn", "verify"]