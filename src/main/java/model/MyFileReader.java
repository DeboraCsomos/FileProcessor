package model;

import lombok.NonNull;

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

public class MyFileReader implements MyReader {

    private FileReader reader;
    private final long linesToProcess;

    public MyFileReader(@NonNull String filePath, Integer linesToProcess){
        try {
            this.reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            System.err.println("File doesn't exist!");
            System.exit(1);
        }
        long totalLineNumber = countTotalLinesInFile(filePath);
        if (linesToProcess == null) {
            Random random = new Random();
            this.linesToProcess = random.nextInt(toIntExact(totalLineNumber) + 1);
        } else if (linesToProcess > totalLineNumber) {
            this.linesToProcess = totalLineNumber;
        } else {
            this.linesToProcess = linesToProcess;
        }
    }

    public List<String> read() {
        List<String> content = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(this.reader)) {
            String line;
            int lineCount = 1;
            while ((line = reader.readLine()) != null && lineCount <= linesToProcess) {
                content.add(line);
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private long countTotalLinesInFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return lines(path).count();
        } catch (IOException e) {
            System.err.println("An error occurred while reaching the file!");
            System.exit(1);
            return 0;
        }
    }

}
