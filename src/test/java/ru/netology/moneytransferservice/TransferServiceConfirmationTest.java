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
import ru.netology.moneytransferservice.dto.OperationConfirmation;
import ru.netology.moneytransferservice.exception.InvalidConfirmationDataException;
import ru.netology.moneytransferservice.repository.CardRepository;
import ru.netology.moneytransferservice.repository.TransferRepository;
import ru.netology.moneytransferservice.servise.TransferServiceImpl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TransferServiceConfirmationTest {

    private final static Card VALID_CARD = new Card("4935478279404040", "08/24", "695", "Evdokimov", "Anton",
            new ConcurrentHashMap<>(Map.of("RUR", new Amount(100_000.0, "RUR"))));
    private final static Transfer VALID_TRANSFER = new Transfer(
            "4935478279404040", "08/24", "695", "4557530545127271",
            new Amount(500.0, "RUR"));
    @Mock
    private TransferRepository transferRepository;
    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private TransferServiceImpl transferService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(transferService, "verificationCode",
                "0000");
        ReflectionTestUtils.setField(transferService, "transferCommission",
                0.01);
    }

    @Test
    public void testConfirmWhenValidOperationThenReturnTrue() throws InvalidConfirmationDataException {
        OperationConfirmation validConfirmation = new OperationConfirmation("0000", "1");

        when(transferRepository.confirmOperation(validConfirmation)).thenReturn(true);
        when(transferRepository.getTransferById(validConfirmation.getOperationId()))
                .thenReturn(VALID_TRANSFER);
        when(cardRepository.getCardByNumber(VALID_TRANSFER.cardFromNumber()))
                .thenReturn(Optional.of(VALID_CARD));

        boolean result = transferService.transferConfirmation(validConfirmation);
        assertTrue(result);
    }

    @Test
    public void testConfirmWhenIncorrectCodeConfirmationThenThrowException() {
        OperationConfirmation invalidCodeConfirmation = new OperationConfirmation("1111", "1");

        Exception ex = Assertions.assertThrows(InvalidConfirmationDataException.class,
                () -> transferService.transferConfirmation(invalidCodeConfirmation));

        assertThat(ex).hasMessageThat().contains("Неверный код подтверждения [1111]!");
    }

    @Test
    public void testConfirmWhenInvalidOperationConfirmationThenThrowEx() {
        OperationConfirmation invalidOperationConfirmation = new OperationConfirmation("0000", "99");

        when(transferRepository.confirmOperation(invalidOperationConfirmation)).thenReturn(false);

        Exception ex = Assertions.assertThrows(InvalidConfirmationDataException.class,
                () -> transferService.transferConfirmation(invalidOperationConfirmation));

        assertThat(ex).hasMessageThat().contains("Транзакции с идентификатором [99] не существует");
    }
}
