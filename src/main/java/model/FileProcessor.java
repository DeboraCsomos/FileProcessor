package model;


import java.text.Collator;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static model.Language.EN;
import static model.Language.HU;

public class FileProcessor {
    private MyFilePartReader reader;
    private MyFileWriter writer;

    public FileProcessor(String[] args) {
        this.reader = getReaderByParameters(args);
        this.writer = new MyFileWriter(args[0]);
    }

    public void process() {
        List<String> content = reader.read();
        sortContent(content);
        writer.write(content);

    }

    private void sortContent(List<String> content) {
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
        Collator collator = Collator.getInstance(Locale.forLanguageTag(reader.getLanguage().name()));
        words.sort(collator);

        content.clear();
        content.addAll(numbers.stream().map(String::valueOf).collect(toList()));
        content.addAll(words);
    }

    public MyFilePartReader getReaderByParameters(String[] args) {
        MyFilePartReader fpr;
        if (args.length == 0) {
            System.out.println("Mandatory parameter [filePath] is missing. \n" +
                    "Please provide a valid path to an existing tex file!");
            System.exit(1);
        } else if (args.length > 3) {
            System.out.println("Too many parameters!");
            System.exit(1);
        }
        String filePath = args[0];
        if (args.length == 1) {
            fpr = new MyFilePartReader(filePath);
        } else {
            int toLine = parseLineNumber(args[1]);
            if (args.length == 2) {
                fpr = new MyFilePartReader(filePath, toLine);
            } else {
                Language language = parseLanguage(args[2]);
                fpr = new MyFilePartReader(filePath, toLine, language);
            }
        }
        return fpr;
    }

    private Language parseLanguage(String langParam) {
        Language language;
        if (langParam.toUpperCase().equals(HU.name()) || langParam.toUpperCase().equals(EN.name())) {
            language = Language.valueOf(langParam.toUpperCase());
        } else {
            language = HU;
        }
        return language;
    }

    private int parseLineNumber(String lineNumberParam) {
        int toLine = 0;
        try {
            toLine = parseInt(lineNumberParam);
        } catch (NumberFormatException exp) {
            System.out.println("Second parameter must be a number!");
            System.exit(1);
        }
        return toLine;
    }

}
