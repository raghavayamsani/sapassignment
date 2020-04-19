package com.sap;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("com.sap")
@SpringBootApplication
@Configuration
public class Bootstrap {
    public static void main(String[] args){
        SpringApplication.run(Bootstrap.class,args);
    }
}
