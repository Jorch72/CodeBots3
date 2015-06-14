package controller.parser;

import controller.Game;
import controller.code.lines.Line;

import java.util.HashMap;

public class BotParser {

    public static Line[] readLines(String code, String botName){
        HashMap<String, String> replacements = new HashMap<String, String>();
        String[] lines = code.trim().split("\n");
        String[] filledLines = new String[Game.MAX_INT];
        int filledIter = 0;
        for (String line: lines){

            line= line.substring(0,line.contains("//")?line.indexOf("//"):line.length()).trim();

            if (line.startsWith("$")){
                int labelPoint = line.indexOf(' ');
                String label = line.substring(0, labelPoint);
                line = line.substring(labelPoint+1).trim();
                replacements.put(label, filledIter+"");
            }
            if (!line.isEmpty()){
                filledLines[filledIter] = line;
                filledIter++;
            }
        }
        for (; filledIter < Game.MAX_INT; filledIter++){
            filledLines[filledIter] = "flag";
        }
        for (int j = 0; j < Game.MAX_INT; j++){
            String line = filledLines[j];
            for (String label: replacements.keySet()){
                if (line.contains(label)){
                    if (line.startsWith("copy") && !line.contains("#") && line.lastIndexOf("$")==line.indexOf("$")){
                        line = line.replaceAll("\\"+label+"\\b", replacements.get(label));
                    } else {
                        line = line.replaceAll("\\"+label+"\\b", "#"+replacements.get(label));
                    }
                }
            }
            filledLines[j] = line;
        }
        return LineParser.buildLines(filledLines, botName);
    }
}
