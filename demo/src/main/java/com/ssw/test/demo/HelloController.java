package com.ssw.test.demo;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @Autowired
    HelloService helloService;

    @RequestMapping("/test")
    @ResponseBody
    public String hello(){

        System.out.println(helloService.hello("ssw"));
        System.out.println(helloService.hello3("ssw"));
        //System.out.println(helloService.hello2("ssw"));
        return "aaa";
    }

}
