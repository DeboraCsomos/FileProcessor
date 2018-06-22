package utils;


import java.text.Collator;
import java.util.*;

import static java.lang.Integer.parseInt;

public class FileProcessor {
    private FilePartReader reader;
    private Writer fileWriter;

    public FileProcessor(String[] args) {
        this.reader = getReaderByParameters(args);
    }

    public void process() {
        List<String> content = reader.read();
        System.out.println("before sort:" + content);
        sortContent(content);
        System.out.println("after content: " + content);
    }

    private void sortContent(List<String> content) {
        Set<String> uniqueContent = new HashSet<>(content);
        List<String> numbers = new ArrayList<>();
        List<String> words = new ArrayList<>();
        for (String line : uniqueContent) {
            try {
                Integer.parseInt(line);
                numbers.add(line);
            } catch (NumberFormatException e) {
                words.add(line);
            }
        }
        Collections.sort(numbers);
        Collator collator = Collator.getInstance(reader.getLanguage());
        words.sort(collator);

        content.clear();
        content.addAll(numbers);
        content.addAll(words);
    }

    public FilePartReader getReaderByParameters(String[] args) {
        FilePartReader fpr;
        if (args.length == 0) {
            System.out.println("Mandatory parameter [filePath] is missing. \n" +
                    "Please provide a valid path to an existing text testfile!");
            System.exit(1);
        } else if (args.length > 3) {
            System.out.println("Too much parameter!");
            System.exit(1);
        }
        String filePath = args[0];
        if (args.length == 1) {
            fpr = new FilePartReader(filePath);
        } else {
            int toLine = parseLineNumber(args);
            if (args.length == 2) {
                fpr = new FilePartReader(filePath, toLine);
            } else {
                String language = parseLanguage(args[2]);
                fpr = new FilePartReader(filePath, toLine, language);
            }
        }
        return fpr;
    }

    private String parseLanguage(String arg) {
        String language;
        if (!arg.equals("hu") && !arg.equals("en")) {
            language = "hu";
        } else {
            language = arg;
        }
        return language;
    }

    private int parseLineNumber(String[] args) {
        int toLine = 0;
        try {
            toLine = parseInt(args[1]);
        } catch (NumberFormatException exp) {
            System.out.println("Second parameter must be a number!");
            System.exit(1);
        }
        return toLine;
    }

}
