package me.gaminglounge.fishing;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;


public class amorstand implements Listener {

    private Fishing fishing;
    public amorstand(Fishing fishing){
        this.fishing=fishing;
    }

    @EventHandler
    public void Fished(PlayerFishEvent e){

        if (e.getCaught() instanceof Item ie){


            Location position_object = ie.getLocation();
            while (position_object.getBlock().getType() == Material.WATER){
                position_object.add(0, 1, 0);
            }
            ItemStack loot = new ItemStack(ie.getItemStack());
            Component item_text = loot.displayName();
            ArrayList<Component> enchantmenttext = new ArrayList<>();
            loot.getEnchantments().forEach((enchant,level)->{

                enchantmenttext.add(enchant.displayName(level));

            });
            Textdisplay(position_object,Component.join(JoinConfiguration.newlines(),item_text,Component.join(JoinConfiguration.newlines(),enchantmenttext)),loot);
        }
    }

    public void Textdisplay(Location location,Component text, ItemStack itemStack) {

        TextDisplay display = (TextDisplay) location.getWorld().spawnEntity(location.add(0, 2, 0), EntityType.TEXT_DISPLAY);
        display.text(text);
        display.setBillboard(Display.Billboard.VERTICAL);
        ItemDisplay itemDisplay = (ItemDisplay) location.getWorld().spawnEntity(location.add(0, -1, 0), EntityType.ITEM_DISPLAY);
        itemDisplay.setItemStack(itemStack);
        itemDisplay.setBillboard(Display.Billboard.VERTICAL);

        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,location.getBlock().getLocation(),10, 0, 0, 0, 0, true);

        Bukkit.getScheduler().runTaskLater(fishing,()->{
            itemDisplay.remove();
            display.remove();
        },200);
    }
}
