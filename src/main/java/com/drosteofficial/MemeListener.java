package com.drosteofficial;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MemeListener extends ListenerAdapter {
    private List<String> memeChannelsRegistered;

    public MemeListener() {
        this.memeChannelsRegistered = new ArrayList<>();
        this.memeChannelsRegistered.add("1221230022459654266");



    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

    if (!memeChannelsRegistered.contains(event.getChannel().getId())) {
        return;
    }

    for (Message.Attachment attachment : event.getMessage().getAttachments())
    {
        if (attachment.isImage() || attachment.isVideo())
        {
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F970")).queue();
        }
    }


    }



}
