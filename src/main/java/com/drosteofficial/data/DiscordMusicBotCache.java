package com.drosteofficial.data;

import com.drosteofficial.ScrapyBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;

public class DiscordMusicBotCache implements ICache<MusicManager, String>{

    private final static Logger logger = LoggerFactory.getLogger(DiscordMusicBotCache.class);
    private final ScrapyBot scrapyBot;
    private final Map<String, MusicManager> musicManagers;

    public DiscordMusicBotCache(ScrapyBot scrapyBot) {
        this.scrapyBot = Objects.requireNonNull(scrapyBot);
        this.musicManagers = new HashMap<>();
    }

    @Override
    public void init() {
    }

    @Override
    public Optional<MusicManager> get(String u) {
        if (musicManagers.containsKey(u)) {
            return Optional.of(musicManagers.get(u));
        }
        return Optional.empty();
    }


    @Override
    public boolean add(String s, MusicManager musicManager) {
        if (musicManagers.containsKey(s)) {
            return false;
        }
        this.musicManagers.put(s, musicManager);
        return true;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public Future<Boolean> deleteFromDB(String s) {
        this.musicManagers.remove(s);
        return null;
    }

    @Override
    public Future<Boolean> save(MusicManager musicManager) {
        throw new UnsupportedOperationException("Not implemented");
    }



}
