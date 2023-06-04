package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.moneytransferservice.domain.Amount;
import ru.netology.moneytransferservice.domain.Card;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.exception.InvalidCardDataException;
import ru.netology.moneytransferservice.repository.CardRepository;
import ru.netology.moneytransferservice.repository.TransferRepository;
import ru.netology.moneytransferservice.servise.TransferServiceImpl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
    public void testTransferWhenValidTransferThenReturnOperationId() {
        when(transferRepository.addTransfer(VALID_TRANSFER)).thenReturn(1L);
        when(cardRepository.getCardByNumber(VALID_TRANSFER.cardFromNumber()))
                .thenReturn(Optional.of(VALID_CARD));

        Long operationId = Long.valueOf(transferService.transfer(VALID_TRANSFER).getOperationId());
        Long idExp = 1L;

        assertEquals(idExp, operationId);
    }

    @Test
    public void testTransferWhenInvalidCardTransferThenThrowEx() {
        Transfer invalidCardTransfer = new Transfer(
                "9999888877776666", "01/25", "555", "5509190158858294",
                new Amount(50_000.0, "RUR"));
        String cardFromNumber = invalidCardTransfer.cardFromNumber();

        when(cardRepository.getCardByNumber(invalidCardTransfer.cardFromNumber()))
                .thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.transfer(invalidCardTransfer));

        assertThat(ex).hasMessageThat().contains("Карты с номером [" + cardFromNumber + "] не существует. Попробуйте еще раз.");
    }
}