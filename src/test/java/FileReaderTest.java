import model.MyFileReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class FileReaderTest {
    private static final String FILE_PATH = "./src/test/testresources/testfile_simple";
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Test
    public void testRead() {
        MyFileReader reader = new MyFileReader(FILE_PATH, 3);

        List<String> content = reader.read();
        assertThat(content.size(), is(3));
        assertThat(content, contains("111", "12", "bqhpz"));

        MyFileReader reader2 = new MyFileReader(FILE_PATH, 22);
        List<String> content2 = reader2.read();
        assertThat(content2.size(), is(22));

        MyFileReader readerHighLineNumber = new MyFileReader(FILE_PATH, 1000);

        List<String> content3 = readerHighLineNumber.read();
        assertThat(content3.size(), is(22));

        MyFileReader reader4 = new MyFileReader(FILE_PATH, null);
        List<String> content4 = reader4.read();
        assertThat(content4, not(empty()));
    }

    @Test
    public void testInvalidPath() {
        exit.expectSystemExitWithStatus(1);
        new MyFileReader("invalid_file_path", null);
        assertEquals("File doesn't exist!\n", systemErrRule.getLog());
    }

}
