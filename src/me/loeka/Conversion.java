package me.loeka;

import me.loeka.errors.IncorrectInputException;

public class Conversion {
    public static class DecimalToBinary implements IConversion<Integer, String> {
        @Override
        public Integer ParseInputToParameter(String input) throws IncorrectInputException {
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IncorrectInputException();
            }
        }

        @Override
        public String Convert(Integer input) {
            double value = input;
            String returnValue = "";
            while (value >= 1) {
                returnValue += (int) value % 2;

                value = Math.floor(value / 2);
            }
            return Utils.ReverseString(returnValue);
        }

    }

    public static class BinaryToDecimal implements IConversion<String, Integer> {

        @Override
        public String ParseInputToParameter(String input) throws IncorrectInputException {
            for (char c : input.toCharArray())
                if (c != '0' && c != '1')
                    throw new IncorrectInputException();

            return input;
        }

        @Override
        public Integer Convert(String input) {
            int decimal = 0;
            for (int i = 0; i < input.length(); i++) {
                Integer binaryNumber = Integer.parseInt(Character.toString(Utils.ReverseString(input).charAt(i)));
                if (i == 0)
                    decimal += binaryNumber;
                else decimal += binaryNumber * Math.pow(2, i);
            }
            return decimal;
        }

    }
}
