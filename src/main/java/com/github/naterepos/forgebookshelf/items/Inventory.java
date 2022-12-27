package com.github.naterepos.forgebookshelf.items;

import com.github.naterepos.forgebookshelf.Internal;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class Inventory {

    private List<EnumeratedItem> items;

    @Internal public Inventory() {}

    public Inventory(ServerPlayerEntity player) {
        items = new ArrayList<>();

        for(int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack stack = player.inventory.mainInventory.get(i);
            if(!stack.isEmpty()) {
                items.add(new EnumeratedItem(i, EnumeratedItem.MAIN_INVENTORY_TYPE, stack));
            }
        }

        for(int i = 0; i < player.inventory.armorInventory.size(); i++) {
            ItemStack stack = player.inventory.armorInventory.get(i);
            if(!stack.isEmpty()) {
                items.add(new EnumeratedItem(i, EnumeratedItem.ARMOR_TYPE, stack));
            }
        }

        if(player.inventory.offHandInventory.size() > 0) {
            ItemStack stack = player.inventory.offHandInventory.get(0);
            if(!stack.isEmpty()) {
                items.add(new EnumeratedItem(0, EnumeratedItem.OFF_HAND_TYPE, stack));
            }
        }
    }

    public List<EnumeratedItem> getItems() {
        return items;
    }

    public void serialize(ServerPlayerEntity player) {
        List<EnumeratedItem> todo = new ArrayList<>();
        for(EnumeratedItem item : items) {
           if(item.isArmor()) {
               if(player.inventory.armorInventory.get(item.getSlot()).isEmpty()) {
                   player.inventory.armorInventory.set(item.getSlot(), item.toStack());
               } else {
                   todo.add(item);
               }
           } else if(item.isOffhand()) {
               if(player.inventory.offHandInventory.get(item.getSlot()).isEmpty()) {
                   player.inventory.offHandInventory.set(item.getSlot(), item.toStack());
               } else {
                   todo.add(item);
               }
           } else {
               if(player.inventory.mainInventory.get(item.getSlot()).isEmpty()) {
                   player.inventory.mainInventory.set(item.getSlot(), item.toStack());
               } else {
                   todo.add(item);
               }
           }
        }
        for(EnumeratedItem item : todo) {
            player.inventory.addItemStackToInventory(item.toStack());
        }
    }
}
