package org.example.project2_webdev_server.Controllers;

import org.example.project2_webdev_server.DataBase.DBManager;
import org.example.project2_webdev_server.Entity.User;
import org.example.project2_webdev_server.Response.BasicResponse;
import org.example.project2_webdev_server.Response.BooleanResponse;
import org.example.project2_webdev_server.Response.LoginResponse;
import org.example.project2_webdev_server.Utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.example.project2_webdev_server.Utils.Errors.*;

@RestController
public class GenaralController {

    @Autowired
    private DBManager dbManager;


    @RequestMapping("/sign-up")
    public BasicResponse addUser(@RequestParam String username, @RequestParam String password) { // אנוטציה ריקווסט פאראם ולא באדי כי בצד לקוח שולחים משתנים ולא אובייקט עם שדות בבקשת פוסט
        if (username != null && !username.isEmpty()) { // נבדוק שהוכנס שפ משתמש
            boolean exists = dbManager.checkIfUsernameExists(username); // בדיקה אם אותו שם משתמש כבר קיים כדי לא לאפשר ליוזר חדש להרשם עם אותו שם משתמש
            if (exists) {
                return new BasicResponse(false, ERROR_USERNAME_ALREADY_EXISTS);
            }
            if (password != null && !password.isEmpty()) { // אם אפשר ליצור יוזרניים כזה שהוכנסה ססמה
                User user = new User(username, GeneralUtils.hash("", password)); // סיסמה מגובבת
                dbManager.createUserOnDb(user);
                return new BasicResponse(true, null);
            } else {
                return new BasicResponse(false, ERROR_MISSING_PASSWORD);
            }
        } else {
            return new BasicResponse(false, ERROR_MISSING_USERNAME);
        }
    }


    @RequestMapping("/sign-in")
    public BasicResponse signIn (@RequestParam String username, @RequestParam String password) {
        if (username != null && !username.isEmpty()) {
            if (password != null && !password.isEmpty()) {
                password = GeneralUtils.hash("", password); //גיבוב
                User user = dbManager.getUserByUsernameAndPassword(username, password); // אם יש יוזר כזה מחזיר אותו אם אין חוזר נאל
                if (user != null) { // אם חזר יוזר (קיים כזה)
                    return new LoginResponse(true, null, user); // הכניסה לחשבון הצליחה נחזיר תשובה בהתאם
                }
                else { // אם חזרה רשומה נאל מהדאטה בייס (כביכול לא קיים יוזר כזה)
                    boolean usernameExists = dbManager.checkIfUsernameExists(username); // קודם נבדוק אם בכלל הזין שם משתמש שקיים במערכת
                    if(usernameExists){ // אם השם משתמש הזה כן קיים זה אומר שהססמה שהזין לא נכונה
                        return new BasicResponse(false, ERROR_WRONG_PASSWORD);
                    }
                    else {// אם חזר מהדאטה בייס יוזר נאל וגם בוליאן(שם משתמש) נאל אז זה אומר שבאמת לא קיים יוזר כזה
                        return new BasicResponse(false, ERROR_NO_ACCOUNT);
                    }
                }
            } else { // אם לא הצליח להתחבר כי לא הוכנסה ססמה (היא נאל)
                return new BasicResponse(false, ERROR_MISSING_PASSWORD);
            }
        } else { // אם לא הצליח להתחבר כי לא הוכנס שם משתמש (הוא נאל)
            return new BasicResponse(false, ERROR_MISSING_USERNAME);
        }
    }
}
