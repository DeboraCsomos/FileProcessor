import lombok.Getter;
import lombok.NonNull;
import model.*;
import org.apache.commons.cli.*;

import java.util.Locale;

import static java.lang.Integer.parseInt;

public class FileProcessorApp {

    public static void main(String[] args) {
        Options options = getOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (MissingOptionException exp) {
            System.err.println("Mandatory parameter [filePath] is missing.\n" +
                    "Please provide a valid path to an existing text file!");
            System.exit(1);
        } catch (ParseException e) {
            System.err.println("Couldn't parse the arguments properly!");
            System.exit(1);
        }
        ArgObject argObject = new ArgObject().extractFrom(cmd);
        String path = argObject.getPath();
        Integer line = argObject.getLine();
        Locale lang = argObject.getLang();

        MyReader reader = new MyFileReader(path, line);
        MyWriter writer = new MyFileWriter(path);
        FileProcessor processor = new FileProcessor(reader, writer, lang);
        processor.process();
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption(Option.builder("path").required().hasArg(true).desc("Path of the file to be processed.").build());
        options.addOption(Option.builder("line").hasArg(true).desc("The number of lines to be processed.").build());
        options.addOption(Option.builder("lang").hasArg(true).desc("The language of the processing.").build());
        return options;
    }

    @Getter
    private static class ArgObject {
        private String path;
        private Integer line;
        private Locale lang;

        private ArgObject extractFrom(@NonNull CommandLine cmd) {
            path = cmd.getOptionValue("path");
            if (cmd.hasOption("line")) {
                line = parseInt(cmd.getOptionValue("line"));
            }
            if (cmd.hasOption("lang") && cmd.getOptionValue("lang").toLowerCase().equals("en")) {
                lang = Locale.forLanguageTag("en");
            } else {
                lang = Locale.forLanguageTag("hu");
            }
            return this;
        }
    }
}
