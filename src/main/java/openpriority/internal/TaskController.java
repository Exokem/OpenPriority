package openpriority.internal;

import openpriority.tasks.SimpleTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TaskController
{
    public static final Set<SimpleTask> tasks = new HashSet<>();
    public static final Map<String, SimpleTask> ASSIGNED_TASKS = new HashMap<>();
    public static final Map<String, ?> TASK_CATEGORIES = new HashMap<>();
}
