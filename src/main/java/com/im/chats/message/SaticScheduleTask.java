package com.im.chats.message;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Scheduled(fixedRate=30*1000)
    private void configureTasks() throws Exception{
        Websocket.onMessage("start");


    }
}
