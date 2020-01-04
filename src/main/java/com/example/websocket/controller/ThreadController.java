package com.example.websocket.controller;
import com.example.websocket.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequestMapping
@RestController
public class ThreadController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    //@GetMapping("/hello")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) throws InterruptedException {
        Runnable runnable =
                () -> {
                    try {
                        Thread.sleep(5000);
                        template.convertAndSend("/topic/public", getMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Lambda Runnable running " + Thread.currentThread().getName());
                };

        Thread thread = new Thread(runnable);
        thread.start();
        //System.out.println("Lambda Runnable running " + Thread.currentThread().getName());
        Thread.sleep(5000);
        return chatMessage;

    }

    @GetMapping("/hello1")
    public void hello1() throws InterruptedException {
        Thread.sleep(5000);
        template.convertAndSend("/topic/public", getMessage());
    }

    private ChatMessage getMessage() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setSender("admin");
        chatMessage.setContent("Lambda Runnable running " + Thread.currentThread().getName());
        return chatMessage;
    }
}