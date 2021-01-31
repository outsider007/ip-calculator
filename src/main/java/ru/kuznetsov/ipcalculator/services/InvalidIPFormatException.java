package ru.kuznetsov.ipcalculator.services;

public class InvalidIPFormatException extends Exception{
    public InvalidIPFormatException(String message) {
        super(message);
    }
}
