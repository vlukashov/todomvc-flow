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

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;

import java.util.Arrays;
import java.util.List;

@Tag("todo-app")
@HtmlImport("frontend://components/todo-app.html")
@StyleSheet("context://styles.css")
public class TodoApp extends PolymerTemplate<TodoApp.Model> implements View {
    public static class Item {
        private String title;
        private boolean completed;

        public Item() {
        }

        public Item(String title, boolean completed) {
            this.title = title;
            this.completed = completed;
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

    private void setItems(List<Item> items) {
        getModel().setItems(items);

        int completedCount = (int) items.stream().filter(Item::isCompleted).count();
        getModel().setActiveCount(items.size() - completedCount);
        getModel().setAllCompleted(completedCount == items.size());
        getModel().setAnyCompleted(completedCount > 0);
    }

    public TodoApp() {
        setItems(Arrays.asList(
                new Item("Get a running Flow app skeleton", true),
                new Item("Copy custom elements from a Polymer 2 app", false),
                new Item("Move business logic to Flow", false)));
    }

    @Override
    public void onLocationChange(LocationChangeEvent locationChangeEvent) {
        getModel().setRoute(locationChangeEvent.getLocation().getFirstSegment());
    }
}
