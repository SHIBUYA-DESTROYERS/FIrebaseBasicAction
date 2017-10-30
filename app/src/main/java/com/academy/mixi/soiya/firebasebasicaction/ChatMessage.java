package com.academy.mixi.soiya.firebasebasicaction;

public class ChatMessage {
    public String body;
    public String sender;
    public long timestamp;

    public ChatMessage(String body, String sender, long timestamp) {
        this.body = body;
        this.sender = sender;
        this.timestamp = timestamp;
    }
}
