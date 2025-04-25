package com.gmail.orangeandy2007.martensite.martensiteneo.mixin;

import com.gmail.orangeandy2007.martensite.martensiteneo.management.experienceData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.gmail.orangeandy2007.martensite.martensiteneo.configuration.FeatureConfiguration.CrampExp;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin extends Entity implements experienceData {
    public ExperienceOrbMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    @Shadow
    public int value;
    @Shadow
    private int count;

    @Override
    public int martensiteNeo$getCount() {
        return count;
    }

    @Inject(method = "canMerge(Lnet/minecraft/world/entity/ExperienceOrb;II)Z", at = @At("RETURN"), cancellable = true)
    private static void canMerge(ExperienceOrb p_147089_, int p_147090_, int p_147091_, CallbackInfoReturnable<Boolean> cir){
        if(CrampExp.get()) {
            cir.setReturnValue(!p_147089_.isRemoved() && (p_147089_.getId() - p_147090_) % 10 == 0 && p_147089_.value >= p_147091_);
        }
    }
    @Inject(method = "playerTouch", at = @At("HEAD"))
    public void playerTouch(Player entity, CallbackInfo ci){
        entity.takeXpDelay = 0;
    }
    @Inject(method = "award", at = @At("HEAD"), cancellable = true)
    private static void award(ServerLevel p_147083_, Vec3 p_147084_, int p_147085_, CallbackInfo ci) {
        if(CrampExp.get())ci.cancel();
    }

    @Inject(method = "getExperienceValue", at = @At("HEAD"), cancellable = true)
    private static void getExperienceValue(int expValue, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(expValue);
    }
}
