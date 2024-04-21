package com.drosteofficial.Utilities;

import com.drosteofficial.data.MusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.LocalDateTime;

public class Playlist {
    public static EmbedBuilder generatePlaylistEmbed(MusicManager manager, String title) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(title);
        stringBuilder.append(System.getProperty("line.separator"));

        int queueSize = manager.getQueue().size();

        if (manager.getCurrentTrack() != null) {
            var currentTrack = manager.getCurrentTrack();

            stringBuilder.append(System.lineSeparator());
            stringBuilder.append(":banjo: Aktualnie gram ");
            stringBuilder.append(currentTrack.getInfo().title);
            stringBuilder.append(" | :clock1: ");
            stringBuilder.append(TImeUtilities.millisecondsToMinutesFormat(currentTrack.getDuration()));
            stringBuilder.append(System.lineSeparator());
            stringBuilder.append(System.lineSeparator());
        }

        if (queueSize <= 0) {
            stringBuilder.append(":empty_nest: Nic tu nie ma :(");
            stringBuilder.append(System.getProperty("line.separator"));
            stringBuilder.append(System.getProperty("line.separator"));
            stringBuilder.append(":tent: Dodaj utwór poleceniem /play");
        }

        int i = 1 + queueSize;

        for (AudioTrack audioTrack : manager.getQueue()) {
            if (i <= 10 + queueSize) {
                stringBuilder.append(":notes:");
                stringBuilder.append(i);
                stringBuilder.append(".` ");
                stringBuilder.append(audioTrack.getInfo().title);
                stringBuilder.append("` | :clock10: ");
                stringBuilder.append(TImeUtilities.millisecondsToMinutesFormat(audioTrack.getDuration()));
                stringBuilder.append(System.getProperty("line.separator"));
                stringBuilder.append(System.getProperty("line.separator"));
            }

            manager.queue(audioTrack);
            i++;
        }
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Liczba utworów w playliście: ");
        stringBuilder.append(String.valueOf(manager.getQueue().size()));

        var embed = new EmbedBuilder()
                .setDescription(stringBuilder.toString())
                .setColor(Color.GREEN)
                .setThumbnail("https://img.youtube.com/vi/" + manager.getQueue().element().getIdentifier() + "/0.jpg")
                .setAuthor("Dodano do playlisty", "https://paulek.pro/", "https://cdn.pixabay.com/photo/2019/08/11/18/27/icon-4399630_1280.png")
                .setTimestamp(LocalDateTime.now())
                ;

        if (manager.getCurrentTrack() != null) {
            embed.setThumbnail("https://img.youtube.com/vi/" + manager.getCurrentTrack().getIdentifier() + "/0.jpg");
        }

        return embed;
    }
}
