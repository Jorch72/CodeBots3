package controller.parser;

import controller.code.lines.Line;

import java.util.HashMap;

public class BotParser {

    public static Line[] readLines(String code, String botName){
        HashMap<String, String> replacements = new HashMap<String, String>();
        String[] lines = code.trim().split("\n");
        for (int j = 0; j < lines.length; j++){
            String line = lines[j];

            line= line.substring(0,line.contains("//")?line.indexOf("//"):line.length()).trim();
            if (line.startsWith("$")){
                int labelPoint = line.indexOf(' ');
                String label = "\\"+line.substring(0, labelPoint);
                line = line.substring(labelPoint+1).trim();
                replacements.put(label, "#"+j);
            }
            lines[j] = line;
        }
        for (int j = 0; j < lines.length; j++){
            String line = lines[j];
            for (String label: replacements.keySet()){
                line = line.replaceAll(label, replacements.get(label));
            }
            lines[j] = line;
        }
        return  LineParser.buildLines(lines, botName);
    }
}
