
package me.exception.tickets.utils;

import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.Color;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder {

    private ItemStack item;
    private List<String> lore;
    private ItemMeta meta;
    
    public ItemBuilder(final Material mat, final short subid, final int amount) {
        this.lore = new ArrayList<String>();
        this.item = new ItemStack(mat, amount, subid);
        this.meta = this.item.getItemMeta();
    }
    
    public ItemBuilder(final ItemStack item) {
        this.lore = new ArrayList<String>();
        this.item = item;
        this.meta = item.getItemMeta();
    }
    
    public ItemBuilder(final Material mat, final short subid) {
        this.lore = new ArrayList<String>();
        this.item = new ItemStack(mat, 1, subid);
        this.meta = this.item.getItemMeta();
    }
    
    public ItemBuilder(final Material mat, final int amount) {
        this.lore = new ArrayList<String>();
        this.item = new ItemStack(mat, amount, (short)0);
        this.meta = this.item.getItemMeta();
    }
    
    public ItemBuilder(final Material mat) {
        this.lore = new ArrayList<String>();
        this.item = new ItemStack(mat, 1, (short)0);
        this.meta = this.item.getItemMeta();
    }
    
    public ItemBuilder setAmount(final int value) {
        this.item.setAmount(value);
        return this;
    }
    
    public ItemBuilder setNoName() {
        this.meta.setDisplayName(" ");
        return this;
    }
    
    public ItemBuilder setGlow() {
        this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        return this;
    }
    
    public ItemBuilder setData(final short data) {
        this.item.setDurability(data);
        return this;
    }
    
    public ItemBuilder addLoreLine(final String line) {
        this.lore.add(line);
        return this;
    }
    
    public ItemBuilder addLoreArray(final String[] lines) {
        for (int x = 0; x < lines.length; ++x) {
            this.lore.add(lines[x]);
        }
        return this;
    }
    
    public ItemBuilder addLoreAll(final List<String> lines) {
        this.lore.addAll(lines);
        return this;
    }
    
    public ItemBuilder setDisplayName(final String name) {
        this.meta.setDisplayName(name);
        return this;
    }
    
    public ItemBuilder setSkullOwner(final String owner) {
        ((SkullMeta)this.meta).setOwner(owner);
        return this;
    }
    
    public ItemBuilder setColor(final Color c) {
        ((LeatherArmorMeta)this.meta).setColor(c);
        return this;
    }
    
    public ItemBuilder setBannerColor(final DyeColor c) {
        ((BannerMeta)this.meta).setBaseColor(c);
        return this;
    }
    
    public ItemBuilder setUnbreakable(final boolean value) {
        this.meta.spigot().setUnbreakable(value);
        return this;
    }
    
    public ItemBuilder addEnchantment(final Enchantment ench, final int lvl) {
        this.meta.addEnchant(ench, lvl, true);
        return this;
    }
    
    public ItemBuilder addItemFlag(final ItemFlag flag) {
        this.meta.addItemFlags(new ItemFlag[] { flag });
        return this;
    }
    
    public ItemBuilder addLeatherColor(final Color color) {
        ((LeatherArmorMeta)this.meta).setColor(color);
        return this;
    }
    
    public ItemStack build() {
        if (!this.lore.isEmpty()) {
            this.meta.setLore((List)this.lore);
        }
        this.item.setItemMeta(this.meta);
        return this.item;
    }
}
