package ru.diplomatransfermoney.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
            "2222222222222222",
            new Amount(150, "RUR"));
    public static final String OPERATION_ID = "1";
    public static final String CONFIRMATION_CODE = "1234";
    public static final Card TEST_CARD_FROM = new Card(
            "1111111111111111",
            "10/24",
            "123",
            new Amount(1000, "RUR"));
    public static final String CARD_NUMBER_FROM = "1111111111111111";
    @Mock
    private TransferMoneyRepository transferRepository;
    @InjectMocks
    TransferMoneyService transferService;

    @Test
    @DisplayName("Transfer() -> TransferMoneyService:")
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
    public void testConfirmOperation(){
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
}