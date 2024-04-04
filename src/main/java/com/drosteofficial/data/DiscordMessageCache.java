package com.drosteofficial.data;

import com.drosteofficial.ScrapyBot;
import com.drosteofficial.entity.DiscordMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

public class DiscordMessageCache implements ICache<DiscordMessage, String>{
    ScrapyBot scrapyBot;
    Map<String,DiscordMessage>map = new HashMap<>();

    ISQLDataModel<DiscordMessage,String> dataModel;
    @Override
    public void init() {
        dataModel = new DiscordMessageDataModel(scrapyBot);
        dataModel.createTable();
    }

    @Override
    public Optional<DiscordMessage> get(String s) {
        return Optional.ofNullable(map.get(s));
    }

    @Override
    public boolean add(String s, DiscordMessage discordMessage) {
        this.save(discordMessage);
        map.put(s,discordMessage);
        return map.containsKey(s);
    }

    @Override
    public boolean delete(String s) {
        map.remove(s);
        return !map.containsKey(s);
    }

    @Override
    public Future<Boolean> deleteFromDB(String s) {
        return dataModel.delete(s);
    }

    @Override
    public Future<Boolean> save(DiscordMessage discordMessage) {
       return dataModel.save(discordMessage);
    }

    public DiscordMessageCache(ScrapyBot scrapyBot) {
        this.scrapyBot = scrapyBot;
    }
    public ISQLDataModel<DiscordMessage, String> getDataModel() {
        return dataModel;
    }
}
