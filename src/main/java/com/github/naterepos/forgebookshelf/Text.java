package com.github.naterepos.forgebookshelf;

import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;

public class Text {

    public static StringTextComponent of(String message) {
        return new StringTextComponent(message.replace("&", "§") + "§r");
    }

    public static StringTextComponent of(String message, Object... placeholders) {
        if(placeholders.length % 2 != 0) {
            return of(message);
        } else {
            for(int i = 0; i < placeholders.length; i += 2) {
                message = message.replace("%" + placeholders[i].toString() + "%", placeholders[i + 1].toString());
            }
        }
        return of(message);
    }

    public static StringNBT nbt(String message) {
        return StringNBT.valueOf(message.replace("&", "§") + "§r");
    }
}
