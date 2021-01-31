package ru.kuznetsov.ipcalculator.services;

import java.util.Locale;

public class Row {
    private int id;
    private String key;
    private String decimalValue;
    private String binaryValue;
    private String hexDecimalValue;
    private static int counter;

    public Row(String key, String[] values) {
        id = counter++;
        this.key = key;

        for (int i = 0; i < values.length; i++) {
            switch (i) {
                case 0:
                    this.decimalValue = values[i];
                    break;
                case 1:
                    this.hexDecimalValue = values[i].toUpperCase(Locale.ROOT);
                    break;
                case 2:
                    this.binaryValue = values[i];
                    break;
                default:
                    break;
            }
        }

    }

    public Row(String key, String decValue) {
        this.id = counter++;
        this.key = key;
        this.decimalValue = decValue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return key;
    }

    public void setName(String name) {
        this.key = name;
    }

    public String getDecimalValue() {
        return decimalValue;
    }

    public void setDecimalValue(String decimalValue) {
        this.decimalValue = decimalValue;
    }

    public String getBinaryValue() {
        return binaryValue;
    }

    public void setBinaryValue(String binaryValue) {
        this.binaryValue = binaryValue;
    }

    public String getHexDecimalValue() {
        return hexDecimalValue;
    }

    public void setHexDecimalValue(String hexDecimalValue) {
        this.hexDecimalValue = hexDecimalValue;
    }
}
