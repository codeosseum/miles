package com.codeosseum.miles.faultseeding.submission.evaluation;

import com.codeosseum.miles.code.execution.CodeExecutionException;
import com.codeosseum.miles.code.execution.listener.ListenerCodeExecutor;
import com.codeosseum.miles.code.execution.listener.listeners.ModuleExportsListener;
import com.codeosseum.miles.code.execution.listener.listeners.TimeoutListener;
import com.codeosseum.miles.faultseeding.task.Task;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import static java.util.Arrays.asList;

public class DefaultSubmissionEvaluatorImpl implements SubmissionEvaluator {
    private static final String JAVASCRIPT = "js";

    private final ListenerCodeExecutor executor;

    private final Source solutionSource;

    private final Source evaluatorSource;

    public DefaultSubmissionEvaluatorImpl(final ListenerCodeExecutor executor, final Task task) {
        this.executor = executor;

        this.solutionSource = Source.create(JAVASCRIPT, task.getSolutionEntrypoint());
        this.evaluatorSource = Source.create(JAVASCRIPT, task.getEvaluatorEntrypoint());
    }

    @Override
    public EvaluationResult evaluate(final String submission) {
        final Value erroneousSolution;
        final Value perfectSolution;
        final Value checkerFunction;
        try {
            erroneousSolution = getExportedValue(solutionSource);
            perfectSolution = getExportedValue(evaluatorSource);
            checkerFunction = makeCheckerFunction();
        } catch (final Exception e) {
            // Throw an exception, because this must not happen.
            e.printStackTrace();

            return null;
        }

        final Value testFunction;
        try {
            testFunction = getExportedValue(Source.create(JAVASCRIPT, submission));
        } catch (final Exception e) {
            // Return result with test function error
            e.printStackTrace();

            return null;
        }

        final Value erroneousOutput;
        try {
            erroneousOutput = testFunction.execute(erroneousSolution);
        } catch (final Exception e) {
            // The erroneous output threw.
            e.printStackTrace();

            return null;
        }

        final Value perfectOutput;
        try {
            perfectOutput = testFunction.execute(perfectSolution);
        } catch (final Exception e) {
            // The perfect output threw.
            e.printStackTrace();

            return null;
        }

        final boolean isOutputsEqual;
        try {
            isOutputsEqual = checkerFunction.execute(erroneousOutput, perfectOutput).asBoolean();
        } catch (final Exception e) {
            // Throw an exception, because this must not happen.
            e.printStackTrace();

            return null;
        }

        // isOutputsEqual == true
        // Return GOOD
        // isOutputsEqual == false
        // Return BAD

        return null;
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
}
