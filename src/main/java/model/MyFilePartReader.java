package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static java.lang.Math.toIntExact;
import static java.nio.file.Files.lines;

public class MyFilePartReader implements MyReader {

    private FileReader fileReader;
    private int totalLineNumber;
    private int linesToProcess;
    private Locale language;


    MyFilePartReader(String filePath) {
        try {
            this.fileReader = new FileReader(filePath);
        } catch (FileNotFoundException exp) {
            System.out.println("File doesn't exist!");
            System.exit(1);
        }
        try {
            Path path = Paths.get(filePath);
            totalLineNumber = toIntExact(lines(path).count());
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        Random random = new Random();
        this.linesToProcess = random.nextInt(totalLineNumber);
        this.language = new Locale("hu");


    }

    MyFilePartReader(String filePath, int linesToProcess) {
        this(filePath);
        this.linesToProcess = linesToProcess;
    }

    MyFilePartReader(String filePath, int linesToProcess, String language) {
        this(filePath, linesToProcess);
        this.language = new Locale(language);
    }

    public List<String> read() {
        List<String> content = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            for (int i = 0; i < linesToProcess; i++) {
                line = reader.readLine();
                content.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public Locale getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "totalLineNumber: " + totalLineNumber +
                ", \nlinesToProcess: " + linesToProcess +
                ", \nlanguage:" + language;
    }
}