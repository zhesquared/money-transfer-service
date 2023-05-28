package ru.netology.moneytransferservice.servise;

import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exceptions.InvalidConfirmationDataException;
import ru.netology.moneytransferservice.responce.OperationConfirmation;
import ru.netology.moneytransferservice.responce.TransferSuccess;

public interface TransferService {

    TransferSuccess transfer(Transfer transfer);

    boolean transferConfirmation(OperationConfirmation confirmation) throws InvalidConfirmationDataException;

    void transferExecution(String operationId);
}