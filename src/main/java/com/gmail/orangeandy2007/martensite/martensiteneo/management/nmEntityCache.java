package com.gmail.orangeandy2007.martensite.martensiteneo.management;

import net.minecraft.world.entity.player.Player;

public interface nmEntityCache {
    Player martensiteNew$getCacheNearestPlayer();

    void martensiteNew$setCacheNearestPlayer(Player player, long time, double disSq);

    long martensiteNew$getCacheTime();

    default double martensiteNew$getCacheNearestPlayerDisSq() {
        return 128;
    }

    void martensiteNew$setNowCancel(boolean boo);

    default boolean martensiteNew$getNowCancel() {return false;}
}
