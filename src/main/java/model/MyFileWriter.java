package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MyFileWriter implements MyWriter {
    private String filePath;

    public MyFileWriter(String originalFilePath) {
        Path p = Paths.get(originalFilePath);
        String originalFileName = p.getFileName().toString();
        String fileName = "processed_".concat(originalFileName);
        this.filePath = originalFilePath.replace(originalFileName, fileName);

    }

    @Override
    public <T> void write(List<T> newContent) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (T line : newContent) {
                bw.write(line.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
