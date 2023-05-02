package ru.netology.moneytransferservice.model.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.netology.moneytransferservice.model.Account;

@Data
@AllArgsConstructor
public class Transfer {

    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Account account;
}
