package ru.Khalilov.banks.console.readers;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SystemIOHandler implements ConsoleIOHandler {
    private Scanner scanner;

    public SystemIOHandler() {
        scanner = new Scanner(System.in);
    }

    public @NonNull String readInput() {
        String result = scanner.nextLine();
        while(result == null || result.length() == 0) {
            System.out.println("Input wasn't read properly or it's empty. Please, try again");
            result = scanner.nextLine();
        }

        return result;
    }

    public @NonNull String readOptions(@NonNull List<String> options) {
        String allOptions = String.join(", ", options);

        System.out.println("Possible options:");
        System.out.println(allOptions);

//        readInput();
        List<String> answers = List.of(readInput().split(" "));
        while (answers.size() != 1 || !options.contains(answers.get(0))) {
            System.out.println("Only one of this words can be accepted: " + allOptions);
            answers = List.of(readInput().split(" "));
        }

        return answers.get(0);
    }

    public int readInt() {
        while (true) {
            try {
                int result = scanner.nextInt();;
                scanner.nextLine();
                return result;
            }
            catch (InputMismatchException e) {
                System.out.println("Your input can't be parsed as int. Please, try again!");
            }
        }
    }

    public BigDecimal readBigDecimal() {
        while (true) {
            try {
                BigDecimal result = scanner.nextBigDecimal();
                scanner.nextLine();
                return result;
            }
            catch (InputMismatchException e) {
                System.out.println("Your input can't be parsed as BigDecimal. Please, try again!");
                scanner.next();
            }
        }
    }

    public boolean readBooleanAnswer() {
        return readOptions(List.of("yes", "no")).equals("yes");
    }

    public <T> @NonNull T chooseFromList(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            println(i + ":\n" + list.get(i));
        }
        println("Which will you chose?");
        int index = readInt();
        while (index < 0 || index >= list.size()) {
            println("Index out of bounds! Try again!");
            index = readInt();
        }

        return list.get(index);
    }

    @Override
    public boolean readBooleanAnswer(String question) {
        println(question);
        return readBooleanAnswer();
    }

    @Override
    public void println(String output) {
        System.out.println(output);
    }
}
