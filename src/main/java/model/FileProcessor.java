package model;


import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.text.Collator;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class FileProcessor {
    private @NonNull MyReader reader;
    private @NonNull MyWriter writer;
    private @NonNull Locale language;

    public void process() {
        List<String> content = reader.read();
        sort(content);
        writer.write(content);

    }

    private void sort(List<String> content) {
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
        Collator collator = Collator.getInstance(language);
        words.sort(collator);

        content.clear();
        content.addAll(numbers.stream().map(String::valueOf).collect(toList()));
        content.addAll(words);
    }

}
