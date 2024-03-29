package com.drosteofficial.commands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;


public class CommandManager extends ListenerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(CommandManager.class);

    private Map<String,Command> commandMap;

    public CommandManager() {
        commandMap = new HashMap<>();
    }

    @Override
    public void onSlashCommandInteraction(@Nullable SlashCommandInteractionEvent event) {
        if (event.getMember().getUser().isBot()) {
            return;
        }

        var command = commandMap.get(event.getName());
        if (command == null) {
            logger.error("Command not found: " + event.getName());
            event.reply("Command not found").setEphemeral(true).queue();
            return;
        }

        var channel = (TextChannel) event.getChannel();
        //TODO: Dodać walidację dla administratorów servera
        if (!event.getMember().isOwner() && command.isOwnerCommand() ){
            event.reply("You do not have permission to use this command1").setEphemeral(true).queue();
            return;
        }

        if (command.isSupremeCommand() && !event.getMember().isOwner()) {
            event.reply("You do not have permission to use this command2").setEphemeral(true).queue();
            return;
        }

        if (!event.getMember().hasPermission(command.getPermission()) && command.isRequirePermission()) {
            event.reply("You do not have permission to use this command3").setEphemeral(true).queue();
            return;
        }

        if (command.isNfsw() && !channel.isNSFW()) {
            event.reply("This command can only be used in NSFW channels").setEphemeral(true).queue();
            return;
        }

        if ((command.isSubcommand() && event.getSubcommandName() == null) || (command.isSubcommand() && event.getSubcommandGroup() == null)) {
            event.reply(":grimacing: Niepoprawnie użycie polecenia, wpisz /help " + event.getName() + " aby uzyskac pomoc.").queue();
            event.reply(command.getUsage()).queue();
            return;
        }

        command.execute(event, channel, event.getGuild(), event.getMember());
    }

    public void addCommand(Command command) {
        commandMap.put(command.getName(), command);
    }

    public void removeCommand(String key) {
        commandMap.remove(key);
    }
    public Map<String,Command> getCommandMap() {
        return commandMap;
    }



}
