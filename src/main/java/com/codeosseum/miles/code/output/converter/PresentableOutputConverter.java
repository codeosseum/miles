package com.codeosseum.miles.code.output.converter;

import java.util.stream.StreamSupport;

import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.SourceSection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public class PresentableOutputConverter implements OutputConverter {
    private static final String STACK_FRAME_FORMAT = "  %s (%s:%d:%d)";

    @Override
    public String convertToString(final Object object) {
        if (isPolyglotException(requireNonNull(object))) {
            return polyglotExceptionToPresentableString((PolyglotException) object);
        } else {
            return objectToPresentableString(object);
        }
    }

    private boolean isPolyglotException(final Object object) {
        return object instanceof PolyglotException;
    }

    private String polyglotExceptionToPresentableString(final PolyglotException exception) {
        final String prefix = exception.getMessage() + "\n";

        return StreamSupport.stream(exception.getPolyglotStackTrace().spliterator(), false)
                .filter(PolyglotException.StackFrame::isGuestFrame)
                .map(this::stackFrameToString)
                .collect(joining("\n", prefix, ""));
    }

    private String stackFrameToString(final PolyglotException.StackFrame frame) {
        final SourceSection location = frame.getSourceLocation();

        return String.format(STACK_FRAME_FORMAT, frame.getRootName(), location.getSource().getName(), location.getStartLine(), location.getStartColumn());
    }

    private String objectToPresentableString(final Object output) {
        return output.toString();
    }
}
