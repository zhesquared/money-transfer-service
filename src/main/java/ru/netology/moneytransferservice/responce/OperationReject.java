package ru.netology.moneytransferservice.responce;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public class OperationReject {

    public static AtomicInteger idCounter = new AtomicInteger(0);
    private Integer id;
    private String message;
}
