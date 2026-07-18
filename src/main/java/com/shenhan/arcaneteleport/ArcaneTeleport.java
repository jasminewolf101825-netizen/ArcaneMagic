package com.shenhan.arcaneteleport;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public final class ArcaneTeleport implements ModInitializer {
    public static final String MOD_ID = "arcane_teleport";

    public static final Item TELEPORT_CORE = registerItem(
            "teleport_core",
            TeleportCoreItem::new,
            new Item.Properties().stacksTo(1)
    );

    private static <T extends Item> T registerItem(
            String name,
            java.util.function.Function<Item.Properties, T> factory,
            Item.Properties properties
    ) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(MOD_ID, name)
        );
        T item = factory.apply(properties.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(TELEPORT_CORE));
    }
}
