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

public class MyFileReader implements MyReader {

    private final FileReader fileReader;
    private final long totalLineNumber;
    private final long linesToProcess;

    private MyFileReader(MyFilePartReaderBuilder builder) {
        this.fileReader = builder.fileReader;
        this.totalLineNumber = builder.totalLineNumber;
        this.linesToProcess = builder.linesToProcess;
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


    @Override
    public String toString() {
        return "totalLineNumber: " + totalLineNumber +
                ", linesToProcess: " + linesToProcess;
    }

    public static class MyFilePartReaderBuilder {

        // mandatory
        private FileReader fileReader;
        // calculated
        private long totalLineNumber;
        //optional
        private long linesToProcess;

        public MyFilePartReaderBuilder(String filePath) {
            try {
                this.fileReader = new FileReader(filePath);
                this.totalLineNumber = countTotalLinesInFile(filePath);
            } catch (FileNotFoundException exp) {
                System.err.println("File doesn't exist!");
                System.exit(1);
            } catch (IOException exp) {
                exp.printStackTrace();
            }
            Random random = new Random();
            this.linesToProcess = random.nextInt(toIntExact(totalLineNumber) + 1);
        }

        public MyFilePartReaderBuilder setLinesToProcess(int linesToProcess) {
            if (linesToProcess <= this.totalLineNumber) {
                this.linesToProcess = linesToProcess;
            } else {
                this.linesToProcess = this.totalLineNumber;
            }
            return this;
        }

        public MyFileReader build() {
            return new MyFileReader(this);
        }

        private long countTotalLinesInFile(String filePath) throws IOException {
            Path path = Paths.get(filePath);
            return lines(path).count();
        }

    }
}
