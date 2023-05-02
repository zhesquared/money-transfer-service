package ru.netology.moneytransferservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.moneytransferservice.model.operations.OperationConfirmation;
import ru.netology.moneytransferservice.model.operations.Transfer;
import ru.netology.moneytransferservice.servise.TransferService;

@RestController
public class TransferController {

    @Autowired
    private final TransferService transferService;

    public TransferController(@Autowired TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> moneyTransfer(@RequestBody Transfer transfer) { //создать объект для возврата данных
        return new ResponseEntity<>("transfer is ok", HttpStatus.OK);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<?> confirmOperation(@RequestBody OperationConfirmation operationConfirmation) { //создать объект для возврата данных
        return new ResponseEntity<>("confirmOperation is ok", HttpStatus.OK);
    }
}
