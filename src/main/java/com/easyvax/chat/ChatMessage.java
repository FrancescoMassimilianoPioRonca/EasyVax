package com.easyvax.chat;


/**
 * Questo rappresenta il model del message della chat. Come si può notare ci sono diversi parametri
 * come il tipo del messaggio (CHAT,JOIN,LEAVE) a seconda  se un utente si unisce o se si disconette.
 * Poi i vari setter e getter
 */

public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
