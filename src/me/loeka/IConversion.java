package me.loeka;

import me.loeka.errors.IncorrectInputException;

public interface IConversion<T, U> {
    T ParseInputToParameter(String input) throws IncorrectInputException;

    U Convert(T input) throws Exception;
}
