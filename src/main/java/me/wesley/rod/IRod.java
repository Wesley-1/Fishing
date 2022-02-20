package me.wesley.rod;

import lombok.Getter;
import me.elapsed.universal.commons.objects.Placeholder;
import me.elapsed.universal.commons.utils.TextUtility;
import me.wesley.Fishing;
import me.wesley.bait.Bait;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface IRod {


    /**
     *
     * @param event This event is activated when a fish is caught
     *
     */
    void onCatch(PlayerFishEvent event);

    /**
     *
     * @return This returns the name of the rod
     *
     */
    String getName();

    /**
     *
     * @return This will return the max level.
     */
    int getMaxLeveL();

    /**
     *
     * @param placeholders This will take in all the placeholders for the plugin.
     *
     * @return This will return the new item
     */
    ItemStack getRodItem(Placeholder... placeholders);


    /**
     *
     * @param instance This is an instance of the main class
     *
     * @return This will return the allowed bait
     */
    Bait getAllowedBait(Fishing instance);

    /**
     * This class is used to create the rods item.
     * This rod item will then be used later on and be adjusted.
     * This rod item will store all the enchants and things of that sort.
     */
    final class RodItemBuilder {

        @Getter private int catchTime;

        private String allowedBait;
        private String name;
        private List<String> lore;

        /**
         *
         * @param instance This is the plugin instance.
         *
         *  This constructor will setup the config for the rod.
         *
         */
        public RodItemBuilder(Fishing instance) {
            try {
                this.allowedBait = instance.getConfig().getString("Rod.allowedBait");
                this.catchTime = instance.getConfig().getInt("Rod.catchTime");
                this.name = instance.getConfig().getString("Rod.item.name");
                this.lore = instance.getConfig().getStringList("Rod.item.lore");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        /**
         *
         * @param placeholders This is where you would put all the placeholders
         * @return This will return the custom itemStack.
         *
         */
        public ItemStack getItem(Placeholder... placeholders) {
            ItemStack item = new ItemStack(Material.FISHING_ROD);
            ItemMeta meta = item.getItemMeta();

            {
                String title = TextUtility.colorize(this.name, placeholders);
                List<String> lore = TextUtility.colorize(this.lore, placeholders);
                meta.setDisplayName(title);
                meta.setLore(lore);
            }

            item.setItemMeta(meta);

            return item;
        }


        public Bait getAllowedBait() {
            return new Bait(Fishing.getPlugin(Fishing.class));
        }
    }

}
