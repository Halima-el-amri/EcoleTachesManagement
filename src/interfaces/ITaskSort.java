package interfaces;

import model.classes.Task;

import java.util.List;

@FunctionalInterface
public interface ITaskSort {
    void sortTasks(List<Task> tasks, String sortType);
}
