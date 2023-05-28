package ru.netology.moneytransferservice.servise;

import org.springframework.stereotype.Component;
import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exceptions.InvalidCardDataException;

import java.util.Objects;

@Component
public class CardDataValidationImpl implements CardDataValidation {
    @Override
    public void validTillValidation(Transfer transfer, Card validCardFrom) throws InvalidCardDataException {
        var validTillIsCorrect = Objects.equals(validCardFrom.cardValidTill(), transfer.cardFromValidTill());

        if (!validTillIsCorrect) {
            throw new InvalidCardDataException("Неккоректные данные карты: срок действия или код CVV");
        }
    }

    @Override
    public void CVVValidation(Transfer transfer, Card validCardFrom) throws InvalidCardDataException {
        var cvvIsCorrect = Objects.equals(validCardFrom.cardCVV(), transfer.cardFromCVV());

        if (!cvvIsCorrect) {
            throw new InvalidCardDataException("Неккоректные данные карты: срок действия или код CVV");
        }
    }

    @Override
    public void transferCurrencyValidation(Transfer transfer, Card validCardFrom) throws InvalidCardDataException {
        var transferCurrency = transfer.amount().currency();

        if (!validCardFrom.amounts().containsKey(transferCurrency)) {
            throw new InvalidCardDataException(
                    String.format("К данной карте не привязан валютный счет в %s", transferCurrency));
        }
    }

    @Override
    public void cardAvailableAmountValidation(Transfer transfer, Card validCardFrom, Double transferCommission) throws InvalidCardDataException {

        var transferCurrency = transfer.amount().currency();
        var cardAvailableAmount = validCardFrom.amounts().get(transferCurrency).value();
        var totalTransfer = (transfer.amount().value() * (1 + transferCommission)) / 100;

        if (cardAvailableAmount < totalTransfer) {
            throw new InvalidCardDataException(
                    String.format("Недостаточно средств. Баланс %,.2f, небходимо для перевода %,.2f",
                            cardAvailableAmount,
                            totalTransfer));
        }
    }
}
