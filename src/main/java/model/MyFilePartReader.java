package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.toIntExact;
import static java.nio.file.Files.lines;
import static model.Language.HU;

public class MyFilePartReader implements MyReader {

    private FileReader fileReader;
    private int totalLineNumber;
    private int linesToProcess;
    private Language language;


    public MyFilePartReader(String filePath) {
        try {
            this.fileReader = new FileReader(filePath);
        } catch (FileNotFoundException exp) {
            System.out.println("File doesn't exist!");
            System.exit(1);
        }
        try {
            this.totalLineNumber = countTotalLinesInFile(filePath);
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        Random random = new Random();
        this.linesToProcess = random.nextInt(totalLineNumber);
        this.language = HU;


    }

    public MyFilePartReader(String filePath, int linesToProcess) {
        this(filePath);
        if (linesToProcess > totalLineNumber) {
            this.linesToProcess = totalLineNumber;
        } else {
            this.linesToProcess = linesToProcess;
        }
    }

    public MyFilePartReader(String filePath, int linesToProcess, Language language) {
        this(filePath, linesToProcess);
        this.language = language;
    }

    public List<String> read() {
        List<String> content = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            int lineCount = 1;
            while ((line = reader.readLine()) != null && lineCount <= linesToProcess){
                content.add(line);
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private int countTotalLinesInFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return toIntExact(lines(path).count());
    }

    public Language getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "totalLineNumber: " + totalLineNumber +
                ", \nlinesToProcess: " + linesToProcess +
                ", \nlanguage:" + language;
    }
}
