package ru.netology.moneytransferservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Amount {

    private Double value;
    private String currency;

    @Override
    public String toString() {
        return value / 100 +
                ", валюта: " + currency;
    }
}