package com.gmail.orangeandy2007.martensite.martensiteneo.mixin.sound;

import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.FeatureConfiguration;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(method = "play", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;)V"), cancellable = true)
    public void noLog(SoundInstance p_sound, CallbackInfo ci){
        if(FeatureConfiguration.NoSoundPoolWarn.get()){
            ci.cancel();
        }
    }
}
