package me.gaminglounge.fishing;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;


public class amorstand implements Listener {

    private final Fishing fishing;
    public amorstand(Fishing fishing){
        this.fishing=fishing;
    }

    //Method to check if a block is watter or watter logged.
public static boolean isWet(Block b) {
    var t = b.getType();
        if(
            t == Material.KELP ||
            t == Material.KELP_PLANT ||
            t == Material.TALL_SEAGRASS ||
            t == Material.SEAGRASS ||
            t == Material.WATER
            )
        {
//        Bukkit.broadcast(Component.text("Kelp & others"));
        return true;
        }
//    Bukkit.broadcast(Component.text("Not Watter, but "+t.toString()));
    if(b.getBlockData() instanceof Waterlogged w) {
//        Bukkit.broadcast(Component.text("Watterlogable"));
        if(w.isWaterlogged()){
//            Bukkit.broadcast(Component.text("Watterloged"));
            return true;
        };
    }
    return false;
}


    @EventHandler
    public void Fished(PlayerFishEvent e){

        if (e.getCaught() instanceof Item ie){


            Location position_object = ie.getLocation();
            int y = position_object.getBlockY();
            position_object.setY(y);
            while (isWet(position_object.getBlock())){/*Checking if the block that the rod is standing in is watter or not, via the isWet method.*/
                position_object.add(0, 1, 0);
            }


            ItemStack loot = new ItemStack(ie.getItemStack());
            Component item_text = loot.displayName();
            ArrayList<Component> enchantmenttext = new ArrayList<>();
            if(loot.getItemMeta() instanceof EnchantmentStorageMeta meta ) {
                meta.getStoredEnchants().forEach((enchant,level)->{

                    enchantmenttext.add(enchant.displayName(level));

                });
            }
            else{
                loot.getEnchantments().forEach((enchant,level)->{

                    enchantmenttext.add(enchant.displayName(level));

                });
            }

            Component displaytext;

            if(!enchantmenttext.isEmpty()){
                displaytext = Component.join(JoinConfiguration.newlines(),item_text,Component.join(JoinConfiguration.newlines(),enchantmenttext));
            }
            else displaytext = item_text;

            Textdisplay(position_object,displaytext,loot);
        }
    }

    public void Textdisplay(Location location,Component text, ItemStack itemStack) {

        var mm = MiniMessage.miniMessage();
        TextDisplay display = (TextDisplay) location.getWorld().spawnEntity(location.add(0, 0.7, 0), EntityType.TEXT_DISPLAY);
        display.text(mm.deserialize("<reset><#11A80C><t></#11A80C>",Placeholder.component("t", text)));
        display.setBillboard(Display.Billboard.VERTICAL);
        ItemDisplay itemDisplay = (ItemDisplay) location.getWorld().spawnEntity(location.add(0, 0.7, 0), EntityType.ITEM_DISPLAY);
        itemDisplay.setItemStack(itemStack);
        itemDisplay.setTransformation(new Transformation(new Vector3f(),new Quaternionf(),new Vector3f(0.75f),new Quaternionf()));
        itemDisplay.setBillboard(Display.Billboard.VERTICAL);
        itemDisplay.setPersistent(false); //entity does not get saved to unloaded chunks
        display.setPersistent(false); //entity does not get saved to unloaded chunks



        Bukkit.getScheduler().runTaskLater(fishing, () -> {
            location.getWorld().playSound(location,Sound.ENTITY_PLAYER_LEVELUP,20,1);
            location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER,location.subtract(0.25,1,0.25),15, 0.5, 0.1, 0.5, 0);
        }, 0);


        Bukkit.getScheduler().runTaskLater(fishing,()->{
            itemDisplay.remove();
            display.remove();
        },150);
    }
}
