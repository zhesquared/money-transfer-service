package ru.netology.moneytransferservice.exception;

public class InvalidConfirmationDataException extends Exception {

    public InvalidConfirmationDataException(String message) {
        super(message);
    }
}