import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class FileProcessorAppIntegrationTest {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Test
    public void testFileProcessorApp() throws IOException {
        String filePath = "./src/test/testresources/";
        String fileName = "testfile_simple";
        String[] args = new String[]{"-path", filePath + fileName, "-line", "10", "-lang", "hu"};
        FileProcessorApp.main(args);
        File f = new File(filePath + "processed_" + fileName);
        assertThat(f.exists(), is(true));
        BufferedReader reader = new BufferedReader(new FileReader(filePath + "processed_" + fileName));
        List<String> content = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            content.add(line);
        }
        assertThat(content, contains("12", "41", "111", "bqhpz", "pkfpm", "qfnxz", "tieag", "uoklc", "zptpm"));
    }

    private Object[] parametersForTestForErrors() {
        return new Object[]{
                new Object[]{new String[]{"-path", "invalid_path"}, "File doesn't exist!\n"},
                new Object[]{new String[0], "Mandatory parameter [filePath] is missing. \n" +
                        "Please provide a valid path to an existing text file!\n"}
        };
    }

    @Test
    @Parameters
    public void testForErrors(String[] input, String errorMessage) {
        systemErrRule.clearLog();
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertEquals(errorMessage, systemErrRule.getLog()));
        FileProcessorApp.main(input);
    }
}
