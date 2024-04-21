package com.drosteofficial.commands.musicDiscordCommands;
import com.drosteofficial.IScrapyBot;
import com.drosteofficial.ScrapyBot;
import com.drosteofficial.Utilities.Playlist;
import com.drosteofficial.commands.Command;
import com.drosteofficial.data.MusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jdk.jshell.spi.ExecutionControl;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class QueueCommand extends Command {
    private ScrapyBot scrapyBot;
    private final static Logger logger = LoggerFactory.getLogger(QuitCommand.class);

    public QueueCommand(IScrapyBot ScrapyBot) {
        this.scrapyBot = (ScrapyBot) ScrapyBot;
        this.setUsage("/play <link do muzyki>");
        this.setDescription("Display the current queue");
        this.setName("queue");
        var commandData = Commands.slash("queue", "Displays the current music queue");
        commandData.addOption(OptionType.STRING, "url", "URL to music source", true);
        this.setCommandData(commandData);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, TextChannel channel, Guild guild, Member member) {


            MusicManager musicPlayer;
            if (!event.getMember().getVoiceState().inAudioChannel()) {
                event.reply(":satellite: Muszisz być na jakimś kanale, abym mógł dołączyć do niego").queue();
                return;
            }
            var manager = scrapyBot.getMusicManager(guild.getId());
            if (manager.isEmpty()) {
                logger.warn("Music manager is empty for guild {}", guild.getId());
                musicPlayer = new MusicManager(scrapyBot.getAudioManager().createPlayer(), guild);
                musicPlayer.init();
                scrapyBot.getMusicManagers().add(guild.getId(), musicPlayer);
                event.replyEmbeds(Playlist.generatePlaylistEmbed(musicPlayer, ":arrow_lower_left: Co gram :arrow_lower_right:").build()).queue();
                return;
            }

             musicPlayer = manager.get();

            scrapyBot.getAudioPlayerManager().loadItemOrdered(event.getGuild(), Objects.requireNonNull(event.getOption("url")).getAsString(), new AudioLoadResultHandler() {


                        @Override
                        public void trackLoaded(AudioTrack audioTrack) {

                            var audioChannel = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();

                            if (!guild.getAudioManager().isConnected()) {
                                guild.getAudioManager().openAudioConnection(audioChannel);
                                musicPlayer.setAudioChannel(audioChannel);
                            }

                            musicPlayer.queue(audioTrack);
                        }

                        @Override
                        public void playlistLoaded(AudioPlaylist audioPlaylist) {
                        }

                        @Override
                        public void noMatches() {
                        }

                        @Override
                        public void loadFailed(FriendlyException e) {
                        }
                    }

            );

            event.reply(":eject: Queue is set").queue();
            event.replyEmbeds(Playlist.generatePlaylistEmbed(musicPlayer, ":arrow_lower_left: Co gram :arrow_lower_right:").build()).queue();
        }
    }


