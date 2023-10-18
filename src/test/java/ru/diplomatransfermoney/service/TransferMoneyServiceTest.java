package ru.diplomatransfermoney.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ru.diplomatransfermoney.model.Amount;
import ru.diplomatransfermoney.model.Card;
import ru.diplomatransfermoney.model.reqest.ConfirmRequest;
import ru.diplomatransfermoney.model.reqest.TransferRequest;
import ru.diplomatransfermoney.model.response.TransferAndConfirmResponse;
import ru.diplomatransfermoney.repository.TransferMoneyRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class TransferMoneyServiceTest {

    public static final TransferRequest TRANSFER_REQUEST = new TransferRequest(
            "1111111111111111",
            "10/24",
            "123",
            "4444444444444444",
            new Amount(150, "RUR"));
    public static final String OPERATION_ID = "1";
    public static final String CONFIRMATION_CODE = "1234";
    public static final Card TEST_CARD_FROM = new Card(
            "1111111111111111",
            "10/24",
            "123",
            new Amount(1000, "RUR"));
    public static final Card TEST_CARD_TO = new Card(
            "4444444444444444",
            "12/27",
            "456",
            new Amount(0, "RUR"));

    public static final String CARD_NUMBER_FROM = "1111111111111111";
    public static final String CARD_NUMBER_TO = "4444444444444444";
    @Mock
    private TransferMoneyRepository transferRepository;
    @InjectMocks
    TransferMoneyService transferService;

    @Test
    @DisplayName("transfer() -> TransferMoneyService:")
    public void testTransfer() {
        //given
        TransferAndConfirmResponse expected = new TransferAndConfirmResponse(OPERATION_ID);
        //when
        when(transferRepository.incrementAndGetOperationId()).thenReturn(Integer.valueOf(OPERATION_ID));
        when(transferRepository.getCard(CARD_NUMBER_FROM)).thenReturn(TEST_CARD_FROM);
        //then
        TransferAndConfirmResponse transferResponseActual = transferService.transfer(TRANSFER_REQUEST);
        assertEquals(expected.getOperationId(), transferResponseActual.getOperationId());
    }

    @Test
    @DisplayName("confirmOperation() -> TransferMoneyService:")
    public void testConfirmOperation() {
        ConfirmRequest request = new ConfirmRequest(OPERATION_ID, CONFIRMATION_CODE);
        String operationId = request.getOperationId();

        TransferAndConfirmResponse responseExpected = new TransferAndConfirmResponse(operationId);
        TransferMoneyService spy = spy(transferService);

        //when
        when(transferRepository.getCode(operationId)).thenReturn(CONFIRMATION_CODE);
        doNothing().when(spy).changeBalance(operationId);

        //then
        TransferAndConfirmResponse responseActual = spy.confirmOperation(request);
        assertEquals(responseExpected.getOperationId(), responseActual.getOperationId());
    }

    @Test
    @DisplayName("changeBalance() -> TransferMoneyService:")
    public void testChangeBalance() {
        //given
        int balanceCardFromExpected = 850;
        int balanceCardToExpected = 149;
        //when
        when(transferRepository.getTransfer(OPERATION_ID)).thenReturn(TRANSFER_REQUEST);
        when(transferRepository.getCard(CARD_NUMBER_FROM)).thenReturn(TEST_CARD_FROM);
        when(transferRepository.getCard(CARD_NUMBER_TO)).thenReturn(TEST_CARD_TO);
        //then
        transferService.changeBalance(OPERATION_ID);
        assertEquals(balanceCardFromExpected, TEST_CARD_FROM.getBalanceCard().getValue());
        assertEquals(balanceCardToExpected, TEST_CARD_TO.getBalanceCard().getValue());
    }
}