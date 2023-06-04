package ru.netology.moneytransferservice.servise;

import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;

public interface CardDataValidation {

    boolean validTillValidation(Transfer transfer, Card validCardFrom);

    boolean CVVValidation(Transfer transfer, Card validCardFrom);

    boolean transferCurrencyValidation(Transfer transfer, Card validCardFrom);

    boolean cardAvailableAmountValidation(Transfer transfer, Card validCardFrom, Double transferCommission);

}
