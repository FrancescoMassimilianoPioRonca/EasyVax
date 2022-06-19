package com.easyvax.controller;

import com.easyvax.chat.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

/**
 * Questi metodi saranno responsabili della ricezione dei messaggi da un client e quindi della trasmissione ad altri.
 * Il secondo metodo in particolare, è responsabile dell'inserimento di uno user al topic che nel nostro caso sarà uno
 */
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    //Gestisce la visualizzazione delle pagine in html
    // (Verrà eliminato quando sarà fatto il front end)
    @RequestMapping(path = "/chat", method = RequestMethod.GET)
    public String chat()
    {
        return "chat.html";
    }

}
