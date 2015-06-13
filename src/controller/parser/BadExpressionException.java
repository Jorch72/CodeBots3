package controller.parser;

public class BadExpressionException extends RuntimeException{
    public BadExpressionException(String message){
        super(message);
    }
    public BadExpressionException(String message, Exception cause){
        super(message, cause);
    }
}
