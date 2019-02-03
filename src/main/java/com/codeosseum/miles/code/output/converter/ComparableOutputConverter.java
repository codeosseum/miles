package com.codeosseum.miles.code.output.converter;

import org.graalvm.polyglot.PolyglotException;

import static java.util.Objects.requireNonNull;

public class ComparableOutputConverter implements OutputConverter {
    @Override
    public String convertToString(final Object object) {
        if (isPolyglotException(requireNonNull(object))) {
            return polyglotExceptionToComparableString((PolyglotException) object);
        } else {
            return objectToComparableString(object);
        }
    }

    private boolean isPolyglotException(final Object object) {
        return object instanceof PolyglotException;
    }

    private String objectToComparableString(final Object output) {
        return output.toString();
    }

    private String polyglotExceptionToComparableString(final PolyglotException exception) {
        return exception.getMessage();
    }
}
