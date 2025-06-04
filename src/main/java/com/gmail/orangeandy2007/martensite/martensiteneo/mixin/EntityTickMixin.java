package com.gmail.orangeandy2007.martensite.martensiteneo.mixin;

import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.ProtectZoneConfiguration;
import com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces.nmEntityCache;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityTickMixin implements nmEntityCache {
    @Unique
    private Player martensiteNew$cachedNearestPlayer = null;
    @Unique
    private long martensiteNew$cachedNearestPlayerTime = 0;
    @Unique
    private double martensiteNew$cachedNearestPlayerDistSq = 128;
    @Unique
    private boolean martensiteNew$NowCancel = false;

    @Override
    public double martensiteNew$getCacheNearestPlayerDisSq() {
        return this.martensiteNew$cachedNearestPlayerDistSq;
    }

    @Override
    public void martensiteNew$setCacheNearestPlayer(Player player, long time, double distSq) {
        this.martensiteNew$cachedNearestPlayer = player;
        this.martensiteNew$cachedNearestPlayerTime = time;
        this.martensiteNew$cachedNearestPlayerDistSq = distSq;
    }

    @Override
    public long martensiteNew$getCacheTime() {
        return this.martensiteNew$cachedNearestPlayerTime;
    }

    @Override
    public Player martensiteNew$getCacheNearestPlayer() {
        return this.martensiteNew$cachedNearestPlayer;
    }

    @Override
    public void martensiteNew$setNowCancel(boolean boo) {
        this.martensiteNew$NowCancel = boo;
    }

    @Override
    public boolean martensiteNew$getNowCancel(){
        return this.martensiteNew$NowCancel;
    }
    @Inject(method = "baseTick", at = @At("HEAD"), cancellable = true)
    public void baseTick(CallbackInfo ci) {
        if ((Object) this instanceof Entity entity) {
            if (!(entity.getType().toString()).equals("minecraft:player") && this.martensiteNew$getNowCancel()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(CallbackInfo ci) {
        if ((Object) this instanceof Entity entity) {
            if (!(entity.getType()).toString().equals("minecraft:player") && this.martensiteNew$getNowCancel()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "push*", at = @At("HEAD"), cancellable = true)
    public void push(CallbackInfo ci) {
        double Distance = martensiteNew$getCacheNearestPlayerDisSq();
        double Radius = ProtectZoneConfiguration.RADIUS.get();
        if (Distance >= Radius && Math.random() < ((Distance - Radius)*0.8)/(128 - Radius)) ci.cancel();
    }
}