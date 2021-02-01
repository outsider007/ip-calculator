package ru.kuznetsov.ipcalculator.services;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class IPCalculator {
    private Map<Integer, Integer> validSeqOctet;
    private String ipv4Decimal;
    private String maskDecimal;
    private Map<String, String[]> networkCharacteristics;

    {
        validSeqOctet = new HashMap<>();
        validSeqOctet.put(0, 0);
        validSeqOctet.put(128, 1);
        validSeqOctet.put(192, 2);
        validSeqOctet.put(224, 3);
        validSeqOctet.put(240, 4);
        validSeqOctet.put(248, 5);
        validSeqOctet.put(252, 6);
        validSeqOctet.put(254, 7);
        validSeqOctet.put(255, 8);
    }

    public IPCalculator() {
        this.networkCharacteristics = new LinkedHashMap<>();
    }

    public Map<String, String[]> calculateNetworkCharacteristics(String ipv4Decimal, String maskDecimal) throws InvalidIPFormatException {
        if (isValidIpAddress(ipv4Decimal)) {
            this.ipv4Decimal = ipv4Decimal;
            this.maskDecimal = maskDecimal;

            networkCharacteristics.clear();
            calculateNetworkCharacteristics();
        } else {
            throw new InvalidIPFormatException("Неверный формат ip адреса!");
        }

        return getNetworkCharacteristics();
    }

    private boolean isValidIpAddress(String ipv4Decimal) {
        Pattern pattern = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");

        if (pattern.matcher(ipv4Decimal).find()) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, String[]> getNetworkCharacteristics() throws NullPointerException {
        if (ipv4Decimal != null && maskDecimal != null) {
            return networkCharacteristics;
        } else {
            System.out.println(ipv4Decimal);
            throw new NullPointerException("ipv4 or mask is empty!");
        }
    }

    private void calculateNetworkCharacteristics() {
        calculateIp();
        calculateMaskPrefix();
        calculateMask();
        calculateWildCard();
        calculateIpWebAddress();
        calculateBroadcastAddress();
        calculateFirstHostIpAddress();
        calculateLastHostIpAddress();
        calculateNumberAvailableAndWorkingAddresses();
    }

    private void calculateIp() {
        networkCharacteristics.put("ip адрес", new String[]{ipv4Decimal, calculateIpHexDec(), calculateIpBinary()});
    }

    private String calculateIpHexDec() {
        String[] ipDec = ipv4Decimal.split("\\.");
        StringBuilder ipHexDec = new StringBuilder();

        for (String num : ipDec) {
            String octetHexDec = Integer.toHexString(Integer.parseInt(num));

            if (octetHexDec.equals("0"))
                octetHexDec = "00";

            ipHexDec.append(octetHexDec).append(".");
        }

        return ipHexDec.deleteCharAt(ipHexDec.length() - 1).toString();
    }

    private String calculateIpBinary() {
        String[] ipDec = ipv4Decimal.split("\\.");
        StringBuilder ipBin = new StringBuilder();

        for (String num : ipDec) {
            StringBuilder octet = new StringBuilder(Integer.toBinaryString(Integer.parseInt(num)));

            int countNullByAppend = 8 - octet.length();

            octet.reverse();

            if (octet.length() != 8) {
                for (int j = 0; j < countNullByAppend; j++) {
                    octet.append("0");
                }
            }

            ipBin.append(octet.reverse()).append(".");
        }

        return ipBin.deleteCharAt(ipBin.length() - 1).toString();
    }

    private void calculateMaskPrefix() {
        networkCharacteristics.put("Префикс маски подсети", new String[]{maskDecimal.substring(0, maskDecimal.indexOf(' '))});
    }

    private void calculateMask() {
        networkCharacteristics.put("Маска подсети", new String[]{calculateMaskDecimal(), calculateMaskHexDec(), calculateMaskBinary()});
    }

    private String calculateMaskDecimal() {
        return maskDecimal.substring(maskDecimal.indexOf("-") + 1).trim();
    }

    private String calculateMaskHexDec() {
        String[] maskDec = calculateMaskDecimal().split("\\.");
        StringBuilder maskHexDec = new StringBuilder();

        for (String num : maskDec) {
            String octetHexDec = Integer.toHexString(Integer.parseInt(num));

            if (octetHexDec.equals("0"))
                octetHexDec = "00";

            maskHexDec.append(octetHexDec).append(".");
        }

        return maskHexDec.deleteCharAt(maskHexDec.length() - 1).toString();
    }

    private String calculateMaskBinary() {
        String[] maskDec = calculateMaskDecimal().split("\\.");
        StringBuilder maskBin = new StringBuilder();

        for (String num : maskDec) {
            StringBuilder octet = new StringBuilder(Integer.toBinaryString(Integer.parseInt(num)));

            int countNullByAppend = 8 - octet.length();

            octet.reverse();

            if (octet.length() != 8) {
                for (int j = 0; j < countNullByAppend; j++) {
                    octet.append("0");
                }
            }

            maskBin.append(octet.reverse()).append(".");
        }

        return maskBin.deleteCharAt(maskBin.length() - 1).toString();
    }

    private void calculateWildCard() {
        networkCharacteristics.put("Обратная маска", new String[]{calculateWildCardDec(), calculateWildCardHexDec(), calculateWildCardBin()});
    }

    private String calculateWildCardDec() {
        String[] maskDec = calculateMaskDecimal().split("\\.");
        StringBuilder wildCardDec = new StringBuilder();

        for (String num : maskDec) {
            wildCardDec.append((255 - Integer.parseInt(num))).append(".");
        }
        return wildCardDec.deleteCharAt(wildCardDec.length() - 1).toString();
    }

    private String calculateWildCardHexDec() {
        String[] wildCardDec = calculateWildCardDec().split("\\.");
        StringBuilder wildCardHexDec = new StringBuilder();

        for (String num : wildCardDec) {
            String octetHexDec = Integer.toHexString(Integer.parseInt(num));

            if (octetHexDec.equals("0"))
                octetHexDec = "00";

            wildCardHexDec.append(octetHexDec).append(".");
        }

        return wildCardHexDec.deleteCharAt(wildCardHexDec.length() - 1).toString();
    }

    private String calculateWildCardBin() {
        String[] wildCardDec = calculateWildCardDec().split("\\.");
        StringBuilder wildCardBin = new StringBuilder();

        for (String num : wildCardDec) {
            StringBuilder octet = new StringBuilder(Integer.toBinaryString(Integer.parseInt(num)));

            int countNullByAppend = 8 - octet.length();

            octet.reverse();

            if (octet.length() != 8) {
                for (int j = 0; j < countNullByAppend; j++) {
                    octet.append("0");
                }
            }

            wildCardBin.append(octet).append(".");
        }

        return wildCardBin.deleteCharAt(wildCardBin.length() - 1).toString();
    }

    private void calculateIpWebAddress() {
        networkCharacteristics.put("IP адрес сети", new String[]{calculateIpWebAddressDec(), calculateIpWebAddressHexDec(), calculateIpWebAddressBin()});
    }

    private String calculateIpWebAddressBin() {
        String[] ipBin = calculateIpBinary().split("\\.");
        String[] maskBin = calculateMaskBinary().split("\\.");
        StringBuilder ipAddressBin = new StringBuilder();

        for (int i = 0; i < ipBin.length; i++) {
            StringBuilder octet = new StringBuilder();

            for (int j = 0; j < ipBin[i].length(); j++) {
                int numIpBin = Integer.parseInt(String.valueOf(ipBin[i].charAt(j)));
                int numMaskBin = Integer.parseInt(String.valueOf(maskBin[i].charAt(j)));

                octet.append(numIpBin * numMaskBin);
            }

            int countNullByAppend = 8 - octet.length();

            octet.reverse();

            if (octet.length() != 8) {
                for (int j = 0; j < countNullByAppend; j++) {
                    octet.append("0");
                }
            }

            ipAddressBin.append(octet.reverse()).append(".");
        }

        return ipAddressBin.deleteCharAt(ipAddressBin.length() - 1).toString();
    }

    private String calculateIpWebAddressDec() {
        String[] ipWebAddressBin = calculateIpWebAddressBin().split("\\.");
        StringBuilder ipWebAddressDec = new StringBuilder();

        for (String num : ipWebAddressBin) {
            ipWebAddressDec.append(Integer.parseInt(num, 2)).append(".");
        }

        return ipWebAddressDec.deleteCharAt(ipWebAddressDec.length() - 1).toString();
    }

    private String calculateIpWebAddressHexDec() {
        String[] ipWebAddressDec = calculateIpWebAddressDec().split("\\.");
        StringBuilder ipWebAddressHexDec = new StringBuilder();

        for (String num : ipWebAddressDec) {
            String octetHexDec = Integer.toHexString(Integer.parseInt(num));

            if (octetHexDec.equals("0"))
                octetHexDec = "00";

            ipWebAddressHexDec.append(octetHexDec).append(".");
        }

        return ipWebAddressHexDec.deleteCharAt(ipWebAddressHexDec.length() - 1).toString();
    }

    private void calculateBroadcastAddress() {
        networkCharacteristics.put("Широковещательный адрес", new String[]{
                calculateBroadcastAddressDec(), calculateBroadcastAddressHexDec(), calculateBroadcastAddressBin()});
    }

    private String calculateBroadcastAddressDec() {
        String[] maskDec = calculateMaskDecimal().split("\\.");
        String[] ipDec = calculateIpWebAddressDec().split("\\.");
        StringBuilder broadcastDec = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            String maskOctet = maskDec[i];
            StringBuilder ipOctet = new StringBuilder(Integer.toBinaryString(Integer.parseInt(ipDec[i])));

            int countNullByAppend = 8 - ipOctet.length();

            ipOctet.reverse();

            if (ipOctet.length() != 8) {
                for (int j = 0; j < countNullByAppend; j++) {
                    ipOctet.append("0");
                }
            }

            int quantityBit = validSeqOctet.get(Integer.parseInt(maskOctet));

            for (int j = 0; j < ipOctet.length() - quantityBit; j++) {
                ipOctet.replace(j, j + 1, "1");
            }

            int broadcastOctet = Integer.parseInt(ipOctet.reverse().toString(), 2);

            if (broadcastOctet == 0) {
                broadcastDec.append(ipDec[i]).append(".");
            } else {
                broadcastDec.append(broadcastOctet).append(".");
            }
        }

        return broadcastDec.deleteCharAt(broadcastDec.length() - 1).toString();
    }

    private String calculateBroadcastAddressBin() {
        String[] broadcastAddressDec = calculateBroadcastAddressDec().split("\\.");
        StringBuilder broadcastAddressBin = new StringBuilder();

        for (String num : broadcastAddressDec) {
            StringBuilder octet = new StringBuilder(Integer.toBinaryString(Integer.parseInt(num)));
            int countNullByAppend = 8 - octet.length();

            octet.reverse();

            if (octet.length() != 8) {
                for (int j = 0; j < countNullByAppend; j++) {
                    octet.append("0");
                }
            }

            broadcastAddressBin.append(octet.reverse()).append(".");
        }

        return broadcastAddressBin.deleteCharAt(broadcastAddressBin.length() - 1).toString();
    }

    private String calculateBroadcastAddressHexDec() {
        String[] broadcastAddressDec = calculateBroadcastAddressDec().split("\\.");
        StringBuilder broadcastAddressHexDec = new StringBuilder();

        for (String num : broadcastAddressDec) {
            String octet = Integer.toHexString(Integer.parseInt(num));

            if (octet.equals("0"))
                octet = "00";

            broadcastAddressHexDec.append(octet).append(".");
        }

        return broadcastAddressHexDec.deleteCharAt(broadcastAddressHexDec.length() - 1).toString();
    }

    private void calculateFirstHostIpAddress() {
        networkCharacteristics.put("IP адрес первого хоста", new String[]{calculateFirstHostIpAddressDec(), calculateFirstHostIpAddressHexDex(), calculateFirstHostIpAddressBin()});
    }

    private String calculateFirstHostIpAddressDec() {
        String[] ipWebAddrss = calculateIpWebAddressDec().split("\\.");
        ipWebAddrss[ipWebAddrss.length - 1] = String.valueOf(Integer.parseInt(ipWebAddrss[ipWebAddrss.length - 1]) + 1);

        return String.join(".", ipWebAddrss);
    }

    private String calculateFirstHostIpAddressHexDex() {
        String[] firstIpDec = calculateFirstHostIpAddressDec().split("\\.");
        StringBuilder firstIpHexDec = new StringBuilder();

        for (String octet : firstIpDec) {
            String octetHexDec = Integer.toHexString(Integer.parseInt(octet));

            if (octetHexDec.equals("0"))
                octetHexDec = "00";

            firstIpHexDec.append(octetHexDec).append(".");
        }

        return firstIpHexDec.deleteCharAt(firstIpHexDec.length() - 1).toString();
    }

    private String calculateFirstHostIpAddressBin() {
        String[] firstIpDec = calculateFirstHostIpAddressDec().split("\\.");
        StringBuilder firstIpBin = new StringBuilder();

        for (String num : firstIpDec) {
            StringBuilder octet = new StringBuilder(Integer.toBinaryString(Integer.parseInt(num)));
            int countNullByAppend = 8 - octet.length();

            octet.reverse();

            if (octet.length() != 8) {
                for (int j = 0; j < countNullByAppend; j++) {
                    octet.append("0");
                }
            }

            firstIpBin.append(octet.reverse()).append(".");
        }

        return firstIpBin.deleteCharAt(firstIpBin.length() - 1).toString();
    }

    private void calculateLastHostIpAddress() {
        networkCharacteristics.put("IP адрес последнего хоста", new String[]{calculateLastHostIpAddressDec(), calculateLastHostIpAddressHexDex(), calculateLastHostIpAddressBin()});
    }

    private String calculateLastHostIpAddressDec() {
        String[] broadcastDec = calculateBroadcastAddressDec().split("\\.");
        broadcastDec[broadcastDec.length - 1] = String.valueOf(
                Math.max((Integer.parseInt(broadcastDec[broadcastDec.length - 1]) - 1), 0));

        return String.join(".", broadcastDec);
    }

    private String calculateLastHostIpAddressHexDex() {
        String[] lastIpDec = calculateLastHostIpAddressDec().split("\\.");
        StringBuilder lastIpHexDec = new StringBuilder();

        for (String octet : lastIpDec) {
            String octetHexDec = Integer.toHexString(Integer.parseInt(octet));

            if (octetHexDec.equals("0"))
                octetHexDec = "00";

            lastIpHexDec.append(octetHexDec).append(".");
        }

        return lastIpHexDec.deleteCharAt(lastIpHexDec.length() - 1).toString();
    }

    private String calculateLastHostIpAddressBin() {
        String[] lastIpDec = calculateLastHostIpAddressDec().split("\\.");
        StringBuilder lastIpBin = new StringBuilder();

        for (String num : lastIpDec) {
            StringBuilder octet = new StringBuilder(Integer.toBinaryString(Integer.parseInt(num)));
            int countNullByAppend = 8 - octet.length();

            octet.reverse();

            if (octet.length() != 8) {
                for (int j = 0; j < countNullByAppend; j++) {
                    octet.append("0");
                }
            }

            lastIpBin.append(octet.reverse()).append(".");
        }

        return lastIpBin.deleteCharAt(lastIpBin.length() - 1).toString();
    }

    private void calculateNumberAvailableAndWorkingAddresses() {
        networkCharacteristics.put("Количество доступных адресов", new String[]{String.valueOf(calculateNumberAvailableAddresses())});
        networkCharacteristics.put("Количество рабочих адресов для хостов", new String[]{String.valueOf(Math.max(calculateNumberAvailableAddresses() - 2, 0))});
    }

    private long calculateNumberAvailableAddresses() {
        long numberAvailable = 1;
        String[] mask = calculateMaskDecimal().split("\\.");

        for (String octet : mask) {
            numberAvailable *= 256 - Integer.parseInt(octet);
        }

        return numberAvailable;
    }
}
