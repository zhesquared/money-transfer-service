package ru.netology.moneytransferservice.servise;

import ru.netology.moneytransferservice.model.Account;
import ru.netology.moneytransferservice.model.Card;

public interface TransferService {

    Card getCardByNumber(String cardNumber); //получаем данные карты по ее номеру

    int getAccountBalance(Card card); //получаем баланс карты

    boolean updateAccountBalance(Card card, int amount); //изменяем баланс карты

}
