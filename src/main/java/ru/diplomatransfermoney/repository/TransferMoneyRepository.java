package ru.diplomatransfermoney.repository;

import ru.diplomatransfermoney.model.Card;
import ru.diplomatransfermoney.model.reqest.TransferRequest;

public interface TransferMoneyRepository {

    Card getCard(String cardNumber);
    void putTransfer(String id, TransferRequest transferRequest);
    TransferRequest getTransfer(String id);
    void putCode(String id, String code);
    String getCode(String id);
    int incrementAndGetOperationId();



}
