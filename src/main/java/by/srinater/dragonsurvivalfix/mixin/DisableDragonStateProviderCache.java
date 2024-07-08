package by.srinater.dragonsurvivalfix.mixin;

import by.dragonsurvivalteam.dragonsurvival.common.capability.Capabilities;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.srinater.dragonsurvivalfix.config.Config;
import by.srinater.dragonsurvivalfix.fixes.DragonStateProviderProxy;
import com.ibm.icu.impl.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DragonStateProvider.class)
public class DisableDragonStateProviderCache {
    @Inject(remap=false, method = "getCap", at=@At("HEAD"), cancellable = true)
    private static void getCap(Entity entity, CallbackInfoReturnable<LazyOptional<DragonStateHandler>> cir)
    {
        if (!Config.DisableDragonStateCache)
            return;
        cir.setReturnValue(DragonStateProviderProxy.getCap(entity));
    }
}
