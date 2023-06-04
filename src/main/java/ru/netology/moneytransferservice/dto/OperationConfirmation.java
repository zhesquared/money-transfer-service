package ru.netology.moneytransferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationConfirmation {

    private String code;
    private String operationId;
}