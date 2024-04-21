package com.drosteofficial.commands.musicDiscordCommands;

import com.drosteofficial.IScrapyBot;
import com.drosteofficial.ScrapyBot;
import com.drosteofficial.commands.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuitCommand extends Command {

    private final static Logger logger = LoggerFactory.getLogger(QuitCommand.class);

    private final ScrapyBot scrapyBot;

    public QuitCommand(IScrapyBot scrapyBot) {
        setName("quit");
        setDescription("quit the voice channel");
        setUsage("/quit");
        this.setUsage("/quit <link do muzyki>");
        var commandData = Commands.slash("play", "Plays music on voice channels");
        commandData.addOption(OptionType.STRING, "url", "URL to music source", true);
        this.scrapyBot = (ScrapyBot) scrapyBot;
        this.setCommandData(Commands.slash("quit", "Making bot leave voice channel"));

    }


    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member) {
        var audioManager = guild.getAudioManager();

        if (event.getMember().getUser().isBot()){
            event.reply(":satellite: Bot's cant invoke commands").setEphemeral(true).queue();
            return;
        }
        if (event.getMember().getVoiceState().inAudioChannel() == false) {
            event.reply(":satellite: You need to be in a voice channel to use this command").setEphemeral(true).queue();
            return;
        }

        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
            event.reply(":mobile_phone_off: Disconnected from the voice channel").setEphemeral(true).queue();
        } else {
            event.reply(" Bot was not connected to a voice channel").setEphemeral(true).queue();
        }
    }

}
