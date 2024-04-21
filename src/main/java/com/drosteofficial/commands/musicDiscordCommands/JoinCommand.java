package com.drosteofficial.commands.musicDiscordCommands;

import com.drosteofficial.IScrapyBot;
import com.drosteofficial.ScrapyBot;
import com.drosteofficial.commands.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinCommand extends Command {

    private final static Logger logger = LoggerFactory.getLogger(JoinCommand.class);
    private final ScrapyBot scrapyBot;

    public JoinCommand(IScrapyBot scrapyBot) {
        this.scrapyBot = (ScrapyBot) scrapyBot;
        setName("join");
        setDescription("Join the voice channel");
        setUsage("/join");
        this.setCommandData(Commands.slash("join", "Making bot join voice channel") );


    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member) {

        if (event.getMember().getUser().isBot()) {
            event.reply(":satellite: Bot's cant invoke commands").setEphemeral(true).queue();
            return;
        }

        var audioManager = guild.getAudioManager();
        var voiceChannel = member.getVoiceState().getChannel();

        if (voiceChannel == null) {
            event.reply(":satellite: You must be in a voice channel to use this command").setEphemeral(true).queue();
            return;
        }
        if (guild.getAudioManager().isConnected()) {
            event.reply(":satellite: I'm already connected to a voice channel").setEphemeral(true).queue();
            return;
        }

        audioManager.openAudioConnection(voiceChannel);
        event.reply(":satellite: Music Bot joined voice channel").queue();

    }
}
