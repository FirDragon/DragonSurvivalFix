package by.srinater.dragonsurvivalfix.mixin;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.magic.DragonAbilities;
import by.dragonsurvivalteam.dragonsurvival.magic.common.DragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.passive.PassiveDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.network.magic.SyncSkillLevelChangeCost;
import by.srinater.dragonsurvivalfix.config.Config;
import by.srinater.dragonsurvivalfix.fixes.ExpCalc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(SyncSkillLevelChangeCost.class)
public class SyncSkillLevelChangeCostMixin{
    @Inject(remap = false, method = "handle(Lby/dragonsurvivalteam/dragonsurvival/network/magic/SyncSkillLevelChangeCost;Ljava/util/function/Supplier;)V", at= @At("HEAD"), cancellable = true)
    private void handle(SyncSkillLevelChangeCost message, Supplier<NetworkEvent.Context> supplier, CallbackInfo ci)
    {
        if (!Config.EnableFixSkillDowngradeBug)
            return;

        ISyncSkillLevelChangeCostAccessor messageAccessor = (ISyncSkillLevelChangeCostAccessor)message;
        NetworkEvent.Context context = supplier.get();
        ServerPlayer sender = context.getSender();

        if (sender != null) {
            context.enqueueWork(() -> DragonStateProvider.getCap(sender).ifPresent(handler -> {
                DragonAbility staticAbility = DragonAbilities.ABILITY_LOOKUP.get(messageAccessor.getSkill());

                if (staticAbility instanceof PassiveDragonAbility ability) {
                    PassiveDragonAbility playerAbility = DragonAbilities.getSelfAbility(sender, ability.getClass());
                    int levelCost = messageAccessor.getLevelChange() > 0 ? -playerAbility.getLevelCost(messageAccessor.getLevelChange()) : Math.max((int) (playerAbility.getLevelCost() * 0.8F), 1);

                    if (levelCost != 0 && !sender.isCreative()) {
                        if (levelCost > 0)
                            sender.giveExperiencePoints(ExpCalc.CalcLevelExpPoint(sender.level(), levelCost));
                        else
                            sender.giveExperienceLevels(levelCost);;
                    }
                }
            }));
        }

        context.setPacketHandled(true);
        ci.cancel();
    }
}
