package ru.diplomatransfermoney.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.diplomatransfermoney.exception.ErrorInputDataException;
import ru.diplomatransfermoney.model.Card;
import ru.diplomatransfermoney.model.reqest.ConfirmRequest;
import ru.diplomatransfermoney.model.reqest.TransferRequest;
import ru.diplomatransfermoney.model.response.TransferAndConfirmResponse;
import ru.diplomatransfermoney.repository.TransferMoneyRepository;
/*
 * 1. Метод "перевод" получает запрос, содержащий данные карты
 * и данные о сумме и валюте перевода в качестве входных данных
 * В методе обрабатываются данные, генерируется идентификатор операции и все данные сохраняются в хранилище
 * Далее метод отправляет клиенту код подтверждения
 *
 * * 2.метод "подтвердить операцию" получает запрос от клиента,
 * содержащий идентификационные данные операции и код подтверждения
 * Этот код сравнивается с кодом из репозитория, и если коды совпадают, то операция подтверждается
 * Клиент получает ответ с идентификатором операции
 * Баланс на картах меняется
 *
 */
@Service
public class TransferMoneyServiceImpl implements TransferMoneyService{

    private final TransferMoneyRepository repository;

    @Value("${transfer.commission:0}")
    private Double transferCommission;

    @Value("${verification.code:0000}")
    private String verificationCode;

    @Autowired
    public TransferMoneyServiceImpl(TransferMoneyRepository repository) {
        this.repository = repository;
    }

    @Override
    public TransferAndConfirmResponse transfer(TransferRequest request) {
        final Card cardFrom = repository.getCard(request.getCardFromNumber());
        final Card cardTo = repository.getCard(request.getCardToNumber());

         if (cardFrom == cardTo) {
            throw new ErrorInputDataException("two identical card numbers");
        }

        final String cardFromValidTill = cardFrom.getValidTill();
        final String cardFromCVV = cardFrom.getCvv();
        final String transferRequestCardFromValidTill = request.getCardFromValidTill();
        final String transferRequestCardFromCVV = request.getCardFromCVV();

        if (!cardFromValidTill.equals(transferRequestCardFromValidTill) && cardFromCVV.equals(transferRequestCardFromCVV)) {
            throw new ErrorInputDataException("card from invalid data: valid till");
        } else if (cardFromValidTill.equals(transferRequestCardFromValidTill) && !cardFromCVV.equals(transferRequestCardFromCVV)) {
            throw new ErrorInputDataException("card from invalid data: cvv");
        } else if (!cardFromValidTill.equals(transferRequestCardFromValidTill) && !cardFromCVV.equals(transferRequestCardFromCVV)) {
            throw new ErrorInputDataException("card from invalid data: valid till and cvv");
        }

        if (cardFrom.getBalanceCard().getValue() < request.getAmount().getValue()) {
            throw new ErrorInputDataException("card from invalid data: not enough money");
        }

        final String transferId = Integer.toString(repository.incrementAndGetOperationId());
        repository.putTransfer(transferId, request);
        repository.putCode(transferId, verificationCode);

        return new TransferAndConfirmResponse(transferId);
    }

    @Override
    public TransferAndConfirmResponse confirmOperation(ConfirmRequest request) {
        final String operationId = request.getOperationId();
        final String code = repository.getCode(operationId);
        if (!request.getCode().equals(code) || code.isEmpty()) {
            throw new ErrorInputDataException("confirm operation invalid data: code");
        }
        final TransferRequest transferRequest = repository.getTransfer(operationId);
        final Card cardFrom = repository.getCard(transferRequest.getCardFromNumber());
        final Card cardTo = repository.getCard(transferRequest.getCardToNumber());

        final int cardFromValue = cardFrom.getBalanceCard().getValue();
        final int cardToValue = cardTo.getBalanceCard().getValue();
        final int transferValue = transferRequest.getAmount().getValue();
        final int commission = (int) (transferValue * transferCommission);

        cardFrom.getBalanceCard().setValue(cardFromValue - transferValue);
        cardTo.getBalanceCard().setValue(cardToValue + transferValue - commission);

        return new TransferAndConfirmResponse(operationId);
    }

}
