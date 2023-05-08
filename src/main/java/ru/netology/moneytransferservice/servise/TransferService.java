package ru.netology.moneytransferservice.servise;

import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exceptions.InvalidCardDataException;
import ru.netology.moneytransferservice.exceptions.InvalidConfirmationDataException;
import ru.netology.moneytransferservice.responce.OperationConfirmation;

public interface TransferService {

    Long transfer(Transfer transfer) throws InvalidCardDataException;
    boolean transferConfirmation(OperationConfirmation confirmation) throws InvalidConfirmationDataException;
    void transferExecution(String operationId);
    void cardDataValidation(Transfer transfer, Card validCardFrom) throws InvalidCardDataException;
}