package ru.netology.moneytransferservice;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.moneytransferservice.domain.Amount;
import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exceptions.InvalidCardDataException;
import ru.netology.moneytransferservice.repository.CardRepository;
import ru.netology.moneytransferservice.repository.TransferRepository;
import ru.netology.moneytransferservice.servise.TransferServiceImpl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TransferServiceTest {

    private final static Card VALID_CARD = new Card("5509190158858294", "05/26", "409", "Zuev", "Demid",
            new ConcurrentHashMap<>(Map.of("RUR", new Amount(2_154.0, "RUR"))));
    private final static Transfer VALID_TRANSFER = new Transfer(
            "5509190158858294", "05/26", "409", "5224855342102882",
            new Amount(500.0, "RUR"));
    @Mock
    private TransferRepository transferRepository;
    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private TransferServiceImpl transferService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(transferService, "verificationCode", "0000");
        ReflectionTestUtils.setField(transferService, "transferCommission", 0.01);
    }

    @Test
    public void testTransferWhenValidTransferThenReturnOperationId() throws InvalidCardDataException {
        Mockito.when(transferRepository.addTransfer(VALID_TRANSFER)).thenReturn(1L);
        Mockito.when(cardRepository.getCardByNumber(VALID_TRANSFER.getCardFromNumber()))
                .thenReturn(Optional.of(VALID_CARD));

        Long operationId = transferService.transfer(VALID_TRANSFER);

        Assertions.assertEquals(1, operationId);
    }

    @Test
    public void testTransferWhenInvalidCardTransferThenThrowEx() throws InvalidCardDataException {
        Transfer invalidCardTransfer = new Transfer(
                "9999888877776666", "01/25", "555", "5509190158858294",
                new Amount(50_000.0, "RUR"));
        String cardFromNumber = invalidCardTransfer.getCardFromNumber();

        Mockito.when(cardRepository.getCardByNumber(invalidCardTransfer.getCardFromNumber()))
                .thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.transfer(invalidCardTransfer));

        Truth.assertThat(ex).hasMessageThat().contains("Карты с номером [" + cardFromNumber + "] не существует. Попробуйте еще раз.");
    }
}