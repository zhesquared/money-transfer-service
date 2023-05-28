package ru.netology.moneytransferservice;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.moneytransferservice.domain.Amount;
import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exceptions.InvalidCardDataException;
import ru.netology.moneytransferservice.servise.TransferServiceImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TransferServiceCardValidationTest {

    @InjectMocks
    private TransferServiceImpl transferService;
    private final static Card VALID_CARD = new Card("4875131749697170", "12/23", "498", "Shcherbakov", "Mikhail",
            new ConcurrentHashMap<>(Map.of("RUR", new Amount(15_220.0, "RUR"))));

    @Test
    public void testValidationWhenIncorrectTillDateWhenThrowException() { //testValidateCardDataWhenInvalidTillDateThenThrowEx
        Transfer incorrectTillDate = new Transfer(
                "4875131749697170", "01/23", "498", "4474958586817833",
                new Amount(400.0, "RUR"));

        Exception exception = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.cardDataValidation(incorrectTillDate, VALID_CARD));

        Truth.assertThat(exception).hasMessageThat().contains("Неккоректные данные карты: срок действия или код cvv");
    }

    @Test
    public void testValidationWhenIncorrectCVVWhenThrowException() {
        Transfer invalidCVV = new Transfer(
                "4875131749697170", "12/230", "999", "4474958586817833",
                new Amount(400.0, "RUR"));

        Exception exception = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.cardDataValidation(invalidCVV, VALID_CARD));

        Truth.assertThat(exception).hasMessageThat().contains("Неккоректные данные карты: срок действия или код cvv");
    }

    @Test
    public void testValidationWhenIncorrectCurrencyWhenThrowException() {
        Transfer invalidCurrency = new Transfer(
                "4875131749697170", "01/23", "498", "4474958586817833",
                new Amount(20.0, "USD"));
        String transferCurrency = invalidCurrency.getAmount().getCurrency();

        Exception exception = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.cardDataValidation(invalidCurrency, VALID_CARD));

        Truth.assertThat(exception).hasMessageThat().contains("К данной карте не привязан валютный счет в  " + transferCurrency);
    }

    @Test
    public void testValidationWhenInsufficientAmountWhenThrowException() {
        ReflectionTestUtils.setField(transferService, "transferCommission", 0.01);
        Transfer invalidAmountTransfer = new Transfer(
                "5561903268892226", "08/23", "207", "4474958586817833",
                new Amount(150_000.0, "RUR"));

        Exception ex = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.cardDataValidation(invalidAmountTransfer, VALID_CARD));

        Truth.assertThat(ex).hasMessageThat().contains("Недостаточно средств. Баланс 205.0, небходимо для перевода 151500.0");
    }
}