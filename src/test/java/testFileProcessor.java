import model.FileProcessor;
import model.Language;
import model.MyFileReader;
import model.MyFileReader.MyFilePartReaderBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

//@RunWith(MockitoJUnitRunner.class)
public class testFileProcessor {
    private static final String FILE_PATH = "./src/test/testresources/";
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().mute();


    @Test
    public void testProcessHU() {
        String huFile = "testfile_hu";
        String[] args = new String[]{FILE_PATH.concat(huFile), "4", "hu"};

        FileProcessor processor = new FileProcessor(args);
        processor.process();
        MyFileReader reader = new MyFilePartReaderBuilder(FILE_PATH.concat("processed_" + huFile)).setLinesToProcess(4).build();
        List<String> content = reader.read();
        assertThat(content, contains("5", "d", "dzx", "dzs"));
    }

    @Test
    public void testProcessEN() {
        String huFile = "testfile_hu";
        String[] args = new String[]{FILE_PATH.concat(huFile), "4", "en"};

        FileProcessor processor = new FileProcessor(args);
        processor.process();
        MyFileReader reader = new MyFilePartReaderBuilder(FILE_PATH.concat("processed_" + huFile)).setLinesToProcess(4).build();
        List<String> content = reader.read();
        assertThat(content, contains("5", "d", "dzs", "dzx"));
    }

    @Test
    public void testSortContent() {
        List<String> listToBeSorted = new ArrayList<>(asList("source", "111", "screen", "ice", "47", "Meal"));
        FileProcessor processor = new FileProcessor(new String[]{FILE_PATH.concat("testfile_simple"), "4", "hu"});
        processor.sortContent(listToBeSorted);
        assertThat(listToBeSorted.size(), is(6));
        assertThat(listToBeSorted, contains("47", "111", "ice", "Meal", "screen", "source"));
    }

    @Test
    public void testGetReaderByParameters() {
        FileProcessor processor = new FileProcessor(new String[]{FILE_PATH.concat("testfile_simple"), "4", "hu"});
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertEquals("Mandatory parameter [filePath] is missing. \n" +
                "Please provide a valid path to an existing text file!\n", systemErrRule.getLog()));
        processor.getReaderByParameters(new String[0]);

        systemErrRule.clearLog();

        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertEquals("Too many parameters!\n", systemErrRule.getLog()));
        processor.getReaderByParameters(new String[4]);

        systemErrRule.clearLog();

        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> {
            assertEquals("Second parameter must be a number!\n", systemErrRule.getLog());

        });
        processor.getReaderByParameters(new String[]{FILE_PATH.concat("testfile_simple"), "text"});

        // This must create a valid reader
        MyFileReader validReader = processor.getReaderByParameters(new String[]{FILE_PATH.concat("testfile_simple"), "4", "hu"});
        List<String> content = validReader.read();
        assertThat(content.size(), is(4));
    }

    @Test
    public void testParseLanguage() {
        FileProcessor processor = new FileProcessor(new String[]{FILE_PATH.concat("testfile_simple"), "4", "hu"});
        Language hu = processor.parseLanguage("hu");
        assertThat(hu, is(Language.HU));

        Language en = processor.parseLanguage("en");
        assertThat(en, is(Language.EN));

        Language defaultHU = processor.parseLanguage("random");
        assertThat(defaultHU, is(Language.HU));
    }

    @Test
    public void testParseLineNumber() {
        FileProcessor processor = new FileProcessor(new String[]{FILE_PATH.concat("testfile_simple"), "4", "hu"});
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> {
            assertEquals("Second parameter must be a number!\n", systemErrRule.getLog());

        });
        systemErrRule.clearLog();
        processor.parseLineNumber("not a parsable number");

        int lineNumber = processor.parseLineNumber("1");
        assertThat(lineNumber, is(1));
    }
}
