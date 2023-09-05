package ru.Khalilov.banks.console.factories;

import lombok.NonNull;
import ru.Khalilov.banks.console.handlers.HandlerBase;

public interface HandlerFactory {
    @NonNull
    HandlerBase getHandler();

    @NonNull
    HandlerBase build();
}
