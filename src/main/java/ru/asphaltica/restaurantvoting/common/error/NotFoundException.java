package ru.asphaltica.restaurantvoting.common.error;

import static ru.asphaltica.restaurantvoting.common.error.ErrorType.NOT_FOUND;

public class NotFoundException extends AppException {
    public NotFoundException(String msg) {
        super(msg, NOT_FOUND);
    }
}
