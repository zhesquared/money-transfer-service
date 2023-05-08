package ru.netology.moneytransferservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exceptions.InvalidCardDataException;
import ru.netology.moneytransferservice.exceptions.InvalidConfirmationDataException;
import ru.netology.moneytransferservice.responce.ConfirmationSuccess;
import ru.netology.moneytransferservice.responce.OperationConfirmation;
import ru.netology.moneytransferservice.responce.TransferSuccess;
import ru.netology.moneytransferservice.servise.TransferService;

@RestController
@CrossOrigin
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferSuccess> transferMoney(@RequestBody Transfer transfer)
            throws InvalidCardDataException {

        Long id = transferService.transfer(transfer);
        return ResponseEntity.ok(new TransferSuccess(String.valueOf(id)));
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<ConfirmationSuccess> confirmOperation(@RequestBody OperationConfirmation confirmation)
            throws InvalidConfirmationDataException {

        transferService.transferConfirmation(confirmation);
        return ResponseEntity.ok(new ConfirmationSuccess(confirmation.getOperationId()));
    }

}
