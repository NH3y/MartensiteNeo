package com.gmail.orangeandy2007.martensite.martensiteneo.classes;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class MessageCache {
    public static int id = 0;
    private final int ID;
    public final ArrayList<String> messages;
    public final Player recipient;

    public MessageCache(ArrayList<String> toSend, Player player){
        messages = new ArrayList<>();
        messages.addAll(toSend);
        recipient = player;
        ID = id;
        id++;
    }

    public int CheckId(){
        return this.ID;
    }
}
