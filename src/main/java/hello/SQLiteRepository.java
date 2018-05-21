package hello;

import java.sql.*;

public class SQLiteRepository {
    private static Connection connection;
    private static boolean hasData;

    public ResultSet displayUsers() throws SQLException, ClassNotFoundException {
        checkConnection();
        return connection.createStatement().executeQuery("SELECT * FROM Users");
    }

    private void checkConnection() throws SQLException {
        if (connection != null) return;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Class not found");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        initialise();

    }

    private void initialise() throws SQLException {
        if (!hasData) {
            hasData = true;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user' ");
            if (!resultSet.next()) {
                System.out.println("Building user table with pre populated values");
                connection.createStatement().execute("CREATE TABLE user(id integer, " +
                        "fName varchar(60), " +
                        "lName varchar(60), " +
                        "primary key(id));");   // Створюємо таблицю

                // Створюємо тестові дані
                createUserStatement("John", "McNeil");
                createUserStatement("Kelly", "McNelly");
                createUserStatement("Sam", "Kelling");
                return;

            }

        }
    }

    private void createUserStatement(String fName, String lName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user values(?,?,?);");
        preparedStatement.setString(2, fName);
        preparedStatement.setString(3, lName);
        preparedStatement.execute();
    }

    public void addUser(String fName, String lName) throws SQLException {
        checkConnection();

        createUserStatement(fName, lName);
    }

}
