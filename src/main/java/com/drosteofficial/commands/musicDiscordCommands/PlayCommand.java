package com.drosteofficial.commands.musicDiscordCommands;

import com.drosteofficial.IScrapyBot;
import com.drosteofficial.ScrapyBot;
import com.drosteofficial.Utilities.TImeUtilities;
import com.drosteofficial.commands.Command;
import com.drosteofficial.data.MusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class PlayCommand extends Command {
    private final static Logger logger = LoggerFactory.getLogger(PlayCommand.class);

    private final ScrapyBot scrapyBot;

    public PlayCommand(IScrapyBot scrapyBot) {
        this.setName("play");
        this.setDescription("pozwala wlaczyc muzyke na kanale glosowym");
        this.setUsage("/play <link do muzyki>");
        var commandData = Commands.slash("play", "Plays music on voice channels");
        commandData.addOption(OptionType.STRING, "url", "URL to music source", true);
        this.setCommandData(commandData);
        this.scrapyBot = (ScrapyBot) scrapyBot;
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

            var musicManagerCopy = musicPlayer;
            MusicManager finalMusicPlayer = musicPlayer;
            scrapyBot.getAudioPlayerManager().loadItemOrdered(event.getGuild(), Objects.requireNonNull(event.getOption("url")).getAsString(), new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack audioTrack) {
                    var audioChannel = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();
                    if (!event.getMember().getVoiceState().inAudioChannel()) {
                        event.reply(":satellite: Muszisz być na jakimś kanale, abym mógł dołączyć do niego").queue();
                        return;
                    }

                    if (!guild.getAudioManager().isConnected()) {
                        guild.getAudioManager().openAudioConnection(audioChannel);
                        finalMusicPlayer.setAudioChannel(audioChannel);
                    }

                    var playedIn = "Teraz";
                    if (TImeUtilities.playlistTime(musicManagerCopy) >= 30000L) {
                        playedIn = TImeUtilities.calculateTimeToPlayTrack(musicManagerCopy);
                    }

                    musicManagerCopy.queue(audioTrack);
                    var embed = new EmbedBuilder()
                            .setDescription(audioTrack.getInfo().title)
                            .setThumbnail("https://img.youtube.com/vi/" + audioTrack.getIdentifier() + "/0.jpg")
                            .setColor(Color.GREEN)
                            .addField("Kanał", audioChannel.getName(), true)
                            .addField("Czas trwania", TImeUtilities.millisecondsToMinutesFormat(audioTrack.getDuration()), true)
                            .addField("Przewidywany czas odtworzenia utworu", playedIn, true)
                            .addField("Pozycja w kolejne", String.valueOf(musicManagerCopy.getQueue().size()), true)
                            .setAuthor("Teraz gram", audioTrack.getInfo().uri, "https://cdn.pixabay.com/photo/2019/08/11/18/27/icon-4399630_1280.png")
                            .setTimestamp(LocalDateTime.now())
                            .build();
                    event.replyEmbeds(embed).queue();
                }

                @Override
                public void playlistLoaded(AudioPlaylist audioPlaylist) {
                    var audioChannel = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();
                    if (!Objects.requireNonNull(event.getMember().getVoiceState()).inAudioChannel()) {
                        event.reply(":satellite: Muszisz być na jakimś kanale, abym mógł dołączyć do niego").queue();
                        return;
                    }
                    if (!guild.getAudioManager().isConnected()) {
                        guild.getAudioManager().openAudioConnection(audioChannel);
                    }

                    audioPlaylist.getTracks().forEach(musicManagerCopy::queue);

                    event.reply(":satellite: Dodano do kolejki").queue();
                }

                @Override
                public void noMatches() {
                    event.reply(":dragon: Nie znalazłęm dopasowań do podanego linku").queue();
                }

                @Override
                public void loadFailed(FriendlyException e) {
                    event.reply(":screwdriver: Wystąpił nieznany błąd podczas wyszukiwania muzyki").queue();
                    }
            });
    }


}

