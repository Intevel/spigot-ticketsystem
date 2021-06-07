package me.exception.tickets.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullBuilder {

    ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    private Class<?> skullMetaClass;
    SkullMeta meta = (SkullMeta) this.i.getItemMeta();

    public SkullBuilder() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            this.skullMetaClass = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftMetaSkull");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public SkullBuilder setDisplayName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public SkullBuilder setLore(String... a) {
        this.meta.setLore(Arrays.asList(a));
        return this;
    }

    public SkullBuilder setOwner(String o) {
        this.meta.setOwner(o);
        return this;
    }

    public SkullBuilder setAmount(int amount) {
        this.i.setAmount(amount);
        return this;
    }

    public SkullBuilder setEnchantment(Enchantment e, int staerke) {
        this.i.addUnsafeEnchantment(e, staerke);
        return this;
    }

    public SkullBuilder setAttributs(ItemFlag flag) {
        this.meta.addItemFlags(new ItemFlag[]{flag});
        return this;
    }

    public SkullBuilder setOwnerFromURL(String url) {
        try {
            Field profileField = this.skullMetaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(this.meta, getProfile(url));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public ItemStack build() {
        this.i.setItemMeta((ItemMeta) this.meta);
        return this.i;
    }

    private GameProfile getProfile(String skinURL) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        String base64encoded = Base64.getEncoder().encodeToString((new String("{textures:{SKIN:{url:\"" + skinURL + "\"}}}")).getBytes());
        Property property = new Property("textures", base64encoded);
        profile.getProperties().put("textures", property);
        return profile;
    }
}

    