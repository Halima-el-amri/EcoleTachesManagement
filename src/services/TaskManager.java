package services;

import config.DataBase;
import interfaces.ITaskSort;
import model.classes.History;
import model.classes.Task;
import model.classes.User;
import model.enums.Category;
import model.enums.Priority;
import model.enums.Status;
import repository.JDBC;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager implements interfaces.ITaskManager {
    List<Task> tasks = new ArrayList<>();
    JDBC jdbc = new JDBC();
    public static final String line = "----------------------------------------------------------------------------------------";



    @Override
    public void registerUser(User user) {
        try {
            // check if the user already exists using the getUser method
            User existingUser = jdbc.getUser(user.getEmail());
            if (existingUser != null) {
                System.out.println(line);
                System.out.println("Utilisateur avec cet email existe déjà");
                System.out.println(line);
                return;
            }
            jdbc.addUser(user);
            System.out.println(line);
            System.out.println("Votre compte a été créé avec succès");
            System.out.println(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUser(String email) throws Exception {
        return jdbc.getUser(email);
    }

    // method to get all tasks
    @Override
    public List<Task> getAllTasks() {
        try {
            return jdbc.getAllTasks();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // method to get all tasks by user
    @Override
    public List<Task> getAllTasksByUser(User user) {
        try {
            return jdbc.getAllTasksByUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean loginUser(String email, String password) {
        try {
            User user = jdbc.loginUser(email, password);


            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void addTaskWithHistory(Task task, User owner, User createdBy, History history) {
        task.setUser(owner);
        history.setUser(createdBy);
        task.getHistories().add(history);
        try {
            jdbc.createTaskAndHistory(task, history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // method pass id and return task
    @Override
    public Task getTask(int id) {
        try {
            return jdbc.getTaskById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // method to delete task
    @Override
    public void deleteTask(Task task) {
        History history = new History("delete task", LocalDate.now(), task, task.getUser());
        try {
            jdbc.deleteTaskAndHistory(task, history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method to update task
    @Override
    public void updateTask(Task task, History history, int id, User owner, User createdBy){
        task.setUser(owner);
        history.setUser(createdBy);
        task.getHistories().add(history);
        try{
            jdbc.updateTask(task, history, id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // method return all history
    @Override
    public List<History> getAllHistory() {
        try {
            return jdbc.getAllHistories();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // @hassan

    public List<Task> displayTasks(User owner) {
        try {

            tasks=jdbc.getTasks(owner);
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return an empty list in case of an exception
    }


    public void sortTasks(ITaskSort taskSort, String sortOrder) {
        taskSort.sortTasks(tasks, sortOrder);
    }

    public List<Task> sortByPriority( String order) {

        sortTasks((tasks, sortOrder) -> {
            Comparator<Task> priorityComparator = Comparator.comparing(Task::getPriority);
            if ("lowToHigh".equalsIgnoreCase(sortOrder)) {
                priorityComparator = priorityComparator.reversed();

            }

            Collections.sort(tasks, priorityComparator);
        }, order);
        return tasks;
    }


    public List<Task> sortByCretionDate( String order) {

        sortTasks((tasks, sortOrder) -> {
            Comparator<Task> DateComparator = Comparator.comparing(Task::getCreatedDate);
            if ("highToLow".equalsIgnoreCase(sortOrder)) {
                DateComparator = DateComparator.reversed();

            }

            Collections.sort(tasks, DateComparator);
        }, order);

        return tasks;
    }

    public List<Task> filterTasksByCategory(List<Task> tasks, Category category) {
        return tasks.stream()
                .filter(task -> task.getCategory() == category)
                .collect(Collectors.toList());
    }


    // methode for create file csv
    public static String CreateFile() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        File csvFile = new File("/Users/ayoub/Documents/Simplon/Titan Task/src/export/Tasksexport.csv" );
        return "/Users/ayoub/Documents/Simplon/Titan Task/src/export/Tasksexport.csv" ;
    }

    // method for export data from database to file cvs
    public static void exportTask() {
        String filePath = TaskManager.CreateFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)))) {
            FileWriter writerdel = new FileWriter(filePath, false);
            try (Connection connection = DataBase.connectionTodatabase()) {
                // Create a Statement object// Write headers
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM task";
                ResultSet resultSet = statement.executeQuery(query);
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    writer.append(resultSet.getMetaData().getColumnName(i));
                    if (i < resultSet.getMetaData().getColumnCount()) {
                        writer.append(",");
                    }

                }
                writer.append("\n");
                while (resultSet.next()) {
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        writer.append(resultSet.getString(i));
                        if (i < resultSet.getMetaData().getColumnCount()) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // Write data to CSV filewriter.newLine();

            // Write data


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    //methode for insert task in database;
    private static void insertTaskIntoDatabase(Connection connection, Task task) throws SQLException {
        String insertQuery = "INSERT INTO  task (title, description, category, priority, status, createdDate, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getCategory().name());
            preparedStatement.setString(4, task.getPriority().name());
            preparedStatement.setString(5, task.getStatus().name());
            preparedStatement.setDate(6, java.sql.Date.valueOf(task.getCreatedDate()));
            preparedStatement.setInt(7, task.getUser().getId());

            preparedStatement.executeUpdate();
        }
    }

    // method for import data form file csv to database
    public static void importTask() {
        List<Task> tasks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(TaskManager.CreateFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                System.out.println(line);
                // Assuming CSV columns are in the order: title, description, category, priority, status, createdDate, userId
                Task task2 = new Task(data[0], data[1], Category.valueOf(data[2]), Priority.valueOf(data[3]),
                        Status.valueOf(data[4]), LocalDate.parse(data[5]), JDBC.getUserById(Integer.parseInt(data[6])), new ArrayList<>());
                tasks.add(task2);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println(tasks);
        for (Task task : tasks) {
            try {
                insertTaskIntoDatabase(DataBase.connectionTodatabase(), task);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
