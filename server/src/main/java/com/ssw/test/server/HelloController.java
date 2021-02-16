package com.ssw.test.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @RequestMapping("/aaa/test")
    @ResponseBody
    public String hello(){
        //System.out.println(HelloController.class.getPackage());
        System.out.println(helloService.hello("ssw"));
        System.out.println(helloService.hello3("ssw"));
        System.out.println(helloService.hello2("ssw"));
        return "aaa";
    }

}
