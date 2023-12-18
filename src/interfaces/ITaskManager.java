package interfaces;

import model.classes.History;
import model.classes.Task;
import model.classes.User;
import model.enums.Category;

import java.util.List;

public interface ITaskManager {
    User getUser(String email) throws Exception;
    List<Task> getAllTasks();
    List<Task> getAllTasksByUser(User user);
    void registerUser(User user);
    boolean loginUser(String email, String password);
    public Task getTask(int id);
    void addTaskWithHistory(Task task, User owner, User createdBy, History history);
    public void deleteTask(Task task);
    public void updateTask(Task task, History history, int id, User owner, User createdBy);
    List<History> getAllHistory();

    // hassan
    List<Task> sortByPriority( String order);
    List<Task> sortByCretionDate( String order);

    List<Task> displayTasks(User owner);
    List<Task> filterTasksByCategory(List<Task> tasks, Category category);
}