package org.example.project2_webdev_server.Response;
import org.example.project2_webdev_server.Entity.User;

public class LoginResponse extends BasicResponse{ // הודעה שיוזר הצליח להתחבר
    private User user;

    public LoginResponse(User user) {
        this.user = user;
    }

    public LoginResponse(boolean success, Integer errorCode, User user) {
        super(success, errorCode);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
