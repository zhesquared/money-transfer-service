package ru.netology.moneytransferservice.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Amount {
    private Integer value; //протокол
    private String currency; // протокол

    @Override
    public String toString() {
        return value + " " + currency;
    }
}