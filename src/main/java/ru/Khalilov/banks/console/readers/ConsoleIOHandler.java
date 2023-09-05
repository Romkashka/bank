package ru.Khalilov.banks.console.readers;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

public interface ConsoleIOHandler {
    @NonNull String readInput();
    @NonNull String readOptions(@NonNull List<String> options);
    int readInt();
    BigDecimal readBigDecimal();
    boolean readBooleanAnswer();
    boolean readBooleanAnswer(String question);
    void println(String output);

    <T> T chooseFromList(List<T> list);
}
