package com.masterangler.mock;

import java.util.HashSet;
import java.util.Set;

public class Item {
    private String name;
    private Set<String> tags = new HashSet<>();

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
}
