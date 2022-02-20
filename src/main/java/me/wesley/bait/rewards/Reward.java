package me.wesley.bait.rewards;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.elapsed.universal.commons.objects.Placeholder;
import me.elapsed.universal.commons.utils.TextUtility;
import me.elapsed.universal.objects.ItemBuilder;
import me.wesley.Fishing;
import me.wesley.rod.IRod;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Reward {

    @Getter private String rewardIdentifier;
    private Type rewardType;

    public Reward(String identifier, Fishing instance) {
        this.rewardIdentifier = identifier;
        this.rewardType = Type.valueOf(instance.getConfig().getString("Rewards." + identifier + ".type"));
    }

    public void giveReward(Player player, Fishing instance) {
        switch (this.rewardType) {

            case COMMAND -> {
                List<String> commands = new CommandBuilder(instance).pickCommands();
                for(String s : commands){
                    s = s.replaceAll("%player%", player.getName());
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s);
                    System.out.println(s);
                }
            }
            case FISH -> player.getInventory().addItem(new FishBuilder(this, instance).makeItem(this.rewardIdentifier));
        }
    }

    final class CommandBuilder {

        private final int commandCount;
        private final List<String> commandList;

        CommandBuilder(Fishing instance) {
                this.commandCount = instance.getConfig().getInt("commands.count");
                this.commandList = instance.getConfig().getStringList("commands.list");
        }

        public ArrayList<String> pickCommands() {
            ArrayList<String> pickedCommands = new ArrayList<>();
            while(pickedCommands.size() < this.commandCount){
                Random random = new Random();
                random.setSeed(System.currentTimeMillis());
                int num = random.nextInt(this.commandList.size());
                String pick = this.commandList.get(num);
                if(!pickedCommands.contains(pick)){
                    pickedCommands.add(pick);
                }
            }
            return pickedCommands;
        }
    }

    /**
     * This class is called to build the fish.
     */

    final class FishBuilder {

        private int min, max;
        private String name;
        private List<String> lore;
        private Material material;

        private final HashMap<String, FishBuilder> loadedFish = new HashMap<>();

        private FishBuilder(Reward reward, Fishing instance) {
            try {
                for (String key : instance.getConfig().getConfigurationSection("Rewards." + reward.rewardIdentifier + ".fish").getKeys(false)) {
                    this.min = instance.getConfig().getInt("fish." + key + ".min");
                    this.max = instance.getConfig().getInt("fish." + key + ".max");
                    this.name = instance.getConfig().getString("fish." + key + ".item.name");
                    this.lore = instance.getConfig().getStringList("fish." + key + ".item.lore");
                    this.material = Material.valueOf(instance.getConfig().getString("fish." + key + ".item.material"));
                    loadedFish.put(key, this);
                }
            } catch (Exception ignored) {

            }
        }

        public ItemStack makeItem(String key) {
            ItemStack item = new ItemStack(loadedFish.get(key).material);
            ItemMeta meta = item.getItemMeta();
            int randomValue = new Random().nextInt(this.max + 1 - this.min) + this.min;

            {
                String title = TextUtility.colorize(loadedFish.get(key).name, new Placeholder("{value}", randomValue));
                List<String> lore = TextUtility.colorize(loadedFish.get(key).lore, new Placeholder("{value}", randomValue));
                meta.setDisplayName(title);
                meta.setLore(lore);
            }

            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setInteger("FishValue", randomValue);

            item.setItemMeta(meta);

            return item;
        }
    }
}
