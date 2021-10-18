package me.loeka;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        System.out.println("What conversion do you want to do?");

        var methods = Conversion.class.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            if (Modifier.isPrivate(methods[i].getModifiers()))
                continue;

            System.out.printf("[%d]: %s\n", i, conversionToConsoleOption(methods[i].getName()));
        }

        var scanner = new Scanner(System.in);
        var option = scanner.nextInt();

        var method = methods[option];
        var t = method.getParameterTypes()[0];

        System.out.println("Enter the value you want to convert:");
        String result = null;
        try {
            result = method.invoke(null, readInput(t)).toString();
        } catch (Exception e) {
            System.out.println("An unknown exception occured.");
            System.exit(1);
        }

        System.out.println(result);

    }

    private static Object readInput(Class<?> c) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                var line = br.readLine();
                if (c.getName() == int.class.getName() ||
                        c.getName() == Integer.class.getName()) {
                    return Integer.parseInt(line);
                }
                return line;
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input, try again...");
                continue;
            } catch (Exception e) {
                System.out.println("An unknown exception occured....");
                System.exit(1);
            }
        }
    }

    private static String conversionToConsoleOption(String s) {
        return String.join(" -> ", s.toLowerCase(Locale.ROOT).split("to"));
    }
}
