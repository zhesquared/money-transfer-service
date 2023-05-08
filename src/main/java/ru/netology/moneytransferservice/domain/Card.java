package ru.netology.moneytransferservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String cardNumber;
    private String cardValidTill;
    private String cardCVV;
    private String cardholderName;
    private String cardholderSurname;
//    private Map<String, Amount> amounts;


//    public Card(String cardNumber, String cardValidTill, String cardCVV) {
//        this.cardNumber = cardNumber;
//        this.cardValidTill = cardValidTill;
//        this.cardCVV = cardCVV;
//    }
}