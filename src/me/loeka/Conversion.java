package me.loeka;

import me.loeka.errors.IncorrectInputException;

import java.util.Map;

public class Conversion {
    private static final Map<Integer, Character> hexValues = Map.of(
            10, 'A',
            11, 'B',
            12, 'C',
            13, 'D',
            14, 'E',
            15, 'F'
    );

    private static Integer getKeyByValueFromHexValues(Character character) {
        for (var pair : hexValues.entrySet()) {
            if (pair.getValue() == character) return pair.getKey();
        }
        return null;
    }

    public static String ParseBinaryString(String input) throws IncorrectInputException {
        for (char c : input.toCharArray())
            if (c != '0' && c != '1')
                throw new IncorrectInputException();

        return input;
    }

    public static char MapNumberToHex(int number) throws Exception {
        var value = hexValues.get(number);
        if (value == null) throw new Exception("Incorrect number"); // TODO: Better Exception
        return value;
    }

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
            return Conversion.ParseBinaryString(input);
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

    public static class BinaryToHexadecimal implements IConversion<String, String> {

        @Override
        public String ParseInputToParameter(String input) throws IncorrectInputException {
            return Conversion.ParseBinaryString(input);
        }

        @Override
        public String Convert(String input) throws Exception {
            String reversedInput = Utils.ReverseString(input);
            double iterations = Math.ceil((double) input.length() / 4);
            String output = "";
            for (int i = 0; i < iterations; i++) {
                String part;
                if (i + 1 == iterations)
                    part = reversedInput.substring(i * 4);
                else
                    part = reversedInput.substring(i * 4, i * 4 + 4);

                part = Utils.ReverseString(part);
                var binaryToDecimal = new BinaryToDecimal();
                var decimal = binaryToDecimal.Convert(part);
                if (decimal >= 10 && decimal <= 15) output += Conversion.MapNumberToHex(decimal);
                else output += decimal;
            }
            return Utils.ReverseString(output);
        }
    }

    public static class HexadecimalToBinary implements IConversion<String, String> {

        @Override
        public String ParseInputToParameter(String input) throws IncorrectInputException {
            for (char c : input.toCharArray()) {
                if ((!Character.isDigit(c) && !hexValues.containsValue(c)))
                    throw new IncorrectInputException();
            }
            return input;
        }

        @Override
        public String Convert(String input) {
            var output = "";
            for (char c : input.toCharArray()) {
                int decimal;
                if (!Character.isDigit(c))
                    decimal = getKeyByValueFromHexValues(c);
                else decimal = Integer.parseInt(Character.toString(c));
                output += new DecimalToBinary().Convert(decimal);
            }
            return output;
        }
    }
}
