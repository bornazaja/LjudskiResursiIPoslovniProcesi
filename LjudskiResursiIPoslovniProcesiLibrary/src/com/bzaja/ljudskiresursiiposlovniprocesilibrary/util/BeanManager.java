/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Component;

/**
 *
 * @author Borna
 */
@Component
public class BeanManager {

    @Autowired
    private Set<BeanDefinition> beanDefinitions;

    public String getFullEntityClassNameByClassBaseName(String className) {
        return beanDefinitions.stream().filter(x -> x.getBeanClassName().endsWith(className)).findFirst().orElse(null).getBeanClassName();
    }
}
