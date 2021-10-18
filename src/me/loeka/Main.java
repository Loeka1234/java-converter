package me.loeka;

import me.loeka.errors.IncorrectInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        System.out.println("What conversion do you want to do?");

        var classes = Conversion.class.getClasses();

        for (int i = 0; i < classes.length; i++) {
            System.out.printf("[%d]: %s\n", i, conversionToConsoleOption(classes[i].getSimpleName()));
        }

        var scanner = new Scanner(System.in);
        var option = scanner.nextInt();

        var selectedConversion = classes[option];
        if (selectedConversion.getInterfaces()[0].getSimpleName() != IConversion.class.getSimpleName()) {
            System.out.println("Oops, something went wrong...");
            System.exit(1);
        }

        Object instantiatedConversionClass = null;
        try {
            instantiatedConversionClass = selectedConversion.getConstructor().newInstance();
        } catch (Exception e) {
            endProgramWithErrorMessage();
        }

        System.out.println("Enter the value you want to convert:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Object output = null;
        while (true) {
            try {
                Object converted = getMethodFromConversionClass(selectedConversion, "ParseInputToParameter")
                        .invoke(instantiatedConversionClass, br.readLine());
                output = getMethodFromConversionClass(selectedConversion, "Convert")
                        .invoke(instantiatedConversionClass, converted);
                break;
            } catch (Exception e) {
                if (e instanceof IOException || e.getCause() instanceof IncorrectInputException) {
                    System.out.println("Something went wrong while reading your input, please try again...");
                    continue;
                }
                endProgramWithErrorMessage();
            }
        }

        System.out.printf("Converted value: %s", output);
    }

    private static Method getMethodFromConversionClass(Class<?> conversion, String methodName) throws NoSuchMethodException {
        for (var method : conversion.getMethods()) {
            if (method.getName() == methodName) return method;
        }
        throw new NoSuchMethodException();
    }

    private static void endProgramWithErrorMessage() {
        System.out.println("Oops, something went wrong...");
        System.exit(1);
    }

    private static String conversionToConsoleOption(String s) {
        return String.join(" -> ", s.toLowerCase(Locale.ROOT).split("to"));
    }
}
