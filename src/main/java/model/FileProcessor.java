package model;


import model.MyFileReader.MyFilePartReaderBuilder;

import java.io.Reader;
import java.text.Collator;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static model.Language.EN;
import static model.Language.HU;

public class FileProcessor {
    private MyReader reader;
    private MyWriter writer;
    private Language language;

    public FileProcessor(String[] args) {
        this.reader = getReaderByParameters(args);
        String filePathParam = args[0];
        this.writer = new MyFileWriter(filePathParam);

        if (args.length == 3) {
            String langParam = args[2];
            this.language = parseLanguage(langParam);
        } else {
            this.language = HU;
        }
    }

    public void process() {
        List<String> content = reader.read();
        sortContent(content);
        writer.write(content);

    }

    public void sortContent(List<String> content) {
        Set<String> uniqueContent = new HashSet<>(content);
        List<Integer> numbers = new ArrayList<>();
        List<String> words = new ArrayList<>();
        for (String line : uniqueContent) {
            try {
                Integer num = Integer.parseInt(line);
                numbers.add(num);
            } catch (NumberFormatException e) {
                words.add(line);
            }
        }
        Collections.sort(numbers);
        Collator collator = Collator.getInstance(Locale.forLanguageTag(language.name()));
        words.sort(collator);

        content.clear();
        content.addAll(numbers.stream().map(String::valueOf).collect(toList()));
        content.addAll(words);
    }

    public MyFileReader getReaderByParameters(String[] args) {
        if (args.length < 1) {
            System.err.println("Mandatory parameter [filePath] is missing. \n" +
                    "Please provide a valid path to an existing text file!");
            System.exit(1);
        } else if (args.length > 3) {
            System.err.println("Too many parameters!");
            System.exit(1);
        }
        String filePath = args[0];
        MyFilePartReaderBuilder builder = new MyFilePartReaderBuilder(filePath);
        if (args.length > 1) {
            int toLine = parseLineNumber(args[1]);
            builder.setLinesToProcess(toLine);
        }
        return builder.build();
    }

    public Language parseLanguage(String langParam) {
        Language language;
        if (langParam.toUpperCase().equals(HU.name()) || langParam.toUpperCase().equals(EN.name())) {
            language = Language.valueOf(langParam.toUpperCase());
        } else {
            language = HU;
        }
        return language;
    }

    public int parseLineNumber(String lineNumberParam) {
        int toLine = 0;
        try {
            toLine = parseInt(lineNumberParam);
        } catch (NumberFormatException exp) {
            System.err.println("Second parameter must be a number!");
            System.exit(1);
        }
        return toLine;
    }

}
