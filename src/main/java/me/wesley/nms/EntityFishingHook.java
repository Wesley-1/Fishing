package me.wesley.nms;

import java.lang.reflect.Field;

import me.elapsed.universal.nms.NMS_1_8_R3;
import org.bukkit.Bukkit;
import org.bukkit.entity.FishHook;

public interface EntityFishingHook {

    String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName()
            .split("\\.")[3];

    static EntityFishingHook create() {
        switch (SERVER_VERSION) {
            case "v1_12_R1" -> {
                return new EntityFishingHook1_12_2();
            }
            case "v1_8_R3" -> {
                return new EntityFishingHook1_8_8();
            }
            default -> {
                return new EntityFishingHookProxy();
            }
        }
    }

    /**
     * @param target      the instance of the fishing hook we are trying to apply
     *                    to this is an object due to the fact that we could have
     *                    to use reflection to apply it or directly with the api.
     * @param minWaitTime Minimum number of ticks one has to wait for a fish
     *                    biting
     */
    void setMinWaitTime(Object target, int minWaitTime);


    /**
     * Proxy class that is designed around using the default spigot api over using
     * NMS.
     */
    final class EntityFishingHookProxy implements EntityFishingHook {

        @Override
        public void setMinWaitTime(Object target, int minWaitTime) {
            if (!(target instanceof FishHook)) {
                throw new IllegalArgumentException("Cannot cast target to FishHook");
            }
            FishHook hook = (FishHook) target;
            hook.setMinWaitTime(minWaitTime);
            hook.setMaxWaitTime(minWaitTime);
        }
    }

    /**
     * Uses reflection to modify the 'EntityFishingHook'.
     */

    final class EntityFishingHook1_8_8 implements EntityFishingHook {
        private static final String TIME_UNTIL_LURED_FIELD_NAME = "aw";
        private static Field TIME_UNTIL_LURED;

        static {
            try {
                var klass = Class.forName("net.minecraft.server.v1_8_R3.EntityFishingHook");
                TIME_UNTIL_LURED = klass.getDeclaredField(TIME_UNTIL_LURED_FIELD_NAME);
                TIME_UNTIL_LURED.setAccessible(true);
            } catch (NoSuchFieldException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void setMinWaitTime(Object target, int minWaitTime) {
            try {
                TIME_UNTIL_LURED.setInt(target, minWaitTime);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    final class EntityFishingHook1_12_2 implements EntityFishingHook {

        private static final String TIME_UNTIL_LURED_FIELD_NAME = "h";
        private static Field TIME_UNTIL_LURED;

        static {
            try {
                var klass = Class.forName("net.minecraft.server.v1_12_R1.EntityFishingHook");
                TIME_UNTIL_LURED = klass.getDeclaredField(TIME_UNTIL_LURED_FIELD_NAME);
                TIME_UNTIL_LURED.setAccessible(true);
            } catch (NoSuchFieldException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setMinWaitTime(Object target, int minWaitTime) {
            try {
                TIME_UNTIL_LURED.setInt(target, minWaitTime);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}