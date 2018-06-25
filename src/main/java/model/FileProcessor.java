package model;


import java.text.Collator;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

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
        Collator collator = Collator.getInstance(reader.getLanguage());
        words.sort(collator);

        content.clear();
        content.addAll(numbers.stream().map(String::valueOf).collect(toList()));
        content.addAll(words);
    }

    public MyFilePartReader getReaderByParameters(String[] args) {
        MyFilePartReader fpr;
        if (args.length == 0) {
            System.out.println("Mandatory parameter [filePath] is missing. \n" +
                    "Please provide a valid path to an existing text testfile!");
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
                String language = parseLanguage(args[2]);
                fpr = new MyFilePartReader(filePath, toLine, language);
            }
        }
        return fpr;
    }

    private String parseLanguage(String langParam) {
        String language;
        if (!langParam.equals("hu") && !langParam.equals("en")) {
            language = "hu";
        } else {
            language = langParam;
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
