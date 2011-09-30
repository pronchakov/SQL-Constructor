package ru.gs.sql.exceptions;

/**
 * TOP level exception while creating SQL query
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public class SQLCreationException extends Exception {
    
    private String reason;

    public SQLCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLCreationException(String message) {
        super(message);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
}
