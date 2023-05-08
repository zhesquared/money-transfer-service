package ru.netology.moneytransferservice.exceptions;

public class InvalidConfirmationDataException extends Exception {

    public InvalidConfirmationDataException(String message) {
        super(message);
    }
}