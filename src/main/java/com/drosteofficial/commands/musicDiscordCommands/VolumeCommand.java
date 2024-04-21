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

public class VolumeCommand extends Command {
    private ScrapyBot scrapyBot;
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VolumeCommand.class);


    public VolumeCommand(IScrapyBot scrapyBot) {
        setName("volume");
        setDescription("Changes the volume of the music");
        setUsage("/volume <volume>");
        this.setCommandData(Commands.slash("volume", "Changes the volume of the music 0 - 100")
                .addOption(OptionType.INTEGER, "volume", "The volume to set", true));
        this.scrapyBot = (ScrapyBot) scrapyBot;
    }
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member) {

        if (event.getMember().getUser().isBot() == true) {
            event.reply("Bot's cant invoke commands").setEphemeral(true).queue();
            return;
        }
        if (!event.getMember().getVoiceState().inAudioChannel() ) {
            event.reply("You need to be in a voice channel to use this command").setEphemeral(true).queue();
            return;
        }

    try {

        var manager = scrapyBot.getMusicManager(guild.getId());
        var musicPlayer = manager.get();
        if (manager.isEmpty()) {
            logger.warn("Music manager is empty for guild {}", guild.getId());
            event.reply("There is no music playing").queue();
            return;
        }
        int volume = event.getOption("volume").getAsInt();
        StringBuilder  message = new StringBuilder(":loud_sound: Volume set to" + volume + "% ") ;
        if (volume < 0 ){
            message.append("Volume can't be lower than 0");
            volume = 0;
        }
        if (volume > 100){
            message.append("Volume can't be higher than 100") ;
            volume = 100;
        }
        musicPlayer.getAudioPlayer().setVolume(volume);
        event.reply(message.toString()).queue();

    }catch (Exception e){
        logger.error("Error while getting music manager", e);
        event.reply("An error occurred while getting music manager").setEphemeral(true).queue();
        return;
    }


    }
}
