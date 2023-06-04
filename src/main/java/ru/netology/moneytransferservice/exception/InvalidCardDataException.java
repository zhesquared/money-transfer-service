package ru.netology.moneytransferservice.exception;

public class InvalidCardDataException extends Exception {

    public InvalidCardDataException(String message) {
        super(message);
    }
}