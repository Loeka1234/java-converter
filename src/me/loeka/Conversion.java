package me.loeka;

public class Conversion {
    public static String DecimalToBinary(int decimal) {
        double value = decimal;
        String returnValue = "";
        while (value >= 1) {
            returnValue += (int) value % 2;

            value = Math.floor(value / 2);
        }
        return reverseString(returnValue);
    }

    public static int BinaryToDecimal(String s) {
        int decimal = 0;
        for (int i = 0; i < s.length(); i++) {
            Integer binaryNumber = Integer.parseInt(Character.toString(reverseString(s).charAt(i)));
            if (i == 0)
                decimal += binaryNumber;
            else decimal += binaryNumber * Math.pow(2, i);
        }
        return decimal;
    }

    private static String reverseString(String s) {
        return new StringBuilder(s).reverse().toString();
    }
}
