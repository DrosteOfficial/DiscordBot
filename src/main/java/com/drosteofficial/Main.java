package com.drosteofficial;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.IOUtil;
import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.core.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.






public class Main {
    private final static Logger logger = LoggerFactory.getLogger("Main");
    public static void main(String[] args) {

        BasicConfigurator.configure();
        File settingsFile = new File("settings.yml");

        if (!settingsFile.exists()){
            logger.error("Settings file not found");
            var is =ClassLoader.getSystemResourceAsStream("settings.yml");
            assert is != null;
            var reader = new InputStreamReader(is);
            try {
                var created = settingsFile.createNewFile();
                if (created){
                   logger.info("Settings file created");
                }
            } catch (IOException exception){
                logger.error("Error reading settings file");
            }
            try (var os = new FileWriter(settingsFile)){
                IOUtils.copy(reader, os);

            } catch (FileNotFoundException exception){
                logger.error("Error reading settings file");
            } catch (IOException exception){
                logger.error("Error reading settings file");
            }
            logger.info("File created successfully");
        }

        ScrapyBot scrapyBot = new ScrapyBot();
        scrapyBot.Init();
        scrapyBot.Start();

    }




}