package exception;

public class DaoException extends RuntimeException{

    private final String code;

    public DaoException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
