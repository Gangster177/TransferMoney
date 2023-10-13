package ru.diplomatransfermoney.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
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
            "1111222233334444",
            "01/29",
            "345",
            "5555666677778888",
            new Amount(15000, "RUR"));
    public static final String OPERATION_ID = "1";
    public static final Card TEST_CARD_FROM = new Card(
            "1111222233334444",
            "01/25",
            "345",
            new Amount(1000, "RUR"));
    public static final String CARD_NUMBER_FROM = "1111222233334444";
    @Mock
    private TransferMoneyRepository transferRepository;
    @InjectMocks
    TransferMoneyService transferService;

    @Test
    public void testTransfer() {
        //given
        TransferAndConfirmResponse expected = new TransferAndConfirmResponse(OPERATION_ID);
        //when
        when(transferRepository.incrementAndGetOperationId()).thenReturn(Integer.valueOf(OPERATION_ID));
        when(transferRepository.getCard(CARD_NUMBER_FROM)).thenReturn(TEST_CARD_FROM);

        //then
        TransferAndConfirmResponse transferResponseActual = transferService.transfer(TRANSFER_REQUEST);
        assertEquals(expected.getOperationId(), transferResponseActual.getOperationId());

        //TODO Встал в ступор, не могу разобраться...
//        2023-10-13T13:55:49.218+03:00 ERROR 9168 --- [           main] r.d.service.TransferMoneyService         : incorrect card data entered: valid till
//
//        ru.diplomatransfermoney.exception.ErrorInputDataException: incorrect card data entered: valid till
//
//        at ru.diplomatransfermoney.service.TransferMoneyService.cardDateVerification(TransferMoneyService.java:94)
//        at ru.diplomatransfermoney.service.TransferMoneyService.transfer(TransferMoneyService.java:33)
//        at ru.diplomatransfermoney.service.TransferMoneyServiceTest.testTransfer(TransferMoneyServiceTest.java:53)
//
    }
}