package ru.netology.moneytransferservice.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.model.Account;
import ru.netology.moneytransferservice.model.Card;
import ru.netology.moneytransferservice.repository.CardRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransferServiceImpl implements TransferService {

    private static final Map<Card, Account> ACCOUNT_REPO = new HashMap<>();

    private static final AtomicLong CARD_ID_HOLDER = new AtomicLong(); //переменная для генерации номера операции

    @Autowired
    private final CardRepository cardRepository;

    public TransferServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card getCardByNumber(String cardNumber) {
        return cardRepository.getOne(Integer.valueOf(cardNumber));
    }

    @Override
    public int getAccountBalance(Card card) {
        return ACCOUNT_REPO.get(card).getAmount();
    }

    @Override
    public boolean updateAccountBalance(Card card, int amount) {

        ACCOUNT_REPO.get(card).setAmount(amount); // реализовать проверку на положительный баланс

        return true;
    }
}
