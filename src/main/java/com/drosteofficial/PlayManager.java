package com.drosteofficial;

import com.dunctebot.sourcemanagers.DuncteBotSources;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

import java.util.Objects;

public class PlayManager extends DefaultAudioPlayerManager {
    private final IScrapyBot scrapyBot;

    public PlayManager(IScrapyBot _scrapyBot) {
        this.scrapyBot = Objects.requireNonNull(_scrapyBot);
    }

    public void initialize() {
        AudioSourceManagers.registerLocalSource(this);
        AudioSourceManagers.registerRemoteSources(this);
        DuncteBotSources.registerAll(this, "pl-PL");

        source(YoutubeAudioSourceManager.class).setPlaylistPageCount(20);
    }
}



