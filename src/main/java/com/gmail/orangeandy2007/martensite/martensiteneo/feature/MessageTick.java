package com.gmail.orangeandy2007.martensite.martensiteneo.feature;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent.Post;

import java.util.ArrayList;

public class MessageTick {
    private static final ArrayList<String> Messages = new ArrayList<>();
    public static boolean running = false;
    private static Player recipient = null;
    private static byte tickCount = 0;
    private static final byte rate = 10;
    private static int INDEX = 0;
    public static void sendMessage(ArrayList<String> messages, Player player){
        Messages.clear();
        Messages.addAll(messages);
        running = true;
        recipient = player;
        tickCount = 0;
        INDEX = 0;

        NeoForge.EVENT_BUS.register(MessageTick.class);
    }
    public static void stopMessage(){
        running = false;
        NeoForge.EVENT_BUS.unregister(MessageTick.class);
    }
    @SubscribeEvent
    public static void onPlayerTick(Post event){
        if(!(running && event.getEntity() instanceof Player CurrentPlayer && CurrentPlayer.is(recipient))) return;

        tickCount++;

        if(tickCount >= rate) {
            tickCount = 0;
            if(INDEX < Messages.size()) {
                CurrentPlayer.displayClientMessage(Component.literal(Messages.get(INDEX)), false);
                INDEX++;
            }else{
                stopMessage();
            }
        }
    }
}
