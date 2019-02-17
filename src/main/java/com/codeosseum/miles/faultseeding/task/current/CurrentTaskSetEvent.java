package com.codeosseum.miles.faultseeding.task.current;

import com.codeosseum.miles.faultseeding.task.Task;
import lombok.Value;

@Value
public class CurrentTaskSetEvent {
    private final Task task;
}
