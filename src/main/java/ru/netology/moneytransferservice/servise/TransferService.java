package ru.netology.moneytransferservice.servise;

import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exception.InvalidConfirmationDataException;
import ru.netology.moneytransferservice.dto.OperationConfirmation;
import ru.netology.moneytransferservice.dto.TransferSuccess;

public interface TransferService {

    TransferSuccess transfer(Transfer transfer);

    boolean transferConfirmation(OperationConfirmation confirmation) throws InvalidConfirmationDataException;

    void transferExecution(String operationId);
}