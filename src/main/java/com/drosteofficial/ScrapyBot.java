package com.drosteofficial;

import com.drosteofficial.Listeners.BotListener;
import com.drosteofficial.Listeners.MemeListener;
import com.drosteofficial.commands.Command;
import com.drosteofficial.commands.CommandManager;
import com.drosteofficial.commands.KickMemberCommand;
import com.drosteofficial.commands.musicDiscordCommands.*;
import com.drosteofficial.data.DiscordMessageCache;
import com.drosteofficial.data.DiscordMusicBotCache;
import com.drosteofficial.data.ICache;
import com.drosteofficial.data.MusicManager;
import com.drosteofficial.database.Database;
import com.drosteofficial.database.MySQL;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ScrapyBot implements IScrapyBot{

    private JDABuilder builder;
    private JDA jda;
    private CommandManager commandManager;
    private Database database;
    private DiscordMessageCache discordMessageCache;
    private AudioPlayerManager audioPlayerManager;
    private ICache<MusicManager, String> musicManager;



    @Override
    public void Init() {
        //init
        builder.enableIntents(

                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MODERATION,
                GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.MESSAGE_CONTENT

        );

        commandManager = new CommandManager();
        commandManager.addCommand(new KickMemberCommand());
        commandManager.addCommand(new JoinCommand(this));
        commandManager.addCommand(new QuitCommand(this));
        commandManager.addCommand(new PlayCommand(this));
        commandManager.addCommand(new QueueCommand(this));
        commandManager.addCommand(new repeatCommand(this));
        commandManager.addCommand(new SkipCommand(this));
        commandManager.addCommand(new StopCommand(this));
        commandManager.addCommand(new VolumeCommand(this));

        //commands registered
      builder.addEventListeners(commandManager);
      builder.addEventListeners(new BotListener(this));
      builder.addEventListeners(new MemeListener());


      discordMessageCache = new DiscordMessageCache(this);
      discordMessageCache.init();

        this.musicManager = new DiscordMusicBotCache(this);

    }
    @Override
    public void Start() {
        jda = builder.build();
        registerCommands();
    }

    @Override
    public void Stop() {
        jda.shutdown();
    }

    private void registerCommands() {

        for (Command command : commandManager.getCommandMap().values()) {
            jda.retrieveCommands().queue(commands-> {
                commands.forEach(existingCommand -> {
                    if (existingCommand.getName().equals(command.getCommandData().getName())) {
                        existingCommand.delete().queue();
                    }
                });
            });
            jda.upsertCommand(command.getCommandData()).queue();
            System.out.println("Command registered: " + command.getName());
        }
        jda.updateCommands().queue(commands -> commands.forEach(command -> jda.upsertCommand(CommandData.fromCommand(command)).queue()));
        jda.updateCommands().queue();
    }


    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Connection getDatabaseConnection()throws SQLException {
        return database.GetConnection();
    }

    public DiscordMessageCache getDiscordMessageCache() {
        return discordMessageCache;
    }
    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }
    public ICache<MusicManager, String> getMusicManagers() {
        return musicManager;
    }
    public Optional<MusicManager> getMusicManager(String guildID) {
        return musicManager.get(guildID);
    }

    public AudioPlayerManager getAudioManager() {
        return audioPlayerManager;
    }
}

