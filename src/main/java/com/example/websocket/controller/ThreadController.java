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
public class ThreadController extends Thread{

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) throws InterruptedException {

//        Runnable runnable =
//                () -> {
//                   try {
//                        Thread.sleep(5000);
//                        template.convertAndSend("/topic/public", getMessage());
//                        ChatMessage chatMessageT = new ChatMessage();
//                        chatMessageT.setType(ChatMessage.MessageType.CHAT);
//                        chatMessageT.setSender(chatMessage.getSender());
//                        chatMessageT.setContent(chatMessage.getContent());
//                        template.convertAndSend("/topic/public", chatMessageT);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                };
//        Thread thread = new Thread(runnable);

        Thread thread = new Thread(){
            public void run(){
            try {
                Thread.sleep(5000);
                template.convertAndSend("/topic/public", getMessage());
                ChatMessage chatMessagePool = new ChatMessage();
                chatMessagePool.setType(ChatMessage.MessageType.CHAT);
                chatMessagePool.setSender(chatMessage.getSender());
                chatMessagePool.setContent(chatMessage.getContent());
                template.convertAndSend("/topic/public", chatMessagePool);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            }
        };
        thread.start();
        return null;
    }

    @MessageMapping("/chat.sendMessageThread")
    @SendTo("/topic/public")
    public ChatMessage sendMessageThread(@Payload ChatMessage chatMessage) throws InterruptedException {
        Thread.sleep(5000);
        ChatMessage chatMessageOneThread = new ChatMessage();
        chatMessageOneThread.setType(ChatMessage.MessageType.CHAT);
        chatMessageOneThread.setSender("admin");
        chatMessageOneThread.setContent("Thread: Thread-1 " + Thread.currentThread().getState() );
        template.convertAndSend("/topic/public", chatMessageOneThread);
        return chatMessage;
    }

    private ChatMessage getMessage() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setSender("admin");
        chatMessage.setContent("Thread: " + Thread.currentThread().getName() + " " + Thread.currentThread().getState());
        return chatMessage;
    }
}