package ru.netology.moneytransferservice.domain;

import java.util.Map;

public record Card(
        String cardNumber,
        String cardValidTill,
        String cardCVV,
        String cardholderName,
        String cardholderSurname,
        Map<String, Amount> amounts) {
}