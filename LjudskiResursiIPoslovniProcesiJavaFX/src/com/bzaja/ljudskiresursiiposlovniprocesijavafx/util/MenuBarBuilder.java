/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MenuBarBuilder {

    private final List<Menu> menus;
    private final List<Runnable> runnables;
    private final int indexOfMenuItem;
    
    public MenuBarBuilder(int indexOfMenuItem) {
        menus = new ArrayList<>();
        runnables = new ArrayList<>();
        this.indexOfMenuItem = indexOfMenuItem;
    }

    public MenuBarBuilder addMenu(String text) {
        menus.add(new Menu(text));
        return this;
    }

    public MenuBarBuilder addMenuItem(String text, Runnable runnable) {
        MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction((e) -> runnable.run());
        menus.get(menus.size() - 1).getItems().add(menuItem);
        runnables.add(runnable);
        return this;
    }

    public MenuBarResultDto build() {
        return new MenuBarResultDto(menus, runnables.get(indexOfMenuItem));
    }
}
