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

public class ClearQueueCommand extends Command {
    private ScrapyBot scrapyBot;
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ClearQueueCommand.class);

    public ClearQueueCommand(IScrapyBot scrapyBot) {
        setName("clearqueue");
        setDescription("clears the queue");
        setUsage("/clearqueue");
        var commandData = Commands.slash("clearqueue", "clears the queue");
        this.setCommandData(commandData);
    }
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member) {
        if (event.getMember().getUser().isBot() == true) {
            event.reply(":satellite: Bot's cant invoke commands").setEphemeral(true).queue();
        }
        if (!event.getMember().getVoiceState().inAudioChannel() ) {
            event.reply(":satellite: You need to be in a voice channel to use this command").setEphemeral(true).queue();
            return;
        }

        var manager = scrapyBot.getMusicManager(guild.getId());
        var musicPlayer = manager.get();
        if (manager.isEmpty()) {
            logger.warn("Music manager is empty for guild {}", guild.getId());
            event.reply(":satellite: There is no music playing").queue();
            return;
        }
        musicPlayer.removeAllTracks();
        event.reply("Queue cleared ").queue();
    }
}
