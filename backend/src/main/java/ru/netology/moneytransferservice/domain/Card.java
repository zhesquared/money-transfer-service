package ru.netology.moneytransferservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    private String cardNumber;
    private String cardValidTill;
    private String cardCVV;
    private String cardholderName;
    private String cardholderSurname;
    private Map<String, Amount> amounts;

}