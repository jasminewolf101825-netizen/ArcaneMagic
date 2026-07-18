package com.jasminewolf.arcanemagic;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArcaneMagic implements ModInitializer {
    public static final String MOD_ID = "arcanemagic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("ArcaneMagic has awakened.");
    }
}
