package com.kingtyphon.kaijucraft.keybinds;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;


public final class KaijuKeybinds {
    public static final KaijuKeybinds INSTANCE = new KaijuKeybinds();

    private KaijuKeybinds(){

    }
    private static final String CATEGORY = "key.categories." + KaijuCraft.MODID;

    public final KeyMapping kaijuGui = new KeyMapping(
            "key." + KaijuCraft.MODID + ".gui_key",
            KeyConflictContext.GUI,
            InputConstants.getKey(InputConstants.KEY_F, 1),
            CATEGORY
    );
}