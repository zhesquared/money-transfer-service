package ru.netology.moneytransferservice.responce;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationConfirmation {

    private String code;
    private String operationId;
}