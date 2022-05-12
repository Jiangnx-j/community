package com.jiangnx.community.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/alpha")
public class AlphaController {

    @RequestMapping("/hello")
    @ResponseBody
    //String返回值表示返回的是网页，@ResponseBody表示返回的是数据
    public String sayHelo(){
        return "Hello";
    }
}
