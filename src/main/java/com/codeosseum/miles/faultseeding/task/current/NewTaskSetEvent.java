package com.codeosseum.miles.faultseeding.task.current;

import com.codeosseum.miles.faultseeding.task.Task;
import lombok.Value;

@Value
public class NewTaskSetEvent {
    private final Task task;
}
