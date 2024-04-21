package com.drosteofficial.commands.musicDiscordCommands;
import com.drosteofficial.IScrapyBot;
import com.drosteofficial.ScrapyBot;
import com.drosteofficial.Utilities.TImeUtilities;
import com.drosteofficial.commands.Command;
import com.drosteofficial.data.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;


public class SkipCommand extends Command {
    private final static Logger logger = LoggerFactory.getLogger(SkipCommand.class);

    private final ScrapyBot scrapyBot;

    public SkipCommand(IScrapyBot scrapyBot) {
        this.scrapyBot = Objects.requireNonNull((ScrapyBot) scrapyBot);

        this.setName("skip");
        this.setDescription("pomija aktualnie odtwarzany utwór");
        this.setUsage("/skip");
        this.setCommandData(Commands.slash("skip", "Skips current played track"));
    }


    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member) {
        MusicManager musicPlayer = null;

        var manager = scrapyBot.getMusicManager(guild.getId());
        if (manager.isEmpty()) {
            logger.warn("Music manager is empty for guild {}", guild.getId());
            musicPlayer = new MusicManager(scrapyBot.getAudioManager().createPlayer(), guild);
            musicPlayer.init();
            scrapyBot.getMusicManagers().add(guild.getId(), musicPlayer);
        }

        if (manager.isPresent()) {
            musicPlayer = manager.get();
        }

        var memberAudioChannel = event.getMember().getVoiceState().getChannel();

            if (musicPlayer.getCurrentTrack() == null) {
                event.reply(":face_with_monocle: There is nothing to skip").queue();
                return;
            }
            event.reply(":track_next:  music skiped").queue();
            musicPlayer.getAudioPlayer().stopTrack();
            musicPlayer.nextTrack();
            var currentTrack = musicPlayer.getCurrentTrack();


            var embed = new EmbedBuilder()
                    .setTitle("Następnie grane")
                    .setDescription(currentTrack.getInfo().title)
                    .setColor(Color.GREEN)
                    .addField("Kanał", event.getChannel().getName(), true)
                    .addField("Czas trwania", TImeUtilities.millisecondsToMinutesFormat(currentTrack.getDuration()), true)
                    .addField("Przewidywany czas odtworzenia utworu", "Teraz", true)
                    .addField("Pozycja w kolejne", "Teraz", true)
                    .setAuthor(currentTrack.getInfo().author, currentTrack.getInfo().uri, "https://cdn.discordapp.com/attachments/885206963598819360/927269255337087026/butelka.png")
                    .setTimestamp(LocalDateTime.now())
                    .build();
            event.replyEmbeds(embed).queue();

        }
    }
