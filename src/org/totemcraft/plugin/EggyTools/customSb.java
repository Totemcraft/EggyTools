package org.totemcraft.plugin.EggyTools;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import java.util.List;

public class customSb extends EntityZombie {
    public customSb(World world){
        super(((CraftWorld)world).getHandle());
        List goalB = (List)in.getPrivateField("b",PathfinderGoalSelector.class,goalSelector);goalB.clear();
        List goalC = (List)in.getPrivateField("c",PathfinderGoalSelector.class,goalSelector);goalC.clear();
        List targetB = (List)in.getPrivateField("b",PathfinderGoalSelector.class,targetSelector);targetB.clear();
        List targetC = (List)in.getPrivateField("b",PathfinderGoalSelector.class,targetSelector);targetC.clear();

        this.goalSelector.a(0,new PathfinderGoalFloat(this));
        //this.goalSelector.a(2,new PathfinderGoalMeleeAttack(EntityHuman.class, 1.0D, false));

    }
}
