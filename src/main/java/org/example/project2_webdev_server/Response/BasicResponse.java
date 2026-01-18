package org.example.project2_webdev_server.Response;

public class BasicResponse { // תגובה בסיסית כשיוזר מנסה לפנות לשרת (להתחבר/להרשם)
    private boolean success;
    private Integer errorCode;

    public BasicResponse() {
    }

    public BasicResponse(boolean success, Integer errorCode) {
        this.success = success;
        this.errorCode = errorCode; // יהיה נאל אם אין שגיאה
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
