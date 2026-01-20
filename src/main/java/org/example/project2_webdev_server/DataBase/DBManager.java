package org.example.project2_webdev_server.DataBase;
import jakarta.annotation.PostConstruct;
import org.example.project2_webdev_server.Entity.User;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// צריך ליצור סכמה עם שם מתאים וטבלת יוזרים שתגיל את העמודות:
// id - int auto incremented (pk)
// username - varchar(50) (unique key)
// password - varchar(50)

@Component
public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/project2";   // אנה שימי לב! כשנגדיר את הדאטה בייס זה יהיה עם הפרטים האלה וחייב לקרוא לסכמה "project2"
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // אנה כשאת מריצה אצלך שימי 1234

    private Connection connection;

    @PostConstruct
    public void connect() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("DB connected successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUserOnDb (User user) { // להרשמה
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "INSERT INTO users (username, password)" +
                            "VALUE (?, ?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate(); // יש שינוי בטבלה לכן אפדייט ולא קוורי כי לא חוזר משהו
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser (String username, String password) { // כרגע לא בשימוש
        try {
            PreparedStatement preparedStatement = this.connection
                    .prepareStatement("SELECT id " +
                            "FROM users " +
                            "WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                return user;
            }
        } catch (SQLException e) {

        }
        return null;
    }


    public boolean checkIfUsernameExists (String username) { // בדיקה אם היוזר קיים להרשמה
        try {
            PreparedStatement preparedStatement =
                    this.connection.prepareStatement(
                            "SELECT username FROM users " +
                                    "WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserByUsernameAndPassword (String username, String password) { //להתחברות
        try {
            PreparedStatement preparedStatement =
                    this.connection.prepareStatement(
                            "SELECT username, password FROM users " +
                                    "WHERE username = ? " +
                                    "AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password); // נשמר מה שמגובב ששולחים מהמתודה עם הנתיב בקונטרולר
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String usr = resultSet.getString("username");
                String pwd = resultSet.getString("password");
                return new User(usr, pwd);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /*
     טבלת היוזרים שנגדיר בהמשך:
     CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    profile_image_url VARCHAR(255) // יכול להיות נאל כי בהוספת יוזר חדש עדיין אין קישור בטבלה
);


     טבלת העוקבים-נעקבים:
    CREATE TABLE follows (
    follower_username VARCHAR(50) NOT NULL,
    followed_username VARCHAR(50) NOT NULL,

    PRIMARY KEY (follower_username, followed_username),

    FOREIGN KEY (follower_username)
        REFERENCES users(username)
        ON DELETE CASCADE,

    FOREIGN KEY (followed_username)
        REFERENCES users(username)
        ON DELETE CASCADE
);


שאילתא - מי עוקב אחרי יוזר מסוים:
SELECT follower_username
FROM follows
WHERE followed_username = ?;



שאילתא - מי היוזר המסויים עוקב אחריהם:
SELECT followed_username
FROM follows
WHERE follower_username = ?;


     */

    public List<String> getFollowing (String username, String password){ // תחזיר את רשימת האנשים שהיוזר עוקב אחריהם
        List<String> following = new ArrayList<>();
        try {
            PreparedStatement preparedStatement =
                    this.connection.prepareStatement(
                             "SELECT followed_username" +
                                     "FROM follows" +
                                     "WHERE follower_username = ?;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                following.add(resultSet.getString("followed_username"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return following;
    }

    public List<String> getFollowers (String username, String password){ // תחזיר את רשימת האנשים שעוקבים אחרי אותו יוזר מסוים
        List<String> followers = new ArrayList<>();
        try {
            PreparedStatement preparedStatement =
                    this.connection.prepareStatement(
                            "SELECT follower_username" +
                                    "FROM follows" +
                                    "WHERE followed_username = ?;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                followers.add(resultSet.getString("follower_username"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return followers;
    }

}
