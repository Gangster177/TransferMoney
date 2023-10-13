package ru.diplomatransfermoney.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.diplomatransfermoney.exception.ErrorInputDataException;
import ru.diplomatransfermoney.model.Card;
import ru.diplomatransfermoney.model.reqest.ConfirmRequest;
import ru.diplomatransfermoney.model.reqest.TransferRequest;
import ru.diplomatransfermoney.model.response.TransferAndConfirmResponse;
import ru.diplomatransfermoney.repository.TransferMoneyRepository;

@Slf4j
@Service
public class TransferMoneyService {

    private final TransferMoneyRepository repository;

    @Value("${verification.code:0000}")
    private String verificationCode;


    @Autowired
    public TransferMoneyService(TransferMoneyRepository repository) {
        this.repository = repository;
    }

    public TransferAndConfirmResponse transfer(TransferRequest request) {

        cardNumberVerification(request);
        cardDateVerification(request);
        transferAmountVerification(request);

        final String transferId = Integer.toString(repository.incrementAndGetOperationId());
        repository.putTransfer(transferId, request);
        repository.putCode(transferId, verificationCode);

        return new TransferAndConfirmResponse(transferId);
    }

    public TransferAndConfirmResponse confirmOperation(ConfirmRequest request) {
        final String operationId = request.getOperationId();
        final String code = repository.getCode(operationId);
        if (!request.getCode().equals(code) || code.isEmpty()) {
            log.error("confirm operation invalid data: code");
            throw new ErrorInputDataException("confirm operation invalid data: code");
        }
        final TransferRequest transferRequest = repository.getTransfer(operationId);
        final Card cardFrom = repository.getCard(transferRequest.getCardFromNumber());
        final Card cardTo = repository.getCard(transferRequest.getCardToNumber());

        final int cardFromValue = cardFrom.getBalanceCard().getValue();
        final int cardToValue = cardTo.getBalanceCard().getValue();
        final int transferValue = transferRequest.getAmount().getValue();
        final int commission = (int) (transferValue * 0.01);


        cardFrom.getBalanceCard().setValue(cardFromValue - transferValue);
        cardTo.getBalanceCard().setValue(cardToValue + transferValue - commission);
        log.info(String.format("Success transfer. Transfer operation id %s. Card from %s. Card to %s. Amount %d. Commission %d",
                operationId, transferRequest.getCardFromNumber(), transferRequest.getCardToNumber(), transferValue, commission));

        return new TransferAndConfirmResponse(operationId);
    }

    private void cardNumberVerification(TransferRequest request) {
        final Card cardFrom = repository.getCard(request.getCardFromNumber());
        final Card cardTo = repository.getCard(request.getCardToNumber());
        if (cardFrom == cardTo) {
            log.error("the card numbers match");
            throw new ErrorInputDataException("the card numbers match");
        }
    }

    private void transferAmountVerification(TransferRequest request) {
        final Card cardFrom = repository.getCard(request.getCardFromNumber());
        if (cardFrom.getBalanceCard().getValue() < request.getAmount().getValue()) {
            log.error("card from invalid data: not enough money");
            throw new ErrorInputDataException("card from invalid data: not enough money");
        }
    }

    private void cardDateVerification(TransferRequest request) {
        final Card cardFrom = repository.getCard(request.getCardFromNumber());
        final String cardFromValidTill = cardFrom.getValidTill();
        final String cardFromCVV = cardFrom.getCvv();
        final String transferRequestCardFromValidTill = request.getCardFromValidTill();
        final String transferRequestCardFromCVV = request.getCardFromCVV();

        if (!cardFromValidTill.equals(transferRequestCardFromValidTill) && cardFromCVV.equals(transferRequestCardFromCVV)) {
            log.error("incorrect card data entered: valid till");
            throw new ErrorInputDataException("incorrect card data entered: valid till");
        } else if (cardFromValidTill.equals(transferRequestCardFromValidTill) && !cardFromCVV.equals(transferRequestCardFromCVV)) {
            log.error("incorrect card data entered: cvv");
            throw new ErrorInputDataException("incorrect card data entered: cvv");
        } else if (!cardFromValidTill.equals(transferRequestCardFromValidTill)) {
            log.error("incorrect card data entered: valid till and cvv");
            throw new ErrorInputDataException("incorrect card data entered: valid till and cvv");
        }
    }

}
