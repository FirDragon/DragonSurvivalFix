package by.srinater.dragonsurvivalfix.mixin;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.DragonSkinReloadHandle;
import by.dragonsurvivalteam.dragonsurvival.client.skins.DragonSkins;
import by.srinater.dragonsurvivalfix.config.Config;
import by.srinater.dragonsurvivalfix.fixes.skin.DragonSkinsNewFields;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonSkinReloadHandle.class)
public class DragonSkinReloadHandleMixin {

    @Inject(remap = false, method = "onReloadEvent", at=@At("HEAD"), cancellable = true)
    private static void onReloadEvent(AddReloadListenerEvent reloadEvent, CallbackInfo ci)
    {
        if (Config.EnableFixSkinLoader)
        {
            DragonSkinsNewFields.initialized = false;
            DragonSkins.init();
            ci.cancel();
        }
    }
}
