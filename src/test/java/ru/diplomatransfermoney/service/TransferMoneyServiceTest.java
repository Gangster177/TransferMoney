package ru.diplomatransfermoney.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.diplomatransfermoney.model.Amount;
import ru.diplomatransfermoney.model.reqest.TransferRequest;
import ru.diplomatransfermoney.model.response.TransferAndConfirmResponse;
import ru.diplomatransfermoney.repository.TransferMoneyRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class TransferMoneyServiceTest {

    private static final TransferRequest TRANSFER_REQUEST = new TransferRequest(
            "1111111111111111",
            "10/24",
            "123",
            "2222222222222222",
            new Amount(100_00, "RUR"));
    public static final String OPERATION_ID = "1";

    @Mock
    private TransferMoneyRepository transferRepository;
    @InjectMocks
    TransferMoneyService transferServiceImpl;

    @Test
    public void testTransfer() {
        when(transferRepository.incrementAndGetOperationId()).thenReturn(Integer.valueOf(OPERATION_ID));
        TransferAndConfirmResponse transferResponseActual = transferServiceImpl.transfer(TRANSFER_REQUEST);
        assertEquals("1", transferResponseActual.getOperationId());
    }
}