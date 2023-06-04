package ru.netology.moneytransferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferSuccess {

    private String operationId;
}