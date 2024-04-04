package com.drosteofficial.data;

import com.drosteofficial.ScrapyBot;
import com.drosteofficial.entity.DiscordMessage;
import com.drosteofficial.entity.MessageAction;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

public class DiscordMessageDataModel implements ISQLDataModel<DiscordMessage, String > {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DiscordMessageDataModel.class);
    private final ScrapyBot scrapyBot;
    private  final ExecutorService executorService = Executors.newSingleThreadExecutor();



    public DiscordMessageDataModel(ScrapyBot bot) {
        this.scrapyBot = Objects.requireNonNull(bot);
    }




    @Override
    public CompletableFuture<DiscordMessage> loadAsync(String s) {
        return null;
    }

    @Override
    public Optional<DiscordMessage> load(String s) {
        try (Connection connection = scrapyBot.getDatabaseConnection();
            var statement = connection.prepareStatement("SELECT * FROM discord_message WHERE message_id = ?")){
            statement.setString(1,s);
            var resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(deserializeData(resultSet));
            }
        }catch (Exception e){
            logger.error("Error loading data from database",e);
        }
        return Optional.empty();
    }

    @Override
    public Future<Boolean> createTable() {
        return executorService.submit(() -> {
            try (Connection connection = scrapyBot.getDatabaseConnection()) {
                var statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS discord_message (id INT PRIMARY KEY AUTO_INCREMENT, guildId VARCHAR(50), message_id VARCHAR(50), author_id VARCHAR(100), channel_id VARCHAR(50), content TEXT, author VARCHAR(50), timestamp TIMESTAMP, action VARCHAR(50))");
                statement.execute();
                return true;
            } catch (Exception e) {
                logger.error("Error creating table in database", e);
                return false;
            }
        });
    }
    @Override
    public Optional<DiscordMessage> load(int id) {
        try (Connection connection = scrapyBot.getDatabaseConnection();
             var statement = connection.prepareStatement("SELECT * FROM discord_message WHERE id = ?")){
            statement.setInt(1,id);
            var resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(deserializeData(resultSet));
            }
        }catch (Exception e){
            logger.error("Error loading data from database",e);
        }
        return Optional.empty();

    }

    @Override
    public Optional<Collection<DiscordMessage>> loadAll() {
            try (Connection connection = scrapyBot.getDatabaseConnection();
                 var statement = connection.prepareStatement("SELECT * FROM discord_message")){
                var result =statement.executeQuery();
                return Optional.of((Collection<DiscordMessage>) result);
            }catch (Exception e){
                logger.error("Error saving data to database",e);
            }
            return Optional.empty();

    }

    @Override
    public Future<Boolean> save(Collection<DiscordMessage> collection, boolean ignoreNotChanged) {
        return executorService.submit(() -> {
            for (var discordMessage : collection) {
                var state = save(discordMessage);
                try {
                    if (!state.isDone() || !state.get()) {
                        return false;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Error saving data to database", e);
                    return false;
                }
            }
            return true;
        });
    }

    @Override
    public Future<Boolean> save(DiscordMessage discordMessage) {
        return executorService.submit(() -> {
        try (Connection connection = scrapyBot.getDatabaseConnection();
             var statement = connection.prepareStatement("INSERT INTO discord_message (guildId, message_id, author_id, channel_id, content, author, timestamp, action) VALUES (?,?,?,?,?,?,?,?)")){
            statement.setString(1,discordMessage.getGuildId());
            statement.setString(2,discordMessage.getMessageId());
            statement.setString(3,discordMessage.getAuthorId());
            statement.setString(4,discordMessage.getChannelId());
            statement.setString(5,discordMessage.getContent());
            statement.setString(6,discordMessage.getAuthor());
            statement.setTimestamp(7,java.sql.Timestamp.from(discordMessage.getTimestamp()));
            statement.setString(8,discordMessage.getAction().name());
            statement.execute();
            return true;
        }catch (Exception e){
            logger.error("Error saving data to database",e);
        }
        return false;
        });
    }

    @Override
    public Future<Boolean> delete(String s) {
        return executorService.submit(() -> {
            try (Connection connection = scrapyBot.getDatabaseConnection();
                 var statement = connection.prepareStatement("DELETE FROM discord_message WHERE message_id = ?")){
                statement.setString(1,s);
                return true;
            }catch (Exception e){
                logger.error("Error deleting data to database",e);
            }
            return false;
        });
    }

    @Override
    public Future<Boolean> delete(int id) {
        return executorService.submit(() -> {
            try (Connection connection = scrapyBot.getDatabaseConnection();
                 var statement = connection.prepareStatement("DELETE FROM discord_message WHERE message_id = ?")){
                statement.setInt(1,id);
                return true;
            }catch (Exception e){
                logger.error("Error deleting data to database",e);
            }
            return false;
        });
    }

    @Override
    public Optional<Integer> count() {

        try (Connection connection = scrapyBot.getDatabaseConnection();
             var statement = connection.prepareStatement("SELECT COUNT(*) FROM discord_message")){
            statement.executeQuery();
            return statement.getResultSet().next() ? Optional.of(statement.getResultSet().getInt(1)) : Optional.empty();
        }catch (Exception e){
            logger.error("Error counting rows in database",e);
        }
        return Optional.empty();
    }

    @Override
    public DiscordMessage deserializeData(ResultSet resultSet) throws SQLException {
        return new DiscordMessage(resultSet.getInt("id"),
                resultSet.getString("guildId"),
                resultSet.getString("message_id"),
                resultSet.getString("author_id"),
                resultSet.getString("channel_id"),
                resultSet.getString("content"),
                resultSet.getString("author"),
                resultSet.getTimestamp("timestamp").toInstant(),
                MessageAction.valueOf(resultSet.getString("action")));

    }



}
