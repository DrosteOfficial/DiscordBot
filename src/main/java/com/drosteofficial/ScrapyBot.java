package com.drosteofficial;

import com.drosteofficial.commands.Command;
import com.drosteofficial.commands.CommandManager;
import com.drosteofficial.commands.KickMemberCommand;
import com.drosteofficial.data.DiscordMessageCache;
import com.drosteofficial.database.Database;
import com.drosteofficial.database.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ScrapyBot implements IScrapyBot{

    private JDABuilder builder;
    private JDA jda;
    private CommandManager commandManager;
    private Database database;
    private DiscordMessageCache discordMessageCache;

    @Override
    public void Init() {
        Map<String, String> databaseConfiguration = new HashMap<>();
       // databaseConfiguration.put("url", "jdbc:mysql://localhost:3306/scrapybot");

        database.init();


        //Prep of bot start
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

        //commands registered
      builder.addEventListeners(commandManager);
      builder.addEventListeners(new BotListener(this));
      builder.addEventListeners(new MemeListener());


      discordMessageCache = new DiscordMessageCache(this);
      discordMessageCache.init();

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

        jda.retrieveCommands().queue(commands -> commands.forEach(command -> command.delete().queue()));


        for (Command command : commandManager.getCommandMap().values()) {
            jda.upsertCommand(command.getCommandData()).queue();
            System.out.println("Command registered: " + command.getName());
        }   
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

}
