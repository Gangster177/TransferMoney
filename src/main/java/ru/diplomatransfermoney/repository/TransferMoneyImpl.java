package ru.diplomatransfermoney.repository;

import org.springframework.stereotype.Repository;
import ru.diplomatransfermoney.model.Amount;
import ru.diplomatransfermoney.model.Card;
import ru.diplomatransfermoney.model.reqest.TransferRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TransferMoneyImpl implements TransferMoneyRepository{

    private final Map <String, TransferRequest> transfers = new ConcurrentHashMap<>();
    private final Map<String, Card> cards = new ConcurrentHashMap<>();
    private final Map<String, String> codes = new ConcurrentHashMap<>();
    private final AtomicInteger operationId = new AtomicInteger();

    {
        final String card1 = "1111111111111111";
        final String card2 = "2222222222222222";
        final String card3 = "3333333333333333";

        cards.put(card1, new Card(card1, "10/24", "123", new Amount(1000_00, "RUR")));
        cards.put(card2, new Card(card2, "11/25", "234", new Amount(1000_00, "RUR")));
        cards.put(card3, new Card(card3, "12/26", "345", new Amount(1000_00, "RUR")));
    }

    @Override
    public Card getCard(String cardNumber) {
        return cards.get(cardNumber);
    }

    @Override
    public void putTransfer(String id, TransferRequest transferRequest) {
        transfers.put(id, transferRequest);
    }

    @Override
    public TransferRequest getTransfer(String id) {
        return transfers.get(id);
    }

    @Override
    public void putCode(String id, String code) {
        codes.put(id, code);
    }

    @Override
    public String getCode(String id) {
        return codes.get(id);
    }

    @Override
    public int incrementAndGetOperationId() {
        return operationId.incrementAndGet();
    }
}
