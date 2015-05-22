package controller.parser;

import controller.bot.BotPrototype;
import controller.code.lines.Line;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileParser {
    public static List<BotPrototype> readAllBots() {
        try {
            File botFolder = new File("src/bots");
            File[] botFiles = botFolder.listFiles();
            List<BotPrototype> bots = new ArrayList<BotPrototype>();
            if (botFiles == null) {
                return bots;
            }
            for (File f: botFiles) {
                String name = f.getName();
                name = name.substring(0, name.indexOf('.'));
                Line[] lines = BotParser.readLines(new Scanner(f).useDelimiter("\\Z").next().toLowerCase(), name);
                bots.add(new BotPrototype(lines, name));

            }
            return bots;
        } catch (FileNotFoundException exception){
            throw new RuntimeException("Unable open to open correct files");
        }
    }

}
