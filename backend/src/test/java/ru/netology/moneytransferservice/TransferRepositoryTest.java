package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.moneytransferservice.domain.Amount;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.repository.TransferRepositoryImpl;
import ru.netology.moneytransferservice.responce.OperationConfirmation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
public class TransferRepositoryTest {

    @InjectMocks
    private TransferRepositoryImpl transferRepository;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(transferRepository, "transfers",
                new ConcurrentHashMap<>(Map.of(
                        1L, new Transfer(
                                "4875131749697170", "12/23", "498", "4474958586817833",
                                new Amount(5_000.0, "RUR")))));
        ReflectionTestUtils.setField(transferRepository, "id", new AtomicLong(1));
    }

    @Test
    public void testTransferWhenAddTransferWhenReturnCorrectId() {
        Transfer newTransfer = new Transfer("4875131749697170", "12/23", "498", "4474958586817833",
                new Amount(5_000.0, "RUR"));

        Long id = transferRepository.addTransfer(newTransfer);
        Assertions.assertEquals(2, id);
    }

    @Test
    public void testConfirmOperationWhenValidOperationIdThenReturnTrue() {
        OperationConfirmation confirmation = new OperationConfirmation("0000", "1");

        Assertions.assertTrue(transferRepository.confirmOperation(confirmation));
    }

    @Test
    public void testConfirmOperationWhenInvalidOperationIdThenReturnFalse() {
        OperationConfirmation confirmation = new OperationConfirmation("0000", "99");

        Assertions.assertFalse(transferRepository.confirmOperation(confirmation));
    }
}