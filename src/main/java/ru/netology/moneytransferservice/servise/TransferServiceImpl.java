package ru.netology.moneytransferservice.servise;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.domain.Amount;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.dto.OperationConfirmation;
import ru.netology.moneytransferservice.dto.TransferSuccess;
import ru.netology.moneytransferservice.exception.InvalidCardDataException;
import ru.netology.moneytransferservice.exception.InvalidConfirmationDataException;
import ru.netology.moneytransferservice.repository.CardRepository;
import ru.netology.moneytransferservice.repository.TransferRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    @Value("${verification.code}")
    private String verificationCode;
    @Value("${transfer.commission}")
    private Double transferCommission;
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final CardDataValidation cardDataValidation;

    private static final Logger logger = LoggerFactory.getLogger("file-logger");

    @SneakyThrows
    @Override
    public TransferSuccess transfer(Transfer transfer) {

        var cardFromNumber = transfer.cardFromNumber();
        var validCardFrom = cardRepository
                .getCardByNumber(cardFromNumber)
                .orElseThrow(
                        () -> new InvalidCardDataException(
                                String.format("Карты с номером [%s] не существует. Попробуйте еще раз.", cardFromNumber)));
        Optional.of(validCardFrom)
                .filter(card -> cardDataValidation.validTillValidation(transfer, card))
                .filter(card -> cardDataValidation.CVVValidation(transfer, card))
                .filter(card -> cardDataValidation.transferCurrencyValidation(transfer, card))
                .filter(card -> cardDataValidation.cardAvailableAmountValidation(transfer, card, this.transferCommission));

        return new TransferSuccess(String.valueOf(transferRepository.addTransfer(transfer)));
    }

    @Override
    public boolean transferConfirmation(OperationConfirmation confirmation) throws InvalidConfirmationDataException {

        if (!confirmation.getCode().equals(verificationCode)) {
            throw new InvalidConfirmationDataException(
                    String.format("Неверный код подтверждения [%s]!", confirmation.getCode())
            );
        }

        if (!transferRepository.confirmOperation(confirmation)) {
            throw new InvalidConfirmationDataException(
                    String.format("Транзакции с идентификатором [%s] не существует.", confirmation.getOperationId()));
        }

        transferExecution(confirmation.getOperationId());

        return true;
    }

    @Override
    public void transferExecution(String operationId) {

        var transfer = transferRepository.getTransferById(operationId);
        var validCardFrom = cardRepository.getCardByNumber(transfer.cardFromNumber()).get();

        var transferCurrency = transfer.amount().currency();
        var totalTransfer = (transfer.amount().value() * (1 + transferCommission)) / 100;
        var balance = validCardFrom.amounts().get(transferCurrency).value() - totalTransfer;

        validCardFrom.amounts().put(transferCurrency, new Amount(balance, transferCurrency));

        logger.info("ID: {}." + operationId);
    }
}