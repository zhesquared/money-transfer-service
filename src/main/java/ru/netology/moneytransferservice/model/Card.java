package ru.netology.moneytransferservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "card_data")
public class Card {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_valid_till")
    private String cardValidTill;

    @Column(name = "card_cvv")
    private String cardCVV;
}
