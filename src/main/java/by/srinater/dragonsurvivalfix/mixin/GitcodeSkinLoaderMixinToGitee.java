package by.srinater.dragonsurvivalfix.mixin;

import by.dragonsurvivalteam.dragonsurvival.client.skins.GitcodeSkinLoader;
import by.dragonsurvivalteam.dragonsurvival.client.skins.NetSkinLoader;
import by.dragonsurvivalteam.dragonsurvival.client.skins.SkinObject;
import by.srinater.dragonsurvivalfix.config.Config;
import by.srinater.dragonsurvivalfix.fixes.skin.loader.GiteeSkinLoader;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@Mixin(GitcodeSkinLoader.class)
public class GitcodeSkinLoaderMixinToGitee{
    @Unique
    protected GiteeSkinLoader dragonSurvivalFix$newSkinLoader = new GiteeSkinLoader();

    @Inject(remap = false, method = "querySkinList", at = @At("HEAD"), cancellable = true)
    public void querySkinList(CallbackInfoReturnable<Collection<SkinObject>> cir)
    {
        if (Config.EnableFixSkinLoader)
            cir.setReturnValue(dragonSurvivalFix$newSkinLoader.querySkinList());
    }
    @Inject(remap = false, method = "querySkinImage", at = @At("HEAD"), cancellable = true)
    public void querySkinImage(SkinObject skin, CallbackInfoReturnable<InputStream> cir) throws IOException {
        if (Config.EnableFixSkinLoader)
            cir.setReturnValue(dragonSurvivalFix$newSkinLoader.querySkinImage(skin));
    }
    @Inject(remap = false, method = "ping", at = @At("HEAD"), cancellable = true)
    public void ping(CallbackInfoReturnable<Boolean> cir) {
        if (Config.EnableFixSkinLoader)
            cir.setReturnValue(dragonSurvivalFix$newSkinLoader.ping());
    }
}
