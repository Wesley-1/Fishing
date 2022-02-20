package me.wesley.bait;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.elapsed.universal.commons.objects.Placeholder;
import me.elapsed.universal.commons.utils.TextUtility;
import me.wesley.Fishing;
import me.wesley.bait.rewards.Reward;
import me.wesley.rod.IRod;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Bait {

    @Getter
    public static final HashMap<String, Bait> storedBaits = new HashMap<>();

  @Getter
  private String name;
  @Getter
  private ItemStack item;
  private List<String> rewardIdentifiers;
  @Getter
  private final HashMap<String, Reward> rewards = new HashMap<>();

    public Bait(Fishing instance) {
        try {
            for (String key : instance.getConfig().getConfigurationSection("Bait").getKeys(false)) {
                this.name = instance.getConfig().getString("Bait." + key + ".name");
                this.item = new BaitItemBuilder(instance).makeItem(key, new Placeholder("{type}", name));
                this.rewardIdentifiers = instance.getConfig().getStringList("Bait." + key + ".rewardIdentifiers");

                rewardIdentifiers.forEach(rewardKey ->
                        rewards.put(key, new Reward(rewardKey, instance)));
                storedBaits.put(key, this);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static class BaitItemBuilder {

        /**
         * These 3 variables are so I can make the item stack for the bait.
         */
        private String name;
        private List<String> lore;
        private Material material;
        private final HashMap<String, BaitItemBuilder> loadedBait = new HashMap<>();


        /**
         *
         * @param instance This is a param for instance of main class
         *
         */
        public BaitItemBuilder(Fishing instance) {
            try {
                for (String key : instance.getConfig().getConfigurationSection("Bait").getKeys(false)) {
                    this.material = Material.valueOf(instance.getConfig().getString("Bait." + key + ".item.material"));
                    this.name = instance.getConfig().getString("Bait." + key + ".item.name");
                    this.lore = instance.getConfig().getStringList("Bait." + key + ".item.lore");
                    loadedBait.put(key, this);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        public ItemStack makeItem(String key, Placeholder... placeholders) {
            ItemStack item = new ItemStack(loadedBait.get(key).material);
            ItemMeta meta = item.getItemMeta();

            {
                String title = TextUtility.colorize(loadedBait.get(key).name, placeholders);
                List<String> lore = TextUtility.colorize(loadedBait.get(key).lore, placeholders);
                meta.setDisplayName(title);
                meta.setLore(lore);
            }

            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setString("BaitType", key);

            item.setItemMeta(meta);

            return item;
        }

    }
}
