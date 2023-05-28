package ru.netology.moneytransferservice.repository;

import ru.netology.moneytransferservice.domain.Card;

import java.util.Optional;

public interface CardRepository {

    Optional<Card> getCardByNumber(String cardNumber);
}