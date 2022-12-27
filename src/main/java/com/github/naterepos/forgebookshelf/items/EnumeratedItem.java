package com.github.naterepos.forgebookshelf.items;

import com.github.naterepos.forgebookshelf.Internal;
import com.github.naterepos.forgebookshelf.utility.ItemUtilities;
import net.minecraft.item.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class EnumeratedItem {

    public static final transient int MAIN_INVENTORY_TYPE = 0;
    public static final transient int ARMOR_TYPE = -1;
    public static final transient int OFF_HAND_TYPE = -2;
    private int slot;
    private int type;
    private String item;

    @Internal public EnumeratedItem() {}

    public EnumeratedItem(int slot, int type, ItemStack stack) {
        this.slot = slot;
        this.type = type;
        this.item = ItemUtilities.serialize(stack);
    }

    public ItemStack toStack() {
        return ItemUtilities.deserialize(item);
    }

    public int getSlot() {
        return slot;
    }

    public boolean isMainInventory() {
        return type == MAIN_INVENTORY_TYPE;
    }

    public boolean isArmor() {
        return type == ARMOR_TYPE;
    }

    public boolean isOffhand() {
        return type == OFF_HAND_TYPE;
    }
}
