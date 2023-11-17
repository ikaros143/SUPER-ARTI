package org.example;

import love.forte.simboot.spring.autoconfigure.EnableSimbot;
import org.example.untils.CoreUsage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableSimbot // 启用simbot
@SpringBootApplication
@ComponentScan({"org.example.listener","org.example.mute","org.example.untils"})
@EnableScheduling//定时注解
public class Main {
    /**
     * main方法，启动Spring应用程序。
     */


    public static void main(String[] args) {
        CoreUsage.setup();
        SpringApplication.run(Main.class, args);
    }



}

