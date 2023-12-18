package repository;
import model.enums.Category;
import model.enums.Priority;
import model.enums.Status;

import config.DataBase;
import model.classes.History;
import model.classes.Task;
import model.classes.User;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBC {
    private static final String URl = "jdbc:mysql://root@localhost:3306/TitanTask";
    private static final String USERNAME = "root@localhost";
    private static final String PASSWORD = "";

    public void addUser(User user) throws Exception {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sql = "INSERT INTO user (name, email, password, role) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getRole());
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    public User loginUser(String email, String password) throws Exception {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    new ArrayList<>()
            );
            user.setId(resultSet.getInt("id"));
            return user;
        } else {
            return null;
        }
    }
    // method get user by id
    public static User getUserById(int id) throws SQLException {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sql = "SELECT * FROM user WHERE  id= ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    new ArrayList<>()
            );
            user.setId(resultSet.getInt("id"));
            return user;
        } else {
            return null;
        }
    }

    //method pass the email and return
    public User getUser(String email) throws Exception {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sql = "SELECT * FROM user WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    new ArrayList<>()
            );
            user.setId(resultSet.getInt("id"));
            return user;
        } else {
            return null;
        }
    }
    // method to return all task and do filter and sort
    public List<Task> getTasks(User user) throws Exception {
        List<Task> tasks = new ArrayList<>();

        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sql = "SELECT * FROM Task WHERE user_id = ?"+" AND softDelete = false";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,user.getId());

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        Category.valueOf(resultSet.getString("category")),
                        Priority.valueOf(resultSet.getString("priority")),
                        Status.valueOf(resultSet.getString("status")),
                        resultSet.getDate("createdDate").toLocalDate(),
                        user,
                        new ArrayList<>()
                );
                task.setId(resultSet.getInt("id"));
                tasks.add(task);
            }
        }

        return tasks;
    }

    // method to return all tasks
    public ArrayList<Task> getAllTasks() throws Exception {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sql = "SELECT task.*, user.name, user.email, user.password, user.role FROM task JOIN user ON task.user_id = user.id WHERE softDelete = false";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    new ArrayList<>()
            );
            user.setId(resultSet.getInt("user_id"));
            Task task = new Task(
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    Category.valueOf(resultSet.getString("category")),
                    Priority.valueOf(resultSet.getString("priority")),
                    Status.valueOf(resultSet.getString("status")),
                    resultSet.getDate("createdDate").toLocalDate(),
                    user,
                    new ArrayList<>()
            );
            task.setId(resultSet.getInt("id"));
            tasks.add(task);
        }
        return tasks;
    }

    // method to return all tasks by user
    public ArrayList<Task> getAllTasksByUser(User user) throws Exception {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sql = "SELECT * FROM task WHERE user_id = ?"+" AND softDelete = false";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            Task task = new Task(
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    Category.valueOf(resultSet.getString("category")),
                    Priority.valueOf(resultSet.getString("priority")),
                    Status.valueOf(resultSet.getString("status")),
                    resultSet.getDate("createdDate").toLocalDate(),
                    user,
                    new ArrayList<>()
            );
            task.setId(resultSet.getInt("id"));
            tasks.add(task);
        }
        return tasks;
    }
    // method to create task and history
    public void createTaskAndHistory(Task task, History history) throws Exception {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sqlTask = "INSERT INTO task (title, description, category, priority, status, createdDate, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statementTask = connection.prepareStatement(sqlTask, Statement.RETURN_GENERATED_KEYS);
        statementTask.setString(1, task.getTitle());
        statementTask.setString(2, task.getDescription());
        statementTask.setString(3, task.getCategory().toString());
        statementTask.setString(4, task.getPriority().toString());
        statementTask.setString(5, task.getStatus().toString());
        statementTask.setDate(6, java.sql.Date.valueOf(task.getCreatedDate()));
        statementTask.setInt(7, task.getUser().getId());
        statementTask.executeUpdate();
        ResultSet generatedKeys = statementTask.getGeneratedKeys();
        if (generatedKeys.next()) {
            task.setId(generatedKeys.getInt(1));
        }

        String sqlHistory = "INSERT INTO history (task_id, user_id, nameOperation, dateOperation) VALUES (?, ?, ?, ?)";
        PreparedStatement statementHistory = connection.prepareStatement(sqlHistory);
        statementHistory.setInt(1, task.getId());
        statementHistory.setInt(2, history.getUser().getId());
        statementHistory.setString(3, history.getNameOperation());
        statementHistory.setDate(4, java.sql.Date.valueOf(history.getDateOperation()));
        statementHistory.executeUpdate();

        statementTask.close();
        statementHistory.close();
        connection.close();
    }

    // method pass task id and return task
    public Task getTaskById(int id) throws Exception {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sql = "SELECT task.*, user.name, user.email, user.password, user.role FROM task JOIN user ON task.user_id = user.id WHERE task.id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    new ArrayList<>()
            );
            user.setId(resultSet.getInt("user_id"));
            Task task = new Task(
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    Category.valueOf(resultSet.getString("category")),
                    Priority.valueOf(resultSet.getString("priority")),
                    Status.valueOf(resultSet.getString("status")),
                    resultSet.getDate("createdDate").toLocalDate(),
                    user,
                    new ArrayList<>()
            );
            task.setId(resultSet.getInt("id"));
            return task;
        } else {
            return null;
        }
    }

    //to delete task by id and history
    public void deleteTaskAndHistory(Task task, History history) throws Exception {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);

        // Delete the task
        String sqlTask = "UPDATE task SET softDelete = true WHERE id = ?";
        PreparedStatement statementTask = connection.prepareStatement(sqlTask);
        statementTask.setInt(1, task.getId());
        statementTask.executeUpdate();

        // Insert the history record
        String sqlHistory = "INSERT INTO history (task_id, user_id, nameOperation, dateOperation) VALUES (?, ?, ?, ?)";
        PreparedStatement statementHistory = connection.prepareStatement(sqlHistory);
        statementHistory.setInt(1, task.getId());
        statementHistory.setInt(2, history.getUser().getId());
        statementHistory.setString(3, history.getNameOperation());
        statementHistory.setDate(4, java.sql.Date.valueOf(history.getDateOperation()));
        statementHistory.executeUpdate();

        statementTask.close();
        statementHistory.close();
        connection.close();
    }

    // method to update task and history
    public void updateTask(Task task, History history, int id) throws Exception{
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);

        String sqlUpdateTask = "UPDATE task SET title = ?, description = ?, category = ?, priority = ?, status = ?, createdDate = ?, user_id = ?  WHERE id = "+id;
        PreparedStatement seUpdateTask = connection.prepareStatement(sqlUpdateTask);
        seUpdateTask.setString(1, task.getTitle());
        seUpdateTask.setString(2, task.getDescription());
        seUpdateTask.setString(3, task.getCategory().toString());
        seUpdateTask.setString(4, task.getPriority().toString());
        seUpdateTask.setString(5, task.getStatus().toString());
        seUpdateTask.setDate(6, java.sql.Date.valueOf(task.getCreatedDate()));
        seUpdateTask.setInt(7, task.getUser().getId());
        seUpdateTask.executeUpdate();

        String sqlHistory = "INSERT INTO history (task_id, user_id, nameOperation, dateOperation) VALUES (?, ?, ?, ?)";
        PreparedStatement seAddHistory = connection.prepareStatement(sqlHistory);
        seAddHistory.setInt(1, id);
        seAddHistory.setInt(2, history.getUser().getId());
        seAddHistory.setString(3, history.getNameOperation());
        seAddHistory.setDate(4, java.sql.Date.valueOf(history.getDateOperation()));
        seAddHistory.executeUpdate();

        seAddHistory.close();
        seUpdateTask.close();
        connection.close();
    }

    // method to return all histories
    public ArrayList<History> getAllHistories() throws Exception {
        Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
        String sql = "SELECT history.*, task.title, task.description, task.category, task.priority, task.status, task.createdDate, user.name, user.email, user.password, user.role FROM history JOIN task ON history.task_id = task.id JOIN user ON history.user_id = user.id";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<History> histories = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    new ArrayList<>()
            );
            user.setId(resultSet.getInt("user_id"));
            Task task = new Task(
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    Category.valueOf(resultSet.getString("category")),
                    Priority.valueOf(resultSet.getString("priority")),
                    Status.valueOf(resultSet.getString("status")),
                    resultSet.getDate("createdDate").toLocalDate(),
                    user,
                    new ArrayList<>()
            );
            task.setId(resultSet.getInt("task_id"));
            History history = new History(
                    resultSet.getString("nameOperation"),
                    resultSet.getDate("dateOperation").toLocalDate(),
                    task,
                    user
            );
            history.setId(resultSet.getInt("id"));
            histories.add(history);
        }
        return histories;
    }

}