package ru.netology.moneytransferservice;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.moneytransferservice.domain.Amount;
import ru.netology.moneytransferservice.domain.Transfer;
import ru.netology.moneytransferservice.responce.OperationConfirmation;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;
    private static final int PORT = 9999;
    @Container
    private static final GenericContainer<?> backend = new GenericContainer<>("app")
            .withExposedPorts(8080)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(
                            new PortBinding(Ports.Binding.bindPort(PORT), new ExposedPort(8080)))));

    @Test
    void testTransferWhenValidThenReturnOperationId() {
        Transfer validTransfer = new Transfer("4474958586817833", "09/27",
                "498", "4875131749697170", new Amount(3_000.0, "RUR"));

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/transfer", validTransfer, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("{\"operationId\":\"1\"}", response);
    }

    @Test
    void testTransferWithIncorrectCardDataThenReturnError() {
        Transfer invalidCardTransfer = new Transfer("9999888877771111", "12/34",
                "012", "4875131749697170", new Amount(3_000.0, "RUR"));

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/transfer", invalidCardTransfer, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("Карты с номером [" +
                        "9999888877771111" +
                        "] не существует. Попробуйте еще раз.",
                response);
    }

    @Test
    void testConfirmationWhenCorrectIdThenReturnOperationId() {
        OperationConfirmation validConfirmation = new OperationConfirmation("0000", "1");

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/confirmOperation", validConfirmation, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("{\"operationId\":\"1\"}", response);
    }

    @Test
    void testConfirmationWhenInvalidCodeThenReturnError() {
        OperationConfirmation invalidCodeConfirmation = new OperationConfirmation("1111", "1");

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/confirmOperation", invalidCodeConfirmation, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("Неверный код подтверждения 1111!", response);
    }

    @Test
    void testConfirmationWhenInvalidOperationIdThenReturnError() {
        OperationConfirmation invalidOperationIdConfirmation = new OperationConfirmation("0000", "5");

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/confirmOperation", invalidOperationIdConfirmation, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("Транзакции с идентификатором [5] не существует", response);
    }
}
