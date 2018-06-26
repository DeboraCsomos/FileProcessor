import model.MyFileReader;
import model.MyFileReader.MyFilePartReaderBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class testFileReader {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    private static final String FILE_PATH = "./src/test/testresources/";

    @Test
    public void testRead() {
        MyFileReader reader = new MyFilePartReaderBuilder(FILE_PATH.concat("testfile_simple"))
                                  .setLinesToProcess(3)
                                  .build();

        List<String> content = reader.read();
        assertThat(content.size(), is(3));
        assertThat(content, contains("111", "12", "bqhpz"));

        MyFileReader reader2 = new MyFilePartReaderBuilder(FILE_PATH.concat("testfile_simple"))
                .setLinesToProcess(22)
                .build();

        List<String> content2 = reader2.read();
        assertThat(content2.size(), is(22));

        MyFileReader readerHighLineNumber = new MyFilePartReaderBuilder(FILE_PATH.concat("testfile_simple"))
                                                .setLinesToProcess(1000)
                                                .build();

        List<String> content3 = readerHighLineNumber.read();
        assertThat(content3.size(), is(22));



    }

    @Test
    public void testInvalidPath() {
        exit.expectSystemExitWithStatus(1);
        new MyFilePartReaderBuilder("invalid_file_path").build();
        assertEquals("File doesn't exist!", systemErrRule.getLog());
    }

}
