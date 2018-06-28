import model.FileProcessor;
import model.MyReader;
import model.MyWriter;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.cglib.core.Local;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class FileProcessorTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private MyReader reader;

    @Mock private MyWriter writer;

    @Captor
    private ArgumentCaptor<List<String>> argumentCaptor;

    private static List<String> LANGUAGE_LIST = new ArrayList<>(asList("dzx", "d", "5", "dzs"));
    private List<String> inputList;
    private List<String> processedList;
    private Locale lang;

    public FileProcessorTest(List<String> inputList, List<String> processedList, Locale lang) {
        this.inputList = inputList;
        this.processedList = processedList;
        this.lang = lang;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
                {LANGUAGE_LIST, asList("5", "d", "dzx", "dzs"), new Locale("hu")},
                {LANGUAGE_LIST, asList("5", "d", "dzs", "dzx"), new Locale("en")},
                {new ArrayList<>(asList("source", "111", "screen", "ice", "47", "Meal")),
                        asList("47", "111", "ice", "Meal", "screen", "source"), new Locale("hu")}
        });
    }

    @Test
    public void testProcess() {
        when(reader.read()).thenReturn(inputList);
        FileProcessor processor = new FileProcessor(reader, writer, lang);
        processor.process();
        verify(writer).write(argumentCaptor.capture());
        assertThat(argumentCaptor.getAllValues().get(0), is(processedList));
    }


}
