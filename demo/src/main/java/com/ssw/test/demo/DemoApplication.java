package com.ssw.test.demo;

import com.ssw.myRpc.annotation.EnableMyRpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMyRpcClient(scanPath = "com.ssw.test.demo")
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
