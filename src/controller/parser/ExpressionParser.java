package controller.parser;

import controller.Game;
import controller.code.expressions.*;
import controller.code.expressions.conditions.Conditional;
import controller.code.expressions.conditions.EqualsCondition;
import controller.code.expressions.conditions.LessThanCondition;

public class ExpressionParser {

    public static Value parseValue(String expression){
        if (expression.contains("+")){
            return parseSummation(expression);
        }
        try {
            int value = Integer.parseInt(expression);
            return new Constant(value);
        } catch (NumberFormatException e){
            return parseVariable(expression);
        }
    }
    public static Variable parseVariable(String expression){
        if (expression.startsWith("*")){
            return new OpponentVariable(parseVariable(expression.substring(1)));
        } else {
            if (expression.length() > 1){
                invalidExpression(expression);
            }
            int index = expression.charAt(0)-'a';
            if (index < 0 || index >= Game.NUM_VARS){
                invalidExpression(expression);
            }
            return new Variable(expression.charAt(0)-'a');
        }
    }
    public static Summation parseSummation(String expression){
        String[] parts = expression.split("\\+");
        Value[] values = new Value[parts.length];
        for (int i = 0; i < parts.length; i++){
            values[i] = parseValue(parts[i].trim());
        }
        return new Summation(values);
    }
    public static LineNumber parseLineNumber(String expression){
        if (expression.startsWith("*")){
            return new OpponentLineNumber(parseLineNumber(expression.substring(1)));
        } else {
            return new LineNumber(parseValue(expression.substring(1)));
        }
    }
    public static boolean isLineNumber(String expression){
        return expression.contains("#");
    }

    public static Conditional parseCondition(String condition){
        Conditional conditional;
        if (condition.contains("<")){
            int index = condition.indexOf('<');
            Value val1 = parseValue(condition.substring(0, index));
            Value val2 = parseValue(condition.substring(index+1));
            conditional = new LessThanCondition(val1, val2);
        } else {
            int index = condition.indexOf('=');
            Value val1 = parseValue(condition.substring(0, index));
            Value val2 = parseValue(condition.substring(index+1));
            conditional = new EqualsCondition(val1, val2);
        }
        return conditional;
    }

    private static void invalidExpression(String expression){
        throw new BadExpressionException("Expression \""+expression+"\" cannot be parsed");
    }

}
