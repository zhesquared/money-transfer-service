package ru.netology.moneytransferservice.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.moneytransferservice.responce.OperationReject;

@RestControllerAdvice
public class ExceptionsHandler {

    private static final Logger logger = LoggerFactory.getLogger("file-logger"); //инициализация логгера

    @ExceptionHandler(
            {InvalidConfirmationDataException.class,
                    InvalidCardDataException.class})
    public ResponseEntity<OperationReject> handleInvalidDataException(Exception exception) {
        return exceptionResponse(exception, HttpStatus.BAD_REQUEST); //обработчик ошибок
    }

    @ExceptionHandler
    public ResponseEntity<OperationReject> handleUnknownException(Exception exception) {
        return exceptionResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR); //обработка неизвестной ошибки
    }

    public ResponseEntity<OperationReject> exceptionResponse(Exception exception, HttpStatus httpStatus) {

        int exceptionId = OperationReject.idCounter.incrementAndGet(); //номер ошибки
        String exceptionMessage = exception.getMessage();

        logger.error(exceptionId + ": " + exceptionMessage);

        return new ResponseEntity<>(new OperationReject(exceptionId, exceptionMessage), httpStatus);
    }
}