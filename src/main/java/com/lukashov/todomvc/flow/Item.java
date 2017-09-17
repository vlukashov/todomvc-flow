package com.lukashov.todomvc.flow;

import java.util.concurrent.atomic.AtomicInteger;

public class Item {
    private int id;
    private String title;
    private boolean completed;
    private static AtomicInteger counter = new AtomicInteger(1);

    public Item() {
    }

    public Item(String title, boolean completed) {
        this.id = counter.addAndGet(1);
        this.title = title;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
