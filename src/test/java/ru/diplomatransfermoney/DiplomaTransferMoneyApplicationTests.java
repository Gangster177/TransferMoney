package ru.diplomatransfermoney;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.diplomatransfermoney.model.Amount;
import ru.diplomatransfermoney.model.reqest.ConfirmRequest;
import ru.diplomatransfermoney.model.reqest.TransferRequest;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiplomaTransferMoneyApplicationTests {

    private static final String HOST_WITHOUT_PORT = "http://localhost:";
    private static final String TRANSFER = "/transfer";
    private static final String CONFIRM_OPERATION_ENDPOINT = "/confirmOperation";
    private static final int PORT = 5500;
    private static final String expectedId = "{\"operationId\":\"1\"}";
    private static final ConfirmRequest CONFIRM_OPERATION_REQUEST = new ConfirmRequest("1", "0000");

    @Autowired
    TestRestTemplate testRestTemplate;

    @Container
    private static final GenericContainer<?> transferContainer = new GenericContainer<>("backend").withExposedPorts(PORT);
    private static final TransferRequest TRANSFER_REQUEST = new TransferRequest(
            "1111111111111111",
            "10/24",
            "123",
            "2222222222222222",
            new Amount(100_00, "RUR"));

    @Test
    void contextLoadsTransfer() {
        ResponseEntity<String> entity = testRestTemplate.postForEntity(HOST_WITHOUT_PORT + transferContainer.getMappedPort(PORT) + TRANSFER, TRANSFER_REQUEST, String.class);
        String actualId = entity.getBody();
        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    void contextLoadsConfirmOperation() {
        ResponseEntity<String> entity = testRestTemplate.postForEntity(HOST_WITHOUT_PORT + transferContainer.getMappedPort(PORT) + CONFIRM_OPERATION_ENDPOINT,
                CONFIRM_OPERATION_REQUEST, String.class);
        String actualId = entity.getBody();
        Assertions.assertEquals(expectedId, actualId);
    }
}