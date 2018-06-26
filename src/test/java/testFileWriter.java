import model.MyFileWriter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class testFileWriter {
    private static final String FILE_NAME = "testfileForWriter";
    private static final String FILE_PATH = "./src/test/testresources/";

    @Test
    public void testWrite() {
        MyFileWriter writer = new MyFileWriter(FILE_PATH.concat(FILE_NAME));
        writer.write(Arrays.asList("New", "content", "in", "file"));
        String pathToNewFile = FILE_PATH + "processed_".concat(FILE_NAME);
        File processed = new File(pathToNewFile);
        assertThat(processed.exists(), is(true));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToNewFile));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(stringBuilder.toString(), is("Newcontentinfile"));
    }

    @Test
    public void testWriteGeneric() {
        MyFileWriter writer = new MyFileWriter(FILE_PATH.concat(FILE_NAME));
        writer.write(Arrays.asList(1, 2, 3, 4));
        String pathToNewFile = FILE_PATH + "processed_".concat(FILE_NAME);
        File processed = new File(pathToNewFile);
        assertThat(processed.exists(), is(true));

        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToNewFile));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(stringBuilder.toString(), is("1234"));
    }

}
