package com.dguner.auspolbot;

import com.dguner.auspolbot.configuration.TwitterConfiguration;
import com.dguner.auspolbot.db.repositories.BillRepository;
import com.dguner.auspolbot.listeners.BillListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuspolbotApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuspolbotApplication.class, args);
    }

    @Bean
    public CommandLineRunner billListenerStarter(BillRepository billRepository, TwitterConfiguration twitterConfiguration) {
        return (args) -> {
            Thread billListenerThread = new Thread(new BillListener(billRepository, twitterConfiguration));
            billListenerThread.start();
            billListenerThread.join();
        };
    }
}
