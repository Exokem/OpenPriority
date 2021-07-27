package openpriority.internal;

import openpriority.task.Task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TaskController
{
    public static final Set<Task> tasks = new HashSet<>();
    public static final Map<String, Task> ASSIGNED_TASKS = new HashMap<>();
    public static final Map<String, ?> TASK_CATEGORIES = new HashMap<>();
}
