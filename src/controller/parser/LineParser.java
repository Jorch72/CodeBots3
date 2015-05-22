package controller.parser;

import controller.Game;
import controller.code.lines.*;

public class LineParser {

    public static Line[] buildLines(String[] code, String botName){
        Line[] lines = new Line[Game.MAX_INT];
        for (int i = 0; i < code.length; i++){
            int split = code[i].indexOf(' ');
            String command;
            String[] params;
            if (split != -1) {
                command = code[i].substring(0, split);
                params = code[i].substring(split + 1).split(" ");
            } else {
                command = code[i];
                params = new String[0];
            }
            if (command.equals("flag")){
                lines[i] = buildLine(command, botName);
            } else {
                lines[i] = buildLine(command, params);
            }
        }
        for (int i = code.length; i < lines.length; i++){
            lines[i] = buildLine("flag", botName);
        }
        return lines;
    }
    private static Line buildLine(String command, String...parameters){
        Line line;
        if (command.equals("flag")){
            checkNumParams(command, parameters, 1);
            line = new FlagLine(parameters[0]);
        } else if (command.equals("move")){
            checkNumParams(command, parameters, 1);
            line = new MoveLine(ExpressionParser.parseValue(parameters[0]));
        } else if (command.equals("rotate")){
            checkNumParams(command, parameters, 1);
            line = new RotateLine(ExpressionParser.parseValue(parameters[0]));
        } else if (command.equals("lock")){
            checkNumParams(command, parameters, 1);
            String toLock = parameters[0];
            if (ExpressionParser.isLineNumber(toLock)){
                line = new LockLine(ExpressionParser.parseLineNumber(toLock));
            } else {
                line = new LockVarLine(ExpressionParser.parseVariable(toLock));
            }
        } else if (command.equals("if")){
            checkNumParams(command, parameters, 3);
            line = new IfLine(ExpressionParser.parseCondition(parameters[0]),
                    ExpressionParser.parseValue(parameters[1].substring(1)),
                    ExpressionParser.parseValue(parameters[2].substring(1)));
        } else if (command.equals("start")){
            checkNumParams(command, parameters, 1);
            line = new StartLine(ExpressionParser.parseVariable(parameters[0]));
        } else if (command.equals("stop")) {
            checkNumParams(command, parameters, 1);
            line = new StopLine(ExpressionParser.parseVariable(parameters[0]));
        } else if (command.equals("copy")) {
            checkNumParams(command, parameters, 2);
            String from = parameters[0];
            String to = parameters[1];
            if (ExpressionParser.isLineNumber(from)){
                line = new CopyLine(ExpressionParser.parseLineNumber(from), ExpressionParser.parseLineNumber(to));
            } else {
                line = new CopyVarLine(ExpressionParser.parseValue(from), ExpressionParser.parseVariable(to));
            }
        } else {
            throw new BadExpressionException("Command ("+command+") not recognized");
        }
        return line;
    }

    private static void checkNumParams(String command, String[] params, int expected){
        if (params.length < expected){
            throw new BadExpressionException(command + " needs more parameters (only "+params.length+" were given)");
        } else if (params.length > expected){
            throw new BadExpressionException(command + " was given too many parameters ("+params.length+" instead of "+expected+")");
        }
    }



}
