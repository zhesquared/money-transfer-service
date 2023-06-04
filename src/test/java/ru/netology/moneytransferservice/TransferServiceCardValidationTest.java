package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.moneytransferservice.domain.Amount;
import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exception.InvalidCardDataException;
import ru.netology.moneytransferservice.servise.CardDataValidationImpl;
import ru.netology.moneytransferservice.servise.TransferServiceImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TransferServiceCardValidationTest {

    @InjectMocks
    private TransferServiceImpl transferService;
    @InjectMocks
    private CardDataValidationImpl cardDataValidation;
    private final static Card VALID_CARD = new Card("4875131749697170", "12/23", "498", "Shcherbakov", "Mikhail",
            new ConcurrentHashMap<>(Map.of("RUR", new Amount(15_220.0, "RUR"))));

    @Test
    public void testValidationWhenIncorrectTillDateWhenThrowException() {
        Transfer incorrectTillDate = new Transfer(
                "4875131749697170", "01/23", "498", "4474958586817833",
                new Amount(400.0, "RUR"));

        Exception exception = Assertions.assertThrows(InvalidCardDataException.class,
                () -> cardDataValidation.validTillValidation(incorrectTillDate, VALID_CARD));

        assertThat(exception).hasMessageThat().contains("Неккоректные данные карты: срок действия или код CVV");
    }

    @Test
    public void testValidationWhenIncorrectCVVWhenThrowException() {
        Transfer invalidCVV = new Transfer(
                "4875131749697170", "12/20", "999", "4474958586817833",
                new Amount(400.0, "RUR"));

        Exception exception = Assertions.assertThrows(InvalidCardDataException.class,
                () -> cardDataValidation.CVVValidation(invalidCVV, VALID_CARD));

        assertThat(exception).hasMessageThat().contains("Неккоректные данные карты: срок действия или код CVV");
    }

    @Test
    public void testValidationWhenIncorrectCurrencyWhenThrowException() {
        Transfer invalidCurrency = new Transfer(
                "4875131749697170", "01/23", "498", "4474958586817833",
                new Amount(20.0, "USD"));
        String transferCurrency = invalidCurrency.amount().currency();

        Exception exception = Assertions.assertThrows(InvalidCardDataException.class,
                () -> cardDataValidation.transferCurrencyValidation(invalidCurrency, VALID_CARD));

        assertThat(exception).hasMessageThat().contains(String.format("К данной карте не привязан валютный счет в %s", transferCurrency));
    }
}