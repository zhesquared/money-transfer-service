package ru.netology.moneytransferservice.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.moneytransferservice.exception.InvalidCardDataException;
import ru.netology.moneytransferservice.exception.InvalidConfirmationDataException;
import ru.netology.moneytransferservice.dto.OperationReject;

@RestControllerAdvice
public class ExceptionsHandler {

    private static final Logger logger = LoggerFactory.getLogger("file-logger");

    @ExceptionHandler({InvalidConfirmationDataException.class, InvalidCardDataException.class})
    public ResponseEntity<OperationReject> handleInvalidDataException(Exception exception) {
        return exceptionResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<OperationReject> handleUnknownException(Exception exception) {
        return exceptionResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<OperationReject> exceptionResponse(Exception exception, HttpStatus httpStatus) {
        int exceptionId = OperationReject.idCounter.incrementAndGet();
        String exceptionMessage = exception.getMessage();

        logger.error(String.format("%d [%s]", exceptionId, exceptionMessage));

        return new ResponseEntity<>(new OperationReject(exceptionId, exceptionMessage), httpStatus);
    }
}