package com.example.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@RequestMapping
@RestController
public class ThreadController {

    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/hello")
    public void hello()  {
        /*
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            Thread.sleep(5000);
            System.out.println("Hello " + Thread.currentThread().getName());
            return null;
        });
        */

        Runnable runnable =
                () -> {
                    try {
                        Thread.sleep(5000);
                        template.convertAndSend("/topic/public", "Lambda Runnable running " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Lambda Runnable running " + Thread.currentThread().getName());
                };

        Thread thread = new Thread(runnable);
        thread.start();
    }
    
    @GetMapping("/hello1")
    public void hello1() throws InterruptedException {
        Thread.sleep(5000);
        template.convertAndSend("/topic/public", "Lambda Runnable running " + Thread.currentThread().getName());
    }
}