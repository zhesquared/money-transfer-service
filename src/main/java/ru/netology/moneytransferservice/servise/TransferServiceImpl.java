package ru.netology.moneytransferservice.servise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.domain.Amount;
import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exceptions.InvalidCardDataException;
import ru.netology.moneytransferservice.exceptions.InvalidConfirmationDataException;
import ru.netology.moneytransferservice.repository.CardRepository;
import ru.netology.moneytransferservice.repository.TransferRepository;
import ru.netology.moneytransferservice.responce.OperationConfirmation;

import java.util.Objects;

@Service
public class TransferServiceImpl implements TransferService {

    private final String verificationCode;
    private final Double transferCommission;
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private static final Logger logger = LoggerFactory.getLogger("file-logger");

    @Autowired
    public TransferServiceImpl(@Value("${verification.code:0000}") String verificationCode,
                               @Value("${transfer.commission:0}") Double transferCommission,
                               TransferRepository transferRepository,
                               CardRepository cardRepository) {
        this.verificationCode = verificationCode;
        this.transferCommission = transferCommission;
        this.transferRepository = transferRepository;
        this.cardRepository = cardRepository;
    }


    @Override
    public Long transfer(Transfer transfer) throws InvalidCardDataException { //перевод

        String cardFromNumber = transfer.getCardFromNumber();
        Card validCardFrom = cardRepository.getCardByNumber(cardFromNumber).orElseThrow(
                () -> new InvalidCardDataException(
                        "Карты с номером [" +
                                cardFromNumber +
                                "] не существует. Попробуйте еще раз.")
        );

        cardDataValidation(transfer, validCardFrom);

        return transferRepository.addTransfer(transfer);
    }

    @Override
    public boolean transferConfirmation(OperationConfirmation confirmation) throws InvalidConfirmationDataException { //подтверждение

        if (!confirmation.getCode().equals(verificationCode)) {
            throw new InvalidConfirmationDataException(
                    "Неверный код подтверждения [" +
                            confirmation.getCode() +
                            "]!");
        }

        if (!transferRepository.confirmOperation(confirmation)) {
            throw new InvalidConfirmationDataException(
                    "Транзакции с идентификатором [" +
                            confirmation.getOperationId() +
                            "] не существует");
        }

        transferExecution(confirmation.getOperationId());

        return true;
    }

    @Override
    public void transferExecution(String operationId) {

        Transfer transfer = transferRepository.getTransferById(operationId);
        Card validCardFrom = cardRepository.getCardByNumber(transfer.getCardFromNumber()).get();

        String transferCurrency = transfer.getAmount().getCurrency();
        Double totalTransfer = (transfer.getAmount().getValue() * (1 + transferCommission)) / 100;
        Double balance = validCardFrom.getAmounts().get(transferCurrency).getValue() - totalTransfer;

        validCardFrom.getAmounts().put(transferCurrency, new Amount(balance, transferCurrency));

        logger.info("ID: {}. " +
                        "Перевод с карты: {}, " +
                        "сумма перевода: {}, " +
                        "карта получателя: {}, " +
                        "размер комиссии: {} {}, " +
                        "остаток на карте: {} {}.",
                operationId,
                transfer.getCardFromNumber(),
                transfer.getAmount(),
                transfer.getCardToNumber(),
                transfer.getAmount().getValue() * transferCommission / 100,
                transferCurrency,
                balance,
                transferCurrency);
    }


    @Override
    public void cardDataValidation(Transfer transfer, Card validCardFrom) throws InvalidCardDataException {

        boolean validTillIsCorrect = Objects.equals(validCardFrom.getCardValidTill(), transfer.getCardFromValidTill());
        boolean cvvIsCorrect = Objects.equals(validCardFrom.getCardCVV(), transfer.getCardFromCVV());
        if (!validTillIsCorrect || !cvvIsCorrect) {
            throw new InvalidCardDataException("Неккоректные данные карты: срок действия или код cvv");
        }

        String transferCurrency = transfer.getAmount().getCurrency();
        if (!validCardFrom.getAmounts().containsKey(transferCurrency)) {
            throw new InvalidCardDataException("К данной карте не привязан валютный счет в  " + transferCurrency);
        }

        Double cardAvailableAmount = validCardFrom.getAmounts().get(transferCurrency).getValue();
        Double totalTransfer = (transfer.getAmount().getValue() * (1 + transferCommission)) / 100;
        if (cardAvailableAmount < totalTransfer) {
            throw new InvalidCardDataException("Недостаточно средств. Баланс " +
                    cardAvailableAmount +
                    ", небходимо для перевода " +
                    totalTransfer);
        }
    }
}