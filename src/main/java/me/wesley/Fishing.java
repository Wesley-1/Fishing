package me.wesley;

import lombok.Getter;
import me.elapsed.universal.nms.NMS;
import me.elapsed.universal.nms.NMS_1_17_R3;
import me.elapsed.universal.objects.ItemBuilder;
import me.wesley.bait.Bait;
import me.wesley.bait.rewards.Reward;
import me.wesley.nms.EntityFishingHook;
import me.wesley.rod.types.Default;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Fishing extends JavaPlugin {

    @Getter
    private EntityFishingHook fishingHookNMS;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.fishingHookNMS = EntityFishingHook.create();
        Bukkit.getPluginManager().registerEvents(new Default(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
