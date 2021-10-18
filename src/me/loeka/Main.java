package me.loeka;

import me.loeka.errors.IncorrectInputException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;


public class Main {
    private static ArrayList<String> options;

    public static void main(String[] args) {
        var classes = Conversion.class.getClasses();

        options = new ArrayList<>();
        for (Class<?> c : classes) {
            var splitted = c
                    .getSimpleName()
                    .toLowerCase(Locale.ROOT)
                    .split("to");
            if (!options.contains(splitted[0]))
                options.add(splitted[0]);
            if (!options.contains(splitted[1]))
                options.add(splitted[1]);
        }

        System.out.println("From what type do you want to convert:");
        var from = selectOption();
        System.out.println("To what type do you want to convert:");
        var to = selectOption();

        System.out.println("Enter the value you want to convert:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Object result = null;
        while (true) {
            try {
                var input = br.readLine();
                if (from.equals(to)) {
                    result = input;
                } else if (from.equals("Binary") || to.equals("Binary")) {
                    result = invokeConversionInClass(from + "To" + to, input);
                } else {
                    var binary = invokeConversionInClass(from + "ToBinary", input);
                    result = invokeConversionInClass("BinaryTo" + to, binary.toString());
                }
                break;
            } catch (Exception e) {
                if (e.getCause() instanceof IncorrectInputException) {
                    System.out.println("Incorrect input data, please try again...");
                    continue;
                }

                endProgramWithErrorMessage();
            }
        }

        System.out.printf("The result is: %s", result);
    }

    private static Object invokeConversionInClass(String className, String input) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var convertClass = getClassFromConversion(className);
        var instance = convertClass.getConstructor().newInstance();
        Object converted = getMethodFromConversionClass(convertClass, "ParseInputToParameter")
                .invoke(instance, input);
        return getMethodFromConversionClass(convertClass, "Convert")
                .invoke(instance, converted);
    }

    private static String selectOption() {
        for (int i = 0; i < options.stream().count(); i++) {
            System.out.printf("[%d]: %s\n", i, capitalizeFirstLetter(options.get(i)));
        }

        var scanner = new Scanner(System.in);
        var selectedOptions = scanner.nextInt();
        return capitalizeFirstLetter(options.get(selectedOptions));
    }

    private static String capitalizeFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private static Class<?> getClassFromConversion(String name) throws ClassNotFoundException {
        for (Class<?> c : Conversion.class.getClasses())
            if (c.getSimpleName().equals(name)) return c;

        throw new ClassNotFoundException();
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
}
