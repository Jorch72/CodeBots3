package controller.parser;

public class BadExpressionException extends RuntimeException{
    public BadExpressionException(String message){
        super(message);
    }
}
