package ru.netology.moneytransferservice.domain;

public record Amount(Double value, String currency) {

    @Override
    public String toString() {
        return value / 100 +
                ", валюта: " + currency;
    }
}