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

    private final FileReader fileReader;
    private final int totalLineNumber;
    private final int linesToProcess;
    private final Language language;

    private MyFilePartReader(MyFilePartReaderBuilder builder) {
        this.fileReader = builder.fileReader;
        this.totalLineNumber = builder.totalLineNumber;
        this.linesToProcess = builder.linesToProcess;
        this.language = builder.language;
    }

    public List<String> read() {
        List<String> content = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(fileReader)) {
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

    public Language getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "totalLineNumber: " + totalLineNumber +
                ", \nlinesToProcess: " + linesToProcess +
                ", \nlanguage:" + language;
    }

    public static class MyFilePartReaderBuilder {

        // mandatory
        private FileReader fileReader;

        // calculated
        private int totalLineNumber;

        //optional
        private int linesToProcess;
        private Language language;

        public MyFilePartReaderBuilder(String filePath) {
            try {
                this.fileReader = new FileReader(filePath);
                this.totalLineNumber = countTotalLinesInFile(filePath);
            } catch (FileNotFoundException exp) {
                System.out.println("File doesn't exist!");
                System.exit(1);
            } catch (IOException exp) {
                exp.printStackTrace();
            }
            Random random = new Random();
            this.linesToProcess = random.nextInt(totalLineNumber);
            this.language = HU;
        }

        public MyFilePartReaderBuilder setLinesToProcess(int linesToProcess) {
            this.linesToProcess = linesToProcess;
            return this;
        }

        public MyFilePartReaderBuilder setLanguage(Language language) {
            this.language = language;
            return this;
        }

        public MyFilePartReader build() {
            return new MyFilePartReader(this);
        }

        private int countTotalLinesInFile(String filePath) throws IOException {
            Path path = Paths.get(filePath);
            return toIntExact(lines(path).count());
        }

    }
}
