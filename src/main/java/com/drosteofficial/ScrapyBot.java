package com.drosteofficial;

import com.drosteofficial.commands.Command;
import com.drosteofficial.commands.CommandManager;
import com.drosteofficial.commands.KickMemberCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class ScrapyBot implements IScrapyBot{

    private JDABuilder builder;
    private JDA jda;
    CommandManager commandManager;




    @Override
    public void Init() {

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









}
