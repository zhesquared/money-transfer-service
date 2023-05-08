package ru.netology.moneytransferservice.repository;

import org.springframework.stereotype.Repository;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.responce.OperationConfirmation;

@Repository
public interface TransferRepository {

    Long addTransfer(Transfer transfer);
    boolean confirmOperation(OperationConfirmation confirmation);
    Transfer getTransferById(String id);
}