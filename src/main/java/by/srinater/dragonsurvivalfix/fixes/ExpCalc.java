package by.srinater.dragonsurvivalfix.fixes;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ExpCalc {
    public static int CalcLevelExpPoint(Level pLevel, int level)
    {
        FakePlayer player = new FakePlayer(pLevel, new BlockPos(0,0,0), 0, new GameProfile(UUID.randomUUID(), "ExpCalc"));
        int totalPoint = 0;
        for (int i = 0 ; i < level; ++i)
        {
            int needPoint = player.getXpNeededForNextLevel();
            totalPoint += needPoint;
            player.giveExperiencePoints(needPoint);
        }
        player.giveExperiencePoints(-totalPoint);
        return totalPoint;
    }
}
