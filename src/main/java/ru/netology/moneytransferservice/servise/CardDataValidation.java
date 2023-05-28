package ru.netology.moneytransferservice.servise;

import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exceptions.InvalidCardDataException;

public interface CardDataValidation {

    public void validTillValidation(Transfer transfer, Card validCardFrom) throws InvalidCardDataException;

    public void CVVValidation(Transfer transfer, Card validCardFrom) throws InvalidCardDataException;

    public void transferCurrencyValidation(Transfer transfer, Card validCardFrom) throws InvalidCardDataException;

    public void cardAvailableAmountValidation(Transfer transfer, Card validCardFrom, Double transferCommission) throws InvalidCardDataException;

}
