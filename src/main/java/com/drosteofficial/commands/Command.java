package com.drosteofficial.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public abstract class Command extends ListenerAdapter {
   private String name;
   private String description;
   private String usage;
   private CommandData commandData;
   private boolean supremeCommand;
   private boolean ownerCommand;
   private boolean requirePermission;
   private Permission permission;
   private boolean subcommand;
   private String subcommandName;
   private boolean nfsw;

    public abstract void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public @NotNull CommandData getCommandData() {
        return commandData;
    }

    public void setCommandData(CommandData commandData) {
        this.commandData = commandData;
    }

    public boolean isSupremeCommand() {
        return supremeCommand;
    }

    public void setSupremeCommand(boolean supremeCommand) {
        this.supremeCommand = supremeCommand;
    }

    public boolean isOwnerCommand() {
        return ownerCommand;
    }

    public void setOwnerCommand(boolean ownerCommand) {
        this.ownerCommand = ownerCommand;
    }

    public boolean isRequirePermission() {
        return requirePermission;
    }

    public void setRequirePermission(boolean requirePermission) {
        this.requirePermission = requirePermission;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public boolean isSubcommand() {
        return subcommand;
    }

    public void setSubcommand(boolean subcommand) {
        this.subcommand = subcommand;
    }

    public String getSubcommandName() {
        return subcommandName;
    }

    public void setSubcommandName(String subcommandName) {
        this.subcommandName = subcommandName;
    }

    public boolean isNfsw() {
        return nfsw;
    }

    public void setNfsw(boolean nfsw) {
        this.nfsw = nfsw;
    }


}
