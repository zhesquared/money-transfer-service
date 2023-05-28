package ru.netology.moneytransferservice.domain;

public record Transfer(
        String cardFromNumber,
        String cardFromValidTill,
        String cardFromCVV,
        String cardToNumber,
        Amount amount) {}