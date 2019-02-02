package com.codeosseum.miles.faultseeding.submission.evaluation;

import java.util.stream.StreamSupport;

import com.codeosseum.miles.code.execution.CodeExecutionException;
import com.codeosseum.miles.code.execution.listener.ListenerCodeExecutor;
import com.codeosseum.miles.code.execution.listener.listeners.ModuleExportsListener;
import com.codeosseum.miles.code.execution.listener.listeners.TimeoutListener;
import com.codeosseum.miles.faultseeding.task.Task;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.SourceSection;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeosseum.miles.faultseeding.submission.evaluation.EvaluationResult.Status.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public class DefaultSubmissionEvaluatorImpl implements SubmissionEvaluator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSubmissionEvaluatorImpl.class);

    private static final String JAVASCRIPT = "js";

    private final ListenerCodeExecutor executor;

    private final Source solutionSource;

    private final Source evaluatorSource;

    private Value erroneousSolution;

    private Value perfectSolution;

    private Value checkerFunction;

    public DefaultSubmissionEvaluatorImpl(final ListenerCodeExecutor executor, final Task task) {
        this.executor = executor;

        this.solutionSource = Source.create(JAVASCRIPT, task.getSolutionEntrypoint());
        this.evaluatorSource = Source.create(JAVASCRIPT, task.getEvaluatorEntrypoint());
    }

    @Override
    public EvaluationResult evaluate(final String submission) throws EvaluationException {
        // TODO: It'd be cheaper to run this only once. However, there are some security concerns
        // regarding that option.
        prepareEvaluation();

        final Value testFunction;
        try {
            testFunction = getExportedValue(Source.create(JAVASCRIPT, submission));
        } catch (final Exception e) {
            return EvaluationResult.submissionError(e);
        }

        final Object erroneousOutput = computeErroneousOutput(testFunction);
        final Object perfectOutput = computePerfectOutput(testFunction);
        final EvaluationResult.Status status = compareOutputInJava(erroneousOutput, perfectOutput);
        final String presentableActual = outputToPresentableString(erroneousOutput);
        final String presentableExpected = outputToPresentableString(perfectOutput);

        return EvaluationResult.evaluatedSubmission(status, presentableActual, presentableExpected);
    }

    private void prepareEvaluation() throws EvaluationException {
        try {
            this.erroneousSolution = getExportedValue(solutionSource);
            this.perfectSolution = getExportedValue(evaluatorSource);
            this.checkerFunction = makeCheckerFunction();
        } catch (final Exception e) {
            throw new EvaluationException("Could not prepare the solutions or the checker.", e);
        }
    }

    private Value getExportedValue(final Source source) throws CodeExecutionException  {
        final ModuleExportsListener exports = new ModuleExportsListener();
        final TimeoutListener timeout = TimeoutListener.withDefaultTimeout(1000);

        executor.executeWithListeners(source, asList(exports, timeout));

        return exports.get();
    }

    private Value makeCheckerFunction() throws CodeExecutionException {
        // TODO: Use deep equals - epoberezkin/fast-deep-equal
        final String checker = "(a, b) => a === b";

        return executor.execute(Source.create(JAVASCRIPT, checker));
    }

    private Object computeErroneousOutput(final Value testFunction) {
        try {
            return testFunction.execute(erroneousSolution);
        } catch (final Exception e) {
            return e;
        }
    }

    private Object computePerfectOutput(final Value testFunction) {
        try {
            return testFunction.execute(perfectSolution);
        } catch (final Exception e) {
            return e;
        }
    }

    private EvaluationResult.Status compareOutputs(final Object erroneousOutput, final Object perfectOutput) {
        if (isException(erroneousOutput) != isException(perfectOutput)) {
            return DIFFERENT_ACTUAL_AND_EXPECTED;
        } else {
            return compareOutputInJava(erroneousOutput, perfectOutput);
        }
    }

    private EvaluationResult.Status compareOutputInJava(final Object erroneousOutput, final Object perfectOutput)  {
        final boolean stringRepresentationIsTheSame = outputToComparableString(erroneousOutput)
                .equals(outputToComparableString(perfectOutput));

        return stringRepresentationIsTheSame ? SAME_ACTUAL_AND_EXPECTED : DIFFERENT_ACTUAL_AND_EXPECTED;
    }

    private String outputToComparableString(final Object output) {
        if (isPolyglotException(output)) {
            return polyglotExceptionToComparableString((PolyglotException) output);
        } else {
            return objectToComparableString(output);
        }
    }

    private String polyglotExceptionToComparableString(final PolyglotException exception) {
        return exception.getMessage();
    }

    private EvaluationResult.Status compareOutputInJavaScript(final Object erroneousOutput, final Object perfectOutput) throws EvaluationException {
        try {
            final boolean sameInJavaScript = checkerFunction.execute(erroneousOutput, perfectOutput).asBoolean();

            return sameInJavaScript ? SAME_ACTUAL_AND_EXPECTED : DIFFERENT_ACTUAL_AND_EXPECTED;
        } catch (final Exception e) {
            throw new EvaluationException("Could not compare outputs in JavaScript.", e);
        }
    }

    private String objectToComparableString(final Object output) {
        return output.toString();
    }

    private boolean isException(final Object object) {
        return object instanceof Exception;
    }

    private boolean isPolyglotException(final Object object) {
        return object instanceof PolyglotException;
    }

    private String outputToPresentableString(final Object output) {
        if (isPolyglotException(output)) {
            return polyglotExceptionToPresentableString((PolyglotException) output);
        } else {
            return objectToPresentableString(output);
        }
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

        return String.format("  %s (%s:%d:%d)", frame.getRootName(), location.getSource().getName(), location.getStartLine(), location.getStartColumn());
    }

    private String objectToPresentableString(final Object output) {
        return output.toString();
    }
}
