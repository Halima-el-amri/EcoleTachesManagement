package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBase {

    private static final String URl = "jdbc:mysql://root@localhost:3306/TitanTask";
    private static final String USERNAME = "root@localhost";
    private static final String PASSWORD = "";
    public static final String line = "----------------------------------------------------------------------------------------";

    public static Connection connectionTodatabase() {
        try {
            Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
            if (connection != null) {
                System.out.println("Connected to the database!");
                return connection;
            }
        } catch (SQLException exception) {

            System.out.println("error connect to database!" + exception.getMessage());
        }
        return null;
    }

    public static void connection() {

        try {
            Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
            if (connection != null) {
                System.out.println(line);
                System.out.println("Connected to the database!");
                System.out.println(line);
            }
        } catch (SQLException exception) {
            System.out.println("error connect to database!" + exception.getMessage());
        }
        // TODO : ADD CONNECTION

        // create tables that we need: user, task and history
        try {
            String createUserTableQuery = "CREATE TABLE IF NOT EXISTS user (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "role VARCHAR(50))";

            String createTaskTableQuery = "CREATE TABLE IF NOT EXISTS task (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "title VARCHAR(255) NOT NULL," +
                    "description VARCHAR(255)," +
                    "category VARCHAR(255)," +
                    "priority VARCHAR(50)," +
                    "status VARCHAR(50)," +
                    "createdDate DATE," +
                    "user_id INT," +
                    "softDelete BOOLEAN DEFAULT FALSE," +
                    "FOREIGN KEY (user_id) REFERENCES user(id))";

            String createHistoryTableQuery = "CREATE TABLE IF NOT EXISTS history (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "task_id INT," +
                    "user_id INT," +
                    "nameOperation VARCHAR(255)," +
                    "dateOperation DATE," +
                    "FOREIGN KEY (task_id) REFERENCES task(id)," +
                    "FOREIGN KEY (user_id) REFERENCES user(id))";

            Connection connection = DriverManager.getConnection(URl, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(createUserTableQuery);
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement(createTaskTableQuery);
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement(createHistoryTableQuery);
            preparedStatement.execute();
            System.out.println(line);
            System.out.println("Tables created successfully!");
            System.out.println(line);
        } catch (SQLException exception) {
            System.out.println("error create tables!" + exception.getMessage());
        }
    }
}