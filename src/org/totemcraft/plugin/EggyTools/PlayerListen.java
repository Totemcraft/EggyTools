package org.totemcraft.plugin.EggyTools;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListen implements Listener {
    public static String nowitem;
    @EventHandler
    public void onPlayerMove(EntityTargetLivingEntityEvent event){
        Bukkit.broadcastMessage(event.getEntityType().name()+" is locked target "+event.getTarget().toString());
        if (event.getEntityType() == EntityType.EXPERIENCE_ORB){

        }

    }
    @EventHandler
    public void ce(InventoryClickEvent event){
        if (event.getClickedInventory().getTitle()=="作弊选项"){
            event.setCancelled(true);
            Player pp = (Player)event.getWhoClicked();
            pp.updateInventory();
            pp.updateInventory();
            ItemStack it = event.getCurrentItem();
            ItemMeta im = it.getItemMeta();
            switch (im.getDisplayName()){
                case "生命回满":relife(pp);
                break;
                case "创造模式":becreate(pp);
                break;
                case "生存模式":benormal(pp);
                break;
            }
            return;
        }
        return;

    }
    public void relife(Player player){
        player.setHealth(20.0);
        player.setFoodLevel(20);
    }
    public void becreate(Player player){
        player.setGameMode(GameMode.CREATIVE);
    }
    public void benormal(Player player){
        player.setGameMode(GameMode.SURVIVAL);
    }

}
