package com.gmail.orangeandy2007.martensite.martensiteneo.feature;

import com.gmail.orangeandy2007.martensite.martensiteneo.classes.MessageCache;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent.Post;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MessageTick {
    private static final ArrayList<String> Messages = new ArrayList<>();
    public static boolean running = false;
    private static Player recipient = null;
    private static byte tickCount = 0;
    private static final byte rate = 10;
    private static final List<MessageCache> caches = new ArrayList<>();
    private static final Logger logger = LogUtils.getLogger();

    public static void sendMessage(ArrayList<String> messages, Player player){
        caches.add(new MessageCache(messages,player));
        if(!running) {
            NeoForge.EVENT_BUS.register(MessageTick.class);
        }else Status();
        running = true;
    }
    public static void stopMessage(){
        running = false;
        NeoForge.EVENT_BUS.unregister(MessageTick.class);
        MessageCache.id = 0;
    }
    private static void unpackCache(){
        Messages.addAll(caches.getFirst().messages);
        recipient = caches.getFirst().recipient;
        caches.removeFirst();
    }
    @SubscribeEvent
    public static void onServerTick(Post event){
        if(!running) return;
        if(caches.isEmpty() && Messages.isEmpty()) stopMessage();

        if(Messages.isEmpty() && !caches.isEmpty()) {
            unpackCache();
        }

        tickCount++;

        if(tickCount >= rate){
            recipient.displayClientMessage(Component.literal(Messages.getFirst()),false);
            Messages.removeFirst();
            tickCount = 0;
        }


    }
    public static void Status(){
        int current = caches.getFirst().CheckId();
        int last = caches.getLast().CheckId();
        logger.info("Current Status :");
        logger.info("Operating id : {}", current);
        logger.info("Last id : {}", last);

    }
}
