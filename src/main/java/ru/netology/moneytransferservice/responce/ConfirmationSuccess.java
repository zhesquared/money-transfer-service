package ru.netology.moneytransferservice.responce;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfirmationSuccess {
    private String operationId;
}