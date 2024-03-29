package com.drosteofficial.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KickMemberCommand extends Command {
    private final static Logger logger = LoggerFactory.getLogger(CommandManager.class);

    public KickMemberCommand() {
        this.setName("kickmember");
        this.setDescription("Kick member from server");
        this.setUsage("/kickmember <@member> <reason>");
        this.setCommandData(Commands.slash("kickmember", "Kick member from server")
                .addOption(OptionType.USER, "member", "The member to kick", true)
                .addOption(OptionType.STRING, "reason", "The reason for the kick", true));
        this.setOwnerCommand(true);

    }
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member) {

        if (event.getMember().getUser().isBot() == true) {
            return;
        }

        Member memberToKick = event.getOption("member").getAsMember();
        if (memberToKick == null) {
            event.reply("Member not found").setEphemeral(true).queue();
            return;
        }

        String reason = event.getOption("reason").getAsString();
        if (reason == null) {
            event.reply("Please provide a reason for the kick").setEphemeral(true).queue();
            return;
        }

        guild.kick(memberToKick, reason).queue();
        event.reply("Member kicked").queue();
    }
}
