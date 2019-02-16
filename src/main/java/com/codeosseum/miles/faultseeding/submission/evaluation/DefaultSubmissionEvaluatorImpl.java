package com.codeosseum.miles.faultseeding.submission.evaluation;

import com.codeosseum.miles.code.execution.CodeExecutionException;
import com.codeosseum.miles.code.execution.listener.ListenerCodeExecutor;
import com.codeosseum.miles.code.execution.listener.listeners.ModuleExportsListener;
import com.codeosseum.miles.code.execution.listener.listeners.TimeoutListener;
import com.codeosseum.miles.code.output.comparator.OutputComparator;
import com.codeosseum.miles.code.output.converter.OutputConverter;
import com.codeosseum.miles.faultseeding.task.Task;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeosseum.miles.faultseeding.submission.evaluation.EvaluationResult.Status.*;
import static java.util.Arrays.asList;

public class DefaultSubmissionEvaluatorImpl implements SubmissionEvaluator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSubmissionEvaluatorImpl.class);

    private static final String JAVASCRIPT = "js";

    private final ListenerCodeExecutor executor;

    private final OutputComparator outputComparator;

    private final OutputConverter outputConverter;

    private final Source solutionSource;

    private final Source evaluatorSource;

    private final Task task;

    private Value erroneousSolution;

    private Value perfectSolution;

    public DefaultSubmissionEvaluatorImpl(final ListenerCodeExecutor executor, final OutputComparator outputComparator,
                                          final OutputConverter outputConverter, final Task task) {
        this.executor = executor;
        this.outputComparator = outputComparator;
        this.outputConverter = outputConverter;
        this.task = task;

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
            return EvaluationResult.submissionError(outputConverter.convertToString(e));
        }

        // TODO: Investigate (and if necessary implement) Promise support.
        final Object erroneousOutput = computeErroneousOutput(testFunction);
        final Object perfectOutput = computePerfectOutput(testFunction);

        LOGGER.debug("Erroneous output is {}", erroneousOutput);
        LOGGER.debug("Perfect output is {}", perfectOutput);

        final EvaluationResult.Status status = compareOutputs(erroneousOutput, perfectOutput);

        return EvaluationResult.evaluatedSubmission(status,
                outputConverter.convertToString(erroneousOutput),
                outputConverter.convertToString(perfectOutput));
    }

    @Override
    public Task getEvaluatedTask() {
        return this.task;
    }

    private void prepareEvaluation() throws EvaluationException {
        try {
            this.erroneousSolution = getExportedValue(solutionSource);
            this.perfectSolution = getExportedValue(evaluatorSource);
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

    private EvaluationResult.Status compareOutputs(final Object erroneousOutput, final Object perfectOutput) throws EvaluationException {
        try {
            if (isException(erroneousOutput) != isException(perfectOutput)) {
                return DIFFERENT_ACTUAL_AND_EXPECTED;
            } else {
                final boolean sameOutputs = outputComparator.compare(executor, erroneousOutput, perfectOutput);

                return sameOutputs ? SAME_ACTUAL_AND_EXPECTED : DIFFERENT_ACTUAL_AND_EXPECTED;
            }
        } catch (final Exception e) {
            throw new EvaluationException("Could not compare outputs.", e);
        }
    }


    private boolean isException(final Object object) {
        return object instanceof Exception;
    }


}
