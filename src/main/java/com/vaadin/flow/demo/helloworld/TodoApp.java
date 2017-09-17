/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.demo.helloworld;

import com.vaadin.annotations.*;
import com.vaadin.flow.html.Input;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Tag("todo-app")
@HtmlImport("frontend://components/todo-app.html")
@StyleSheet("context://styles.css")
public class TodoApp extends PolymerTemplate<TodoApp.Model> implements View {

    @Id("new-todo")
    private Input newTodoInput;
    private List<Item> allItems;
    private String filter;

    public static class Item {
        private int id;
        private String title;
        private boolean completed;
        private static AtomicInteger counter = new AtomicInteger(1);

        public Item() {
        }

        private Item(int id, String title, boolean completed) {
            this.id = id;
            this.title = title;
            this.completed = completed;
        }

        public static Item create(String title, boolean completed) {
            int id = counter.addAndGet(1);
            return new Item(id, title, completed);
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

    public interface Model extends TemplateModel {
        void setItems(List<Item> items);
        void setAllCompleted(boolean allCompleted);
        void setAnyCompleted(boolean anyCompleted);
        void setActiveCount(int activeCount);
        void setRoute(String route);
    }

    public TodoApp() {
        allItems = new ArrayList<>(3);
        allItems.add(Item.create("Get a running Flow app skeleton", true));
        allItems.add(Item.create("Copy custom elements from a Polymer 2 app", false));
        allItems.add(Item.create("Move business logic to Flow", false));
    }

    private void setFilter(String filter) {
        if (filter == null) {
            filter = "";
        }

        filter = filter.trim().toLowerCase(Locale.ENGLISH);
        if (!"active".equals(filter) && !"completed".equals(filter)) {
            filter = "";
        }

        if (!filter.equals(this.filter)) {
            this.filter = filter;
            updateModel(allItems, filter);
        }
    }

    private void updateModel(List<Item> allItems, String filter) {
        List<Item> items = allItems;
        if ("active".equals(filter)) {
            items = allItems.stream().filter(item -> !item.isCompleted()).collect(Collectors.toList());
        } else if ("completed".equals(filter)) {
            items = allItems.stream().filter(item -> item.isCompleted()).collect(Collectors.toList());
        }

        getModel().setItems(items);

        int completedCount = (int) allItems.stream().filter(Item::isCompleted).count();
        getModel().setActiveCount(allItems.size() - completedCount);
        getModel().setAllCompleted(completedCount == allItems.size());
        getModel().setAnyCompleted(completedCount > 0);

        getModel().setRoute(filter);
    }

    @Override
    public void onLocationChange(LocationChangeEvent locationChangeEvent) {
        setFilter(locationChangeEvent.getLocation().getFirstSegment().toLowerCase());
    }

    @EventHandler
    private void addTodoAction() {
        String title = newTodoInput.getValue().trim();
        if (!title.isEmpty()) {
            allItems.add(Item.create(title, false));
            updateModel(allItems, filter);
        }
        newTodoInput.clear();
    }

    @EventHandler
    private void destroyItemAction(
            @EventData("event.detail") int id) {
        allItems.removeIf(item -> item.getId() == id);
        updateModel(allItems, filter);
    }

    @EventHandler
    private void updateItemAction(
            @EventData("event.detail.id") int id,
            @EventData("event.detail.title") String title,
            @EventData("event.detail.completed") boolean completed) {
        Optional<Item> allItemsItem = allItems.stream().filter(item -> item.getId() == id).findFirst();
        allItemsItem.ifPresent(item -> {
            item.setTitle(title);
            item.setCompleted(completed);
            updateModel(allItems, filter);
        });
    }

    @EventHandler
    private void toggleAllCompletedAction(
            @EventData("event.target.checked") boolean completed) {
        Set<Integer> displayedIds = getModel().getListProxy("items", Item.class)
                .stream().map(Item::getId).collect(Collectors.toSet());
        allItems.forEach(item -> {
            if (displayedIds.contains(item.getId())) {
                item.setCompleted(completed);
            }
        });
        updateModel(allItems, filter);
    }

    @EventHandler
    private void clearCompletedAction() {
        allItems = allItems.stream().filter(item -> !item.isCompleted()).collect(Collectors.toList());
        updateModel(allItems, filter);
    }
}
