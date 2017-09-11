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
import com.vaadin.flow.demo.helloworld.data.Item;
import com.vaadin.flow.html.Input;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Tag("td-todos")
@HtmlImport("frontend://src/td-todos/td-todos.html")
public class TdTodos extends PolymerTemplate<TdTodos.Model> implements View {
    public interface Model extends TemplateModel {

        void setItems(List<Item> items);
        void setActiveCount(int activeCount);
        void setAllCompleted(boolean allCompleted);
        void setAnyCompleted(boolean anyCompleted);
    }

    @Id("new-todo")
    private Input newTodoInput;

    private List<Item> items = new ArrayList<>();

    public TdTodos() {
        updateModel();
    }

    @EventHandler
    void addTodoAction() {
        String title = newTodoInput.getValue().trim();
        if (!title.isEmpty()) {
            items.add(new Item(title, false));
            updateModel();
        }
        newTodoInput.clear();
    }

    @EventHandler
    void cancelAddTodoAction() {
        newTodoInput.clear();
    }

    @EventHandler
    void destroyItemAction(@ModelItem Item item) {
        System.out.println("destroyItemAction: index of the given item: " +
                getModel().getListProxy("items", Item.class).indexOf(item));
    }

    @EventHandler
    void toggleAllCompletedAction(@EventData("event.target.checked") List<Integer> items) {
        System.out.println("toggleAllCompletedAction: items: [" +
                items.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "]");
    }

    @EventHandler
    void clearCompletedAction() {
        System.out.println("clearCompletedAction");
    }

    private void updateModel() {
        getModel().setItems(items);

        int completedCount = (int) items.stream().filter(Item::isCompleted).count();
        getModel().setActiveCount(items.size() - completedCount);
        getModel().setAnyCompleted(completedCount > 0);
        getModel().setAllCompleted(completedCount == items.size());
    }
}


