package model;

import java.util.List;

@FunctionalInterface
public interface MyWriter {
    <T> void write(List<T> newContent);
}
