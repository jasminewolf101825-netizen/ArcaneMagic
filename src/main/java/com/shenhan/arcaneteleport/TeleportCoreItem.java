package com.shenhan.arcaneteleport;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TeleportCoreItem extends Item {
    private static final Map<UUID, Anchor> ANCHORS = new ConcurrentHashMap<>();

    public TeleportCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (player == null || level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockPos clicked = context.getClickedPos();
        if (!level.getBlockState(clicked).is(Blocks.LODESTONE)) {
            return InteractionResult.PASS;
        }

        ANCHORS.put(
                player.getUUID(),
                new Anchor(level.dimension().identifier().toString(), clicked.immutable())
        );

        player.displayClientMessage(
                Component.literal("✦ 已綁定傳送錨點：" +
                        clicked.getX() + ", " + clicked.getY() + ", " + clicked.getZ())
                        .withStyle(ChatFormatting.AQUA),
                false
        );

        level.playSound(
                null,
                clicked,
                SoundEvents.RESPAWN_ANCHOR_SET_SPAWN,
                SoundSource.PLAYERS,
                1.0F,
                1.15F
        );

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Anchor anchor = ANCHORS.get(player.getUUID());
        if (anchor == null) {
            player.displayClientMessage(
                    Component.literal("尚未綁定錨點。請用傳送核心對磁石右鍵。")
                            .withStyle(ChatFormatting.RED),
                    false
            );
            return InteractionResult.FAIL;
        }

        String currentDimension = level.dimension().identifier().toString();
        if (!currentDimension.equals(anchor.dimension())) {
            player.displayClientMessage(
                    Component.literal("測試版只能傳送到同一維度的錨點。")
                            .withStyle(ChatFormatting.RED),
                    false
            );
            return InteractionResult.FAIL;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            BlockPos pos = anchor.pos();
            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS,
                    1.0F,
                    0.85F
            );

            serverPlayer.teleportTo(
                    pos.getX() + 0.5D,
                    pos.getY() + 1.0D,
                    pos.getZ() + 0.5D
            );

            level.playSound(
                    null,
                    pos.above(),
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.2F
            );

            player.getCooldowns().addCooldown(this.getDefaultInstance(), 60);
            player.displayClientMessage(
                    Component.literal("空間折疊完成。")
                            .withStyle(ChatFormatting.LIGHT_PURPLE),
                    true
            );
        }

        return InteractionResult.SUCCESS;
    }

    private record Anchor(String dimension, BlockPos pos) {
    }
}
