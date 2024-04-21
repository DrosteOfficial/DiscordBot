package com.drosteofficial.commands.musicDiscordCommands;

import com.drosteofficial.IScrapyBot;
import com.drosteofficial.ScrapyBot;
import com.drosteofficial.commands.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopCommand extends Command {
    private final static Logger logger = LoggerFactory.getLogger(StopCommand.class);

    private final ScrapyBot scrapyBot;

    public StopCommand(IScrapyBot scrapyBot) {
        setName("stop");
        setDescription("Stops the music");
        setUsage("/stop");
        this.scrapyBot = (ScrapyBot) scrapyBot;
        this.setCommandData(Commands.slash("stop", "Stops the music"));
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member) {
        var manager = scrapyBot.getMusicManager(guild.getId());
        if (!manager.isPresent()){
            event.reply("Threre is no music player").queue();
            return;
        }
        var musicPlayer =  manager.get();
        if (manager.isEmpty()) {
            logger.warn("Music manager is empty for guild {}", guild.getId());
            event.reply("There is no music playing").queue();
            return;
        }

         musicPlayer.getAudioPlayer().setPaused(!musicPlayer.getAudioPlayer().isPaused());
        StringBuilder message = new StringBuilder(":stop_button: Music ");
        if (musicPlayer.getAudioPlayer().isPaused()) {
            message.append("paused");
        }
        else{
            message.append("resumed");
        }
         event.reply(message.toString()).queue();
    }
}
