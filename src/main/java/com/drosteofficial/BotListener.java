package com.drosteofficial;

import com.drosteofficial.commands.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Objects;


public class BotListener extends ListenerAdapter {
    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(BotListener.class);
    private final ScrapyBot scrapyBot;

    public BotListener(ScrapyBot scrapyBot) {
        this.scrapyBot = scrapyBot;
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).isOwner()) {
            return;
        }

        if (!event.getMessage().getContentDisplay().toLowerCase().contains("update splash commands")) {
            return;
        }
        logger.info(String.format("Updating splash commands on %s command used by %s", event.getGuild().getName(), event.getMember().getNickname()));

        scrapyBot.getCommandManager().getCommandMap().values().forEach(command -> {
            event.getGuild().upsertCommand(command.getCommandData()).queue();
            logger.debug("Added " + command.getName() + " command to guild " + event.getGuild().getName());
        });
        event.getGuild().updateCommands().queue();

        event.getMessage().reply(":wink:").queue();
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        TextChannel textChannel =  (TextChannel) event.getGuild().getChannels().get(1);

        var embed = new EmbedBuilder()
                .setTitle("**Welcome**") // Bold text
                .setAuthor("*ScrapyBot*") // Italic text
                .addField(":)", "*It's nice to have you here *"+ event.getUser().getName(), true)
                .setImage("https://cdn.pixabay.com/photo/2023/04/03/04/49/ai-generated-7895957_640.jpg")
                .setTimestamp(event.getMember().getTimeJoined())
                .build();


            textChannel.sendMessageEmbeds(embed).queue();
    }

}

//    @Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//
//        TextChannel txtChannel = event.getGuild().getTextChannels().get(1);
//
//        if (event.getAuthor().isBot()) {
//            return;
//        }

//        var embed = new EmbedBuilder()
//                .setTitle("Tytuł")
//                .setAuthor("Adrian")
//                .addField("text", " :)", true)
//                .addField("aa", event.getAuthor().getName(), true)
//                .build();
//
//        txtChannel.sendMessageEmbeds(embed).queue();
//
//
////        if(event.getMessage().getContentRaw().equals("!dziala?")){
////
////            event.getMessage().reply("NIE").queue();
////
////        }
////
////        if (event.getMessage().getContentRaw().equalsIgnoreCase("nie działa")){
////            event.getMessage().delete().queue();
////        }
//    }



//    @Override
//    public void onMessageUpdate(MessageUpdateEvent messageUpdateEvent){
//        if (messageUpdateEvent.getMessage().getContentRaw().equalsIgnoreCase("nie działa")){
//            messageUpdateEvent.getMessage().delete().queue();
//        }
//    }

