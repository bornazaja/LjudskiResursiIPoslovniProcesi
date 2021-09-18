/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import java.util.List;
import javafx.scene.control.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Borna
 */
@Data
@AllArgsConstructor
public class MenuBarResultDto {
    private List<Menu> menus;
    private Runnable menuItemRunnable;
}
