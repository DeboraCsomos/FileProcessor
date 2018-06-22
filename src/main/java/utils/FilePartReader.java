package utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Random;

import static java.lang.Math.toIntExact;
import static java.nio.file.Files.lines;

public class FilePartReader {

    private FileReader fileReader;
    private int totalLineNumber;
    private int linesToProcess;
    private Locale language;

    public FilePartReader(String filePath) {
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

    public FilePartReader(String filePath, int linesToProcess) {
        this(filePath);
        this.linesToProcess = linesToProcess;
    }

    public FilePartReader(String filePath, int linesToProcess, String language) {
        this(filePath, linesToProcess);
        this.language = new Locale(language);
    }

    @Override
    public String toString() {
        return "totalLineNumber: " + totalLineNumber +
                ", \nlinesToProcess: " + linesToProcess +
                ", \nlanguage:" + language;
    }
}
