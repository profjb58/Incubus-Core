package net.id.incubus_core.mixin.entity;

import com.mojang.authlib.GameProfile;
import net.id.incubus_core.misc.WorthinessChecker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.id.incubus_core.misc.WorthinessChecker.CapeType;

@Mixin(PlayerEntity.class)
public abstract class NameDisplayMixin {

    @Shadow public abstract GameProfile getGameProfile();

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    public void textNameIntercept(CallbackInfoReturnable<Text> cir) {
        var profile = getGameProfile();
        var capeType = WorthinessChecker.getCapeType(profile.getId());
        if(capeType != CapeType.NONE) {
            cir.setReturnValue(Text.of(capeType.prefix + profile.getName()));
            cir.cancel();
        }
    }

    @Inject(method = "getEntityName", at = @At("HEAD"), cancellable = true)
    public void stringNameIntercept(CallbackInfoReturnable<String> cir) {
        var profile = getGameProfile();
        var capeType = WorthinessChecker.getCapeType(profile.getId());
        if(capeType != CapeType.NONE) {
            cir.setReturnValue(capeType.prefix + profile.getName());
            cir.cancel();
        }
    }
}
