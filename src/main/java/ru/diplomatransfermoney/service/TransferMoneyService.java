package ru.diplomatransfermoney.service;

import org.springframework.stereotype.Service;
import ru.diplomatransfermoney.model.reqest.ConfirmRequest;
import ru.diplomatransfermoney.model.reqest.TransferRequest;
import ru.diplomatransfermoney.model.response.TransferAndConfirmResponse;

@Service
public interface TransferMoneyService {
    TransferAndConfirmResponse transfer(TransferRequest request);

    TransferAndConfirmResponse confirmOperation(ConfirmRequest request);
}
