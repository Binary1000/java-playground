package com.binary.mvc;

import annotation.RequestMapping;

/**
 * @author Binary
 */
public class IndexController {

    @RequestMapping("login")
    public void login() {
        System.out.println("login");
    }

    @RequestMapping("register")
    public void register() {
        System.out.println("register");
    }
}
