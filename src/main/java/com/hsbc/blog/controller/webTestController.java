package com.hsbc.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class webTestController {

    /*@GetMapping("/{id}/{name}")
    public String index(@PathVariable int id,@PathVariable String name){
        return id+"::"+name;
    }
*/
    /*但是更规范的是将返回页面这些方法单独定义到一个的返回视图的controller中抽离出来*/
    @GetMapping("/")
    public ModelAndView get() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }
}
