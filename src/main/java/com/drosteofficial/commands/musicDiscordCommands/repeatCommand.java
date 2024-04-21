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

public class repeatCommand extends Command {
    private ScrapyBot scrapyBot;
    private final static Logger logger = LoggerFactory.getLogger(QuitCommand.class);

    public repeatCommand(IScrapyBot scrapyBot) {
        this.scrapyBot = (ScrapyBot) scrapyBot;
        this.setUsage("/repeat");
        this.setDescription("repeat the current track use once again to disable loop");
        this.setName("repeat");
        var commandData = Commands.slash("repeat", "repeat the current track");
        this.setCommandData(commandData);}


    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member) {

        var manager = scrapyBot.getMusicManager(guild.getId());
        var musicPlayer = manager.get();
        if (manager.isEmpty()) {
            logger.warn("Music manager is empty for guild {}", guild.getId());
            event.reply("There is no music playing").queue();
            return;
        }

        if( musicPlayer.getAudioPlayer().getPlayingTrack() == null){
            event.reply("There is no track playing").queue();
            return;
        }

            musicPlayer.setRepeat();

            if(!musicPlayer.isRepeat()){
                event.reply(":arrows_counterclockwise: Music is no longer looped ").queue();
                return;
            }else {
                event.reply(":arrows_counterclockwise: Music is now looped ").queue();
            }





    }
}
