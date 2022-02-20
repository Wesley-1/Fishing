package me.wesley.rod.types;

import me.elapsed.universal.commons.objects.Placeholder;
import me.elapsed.universal.nms.NMS;
import me.wesley.Fishing;
import me.wesley.bait.Bait;
import me.wesley.rod.IRod;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Default implements IRod, @NotNull Listener {

    private final RodItemBuilder builder;
    private final Fishing instance;

    public Default() {
        this.instance = Fishing.getPlugin(Fishing.class);
        this.builder = new RodItemBuilder(Fishing.getPlugin(Fishing.class));
    }

    @Override @EventHandler
    public void onCatch(PlayerFishEvent event) {
        Bait bait = getAllowedBait(instance);

        if (event.getPlayer().getInventory().contains(bait.getItem())) {
            instance.getFishingHookNMS().setMinWaitTime(event.getHook(), builder.getCatchTime());
        }  else {
            event.setCancelled(true);
        }

        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            event.getPlayer().getInventory().removeItem(bait.getItem());
           // GIVE RANDOM REWARD
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().addItem(this.getRodItem(
                new Placeholder("{catch_time}", builder.getCatchTime()),
                new Placeholder("{allowed_bait}", getAllowedBait(instance).getName())));
        event.getPlayer().getInventory().addItem(getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem(), getAllowedBait(instance).getItem());
    }

    @Override
    public String getName() {
        return "Rod";
    }

    @Override
    public int getMaxLeveL() {
        return 10;
    }

    @Override
    public ItemStack getRodItem(Placeholder... placeholders) {
        return this.builder.getItem(placeholders);
    }

    @Override
    public Bait getAllowedBait(Fishing instance) {
        return this.builder.getAllowedBait();
    }
}
