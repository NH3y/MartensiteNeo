package com.gmail.orangeandy2007.martensite.martensiteneo.mixin.sound;

import com.gmail.orangeandy2007.martensite.martensiteneo.configuration.FeatureConfiguration;
import com.mojang.blaze3d.audio.Channel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.mojang.blaze3d.audio.Library$CountingChannelPool")
public abstract class LibraryMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"), method = "acquire", cancellable = true)
    private void acquire(CallbackInfoReturnable<Channel> cir){
        if(FeatureConfiguration.NoSoundPoolWarn.get()){
            cir.setReturnValue(null);
        }
    }
}
