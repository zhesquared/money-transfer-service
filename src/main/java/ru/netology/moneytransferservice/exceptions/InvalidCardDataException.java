package ru.netology.moneytransferservice.exceptions;

public class InvalidCardDataException extends Exception {

    public InvalidCardDataException(String message) {
        super(message);
    }
}