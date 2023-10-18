package ru.diplomatransfermoney.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.diplomatransfermoney.model.Amount;
import ru.diplomatransfermoney.model.Card;
import ru.diplomatransfermoney.model.reqest.TransferRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testing transfer repository....")
public class TransferMoneyRepositoryTest {


    @Autowired
    TransferMoneyRepository transferMoneyRepository;

    public static final String OPERATION_ID = "1";
    public static final int ID = 1;
    public static final String CONFIRMATION_CODE = "9999";
    public static final TransferRequest TRANSFER_REQUEST = new TransferRequest("1111222233334444", "01/30", "111",
            "5555666677778888", new Amount(1500, "RUR"));
    private final Map<String, Card> testCards = new ConcurrentHashMap<>();
    final String card1 = "1111111111111111";
    final String card2 = "2222222222222222";
    final String card3 = "3333333333333333";

    @BeforeEach
    void setUp() {
        testCards.put(card1, new Card(card1, "10/24", "123", new Amount(1000_00, "RUR")));
        testCards.put(card2, new Card(card2, "11/25", "234", new Amount(1000_00, "RUR")));
        testCards.put(card3, new Card(card3, "12/26", "345", new Amount(1000_00, "RUR")));
    }

    @ParameterizedTest
    @ValueSource(strings = {card1, card2, card3 })
    void getCard(String cardNumber) {
        assertEquals(testCards.get(cardNumber), transferMoneyRepository.getCard(cardNumber));
    }

    @Test
    @DisplayName("Put&Get -> TransferRequest:")
    void transferTests() {
        String operationId = OPERATION_ID;
        TransferRequest request = TRANSFER_REQUEST;
        transferMoneyRepository.putTransfer(operationId, request);

        TransferRequest requestActual = transferMoneyRepository.getTransfer(operationId);
        assertEquals(request, requestActual);

    }

    @Test
    @DisplayName("Get -> ID:")
    void incrementAndGetOperationIdTest() {
        int operationIdActual = transferMoneyRepository.incrementAndGetOperationId();
        assertEquals(ID, operationIdActual);
    }

    @Test
    @DisplayName("Save&Get -> confirmation code:")
    void codeTests() {

        String operationId = OPERATION_ID;
        String codeExpected = CONFIRMATION_CODE;
        transferMoneyRepository.putCode(operationId, codeExpected);

        String codeActual = transferMoneyRepository.getCode(operationId);
        assertEquals(codeExpected, codeActual);
    }

    @Test
    void incrementAndGetOperationId() {
        int forTestOperationId = 2;
        assertEquals(transferMoneyRepository.incrementAndGetOperationId(), forTestOperationId);
    }
}
