package com.drosteofficial.entity;

import net.dv8tion.jda.api.entities.Guild;

import java.sql.Timestamp;
import java.time.Instant;

public class DiscordMessage {

   private int id;
   private String guildId;
   private String messageId;
   private String authorId;
   private String channelId;
   private String content;
   private String author;
   private Instant timestamp;
    private MessageAction action;


    public DiscordMessage(int id, String guildId, String messageId, String authorId, String channelId, String content, String author, Instant timestamp, MessageAction action) {
        this.id = id;
        this.guildId = guildId;
        this.messageId = messageId;
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.action = action;
    }

    public DiscordMessage(String guildId, String messageId, String authorId, String channelId, String content, String author, Instant timestamp, MessageAction action) {
        this.guildId = guildId;
        this.messageId = messageId;
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public MessageAction getAction() {
        return action;
    }

    public void setAction(MessageAction action) {
        this.action = action;
    }
}
