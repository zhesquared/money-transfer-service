package ru.netology.moneytransferservice.servise;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exception.InvalidCardDataException;

import java.util.Objects;

@Component
public class CardDataValidationImpl implements CardDataValidation {
    @SneakyThrows
    @Override
    public boolean validTillValidation(Transfer transfer, Card validCardFrom) {
        var validTillIsCorrect = Objects.equals(validCardFrom.cardValidTill(), transfer.cardFromValidTill());

        if (!validTillIsCorrect) {
            throw new InvalidCardDataException("Неккоректные данные карты: срок действия или код CVV");
        }
        return true;
    }

    @SneakyThrows
    @Override
    public boolean CVVValidation(Transfer transfer, Card validCardFrom)  {
        var cvvIsCorrect = Objects.equals(validCardFrom.cardCVV(), transfer.cardFromCVV());

        if (!cvvIsCorrect) {
            throw new InvalidCardDataException("Неккоректные данные карты: срок действия или код CVV");
        }
        return true;
    }

    @SneakyThrows
    @Override
    public boolean transferCurrencyValidation(Transfer transfer, Card validCardFrom) {
        var transferCurrency = transfer.amount().currency();

        if (!validCardFrom.amounts().containsKey(transferCurrency)) {
            throw new InvalidCardDataException(
                    String.format("К данной карте не привязан валютный счет в %s", transferCurrency));
        }
        return true;
    }

    @SneakyThrows
    @Override
    public boolean cardAvailableAmountValidation(Transfer transfer, Card validCardFrom, Double transferCommission) {

        var transferCurrency = transfer.amount().currency();
        var cardAvailableAmount = validCardFrom.amounts().get(transferCurrency).value();
        var totalTransfer = (transfer.amount().value() * (1 + transferCommission)) / 100;

        if (cardAvailableAmount < totalTransfer) {
            throw new InvalidCardDataException(
                    String.format("Недостаточно средств. Баланс %,.2f, небходимо для перевода %,.2f",
                            cardAvailableAmount,
                            totalTransfer));
        }
        return true;
    }
}
