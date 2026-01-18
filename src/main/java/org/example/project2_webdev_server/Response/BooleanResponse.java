package org.example.project2_webdev_server.Response;

public class BooleanResponse extends BasicResponse{ // תשובה לוליאנית בנוסף ךבייסיק ריספונס - האם היוזר כבר קיים או לא (בעת הרשמה)
    private boolean result;

    public BooleanResponse(boolean result) {
        this.result = result;
    }

    public BooleanResponse(boolean success, Integer errorCode, boolean result) {
        super(success, errorCode);
        this.result = result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
