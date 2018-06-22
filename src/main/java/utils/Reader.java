package utils;

import java.util.List;

@FunctionalInterface
public interface Reader {
    List<String> read();
}
