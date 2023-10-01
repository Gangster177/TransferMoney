FROM openjdk:17-oracle
EXPOSE 5500
COPY target/diplomaTransferMoney-0.0.1-SNAPSHOT.jar transfer.jar
CMD ["java", "-jar", "transfer.jar"]