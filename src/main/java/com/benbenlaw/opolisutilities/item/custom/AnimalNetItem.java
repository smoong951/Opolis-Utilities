package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.event.ModEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;


public class AnimalNetItem extends Item {
    public AnimalNetItem(Properties properties) {
        super(properties);
    }



    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity livingEntity, @NotNull InteractionHand hand) {
        // NULL this is in the ModEvents class because of taming / right click interacting mobs
        return InteractionResult.FAIL;
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        assert player != null;
        ItemStack itemstack = player.getItemInHand(hand);
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        CompoundTag nbt = itemstack.getTag();

        assert nbt != null;

        if (!level.isClientSide()) {
            if (nbt.contains("entity")) {

                EntityType<?> type = EntityType.byString(itemstack.getTag().getString("entity")).orElse(null);

                if (type != null) {

                    // Spawn the mob
                    Entity entity = type.create(level);
                    assert entity != null;
                    entity.load(itemstack.getTag());
                    entity.absMoveTo(pos.relative(direction).getX() + 0.5, pos.relative(direction).getY(), pos.relative(direction).getZ() + 0.5, 0, 0);


                    nbt.remove("entity");
                    itemstack.setTag(null);

                    //TODO Fix Damage Values not being applied correctly
                    if (ConfigFile.animalNetTakesDamage.get().equals(true)) {
                        player.getItemInHand(hand).hurtAndBreak(1, player,
                                (damage) -> player.broadcastBreakEvent(player.getUsedItemHand()));
                    }

                    level.addFreshEntity(entity);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);


                } else {
                    player.sendSystemMessage(Component.translatable("tooltips.animal_net.no_mob").withStyle(ChatFormatting.RED));
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {

        if(Screen.hasShiftDown()) {

            if (stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("entity")) {

                CompoundTag nbt = stack.getTag();
                EntityType<?> entity = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(nbt.getString("entity")));
                components.add(Component.translatable("tooltips.animal_net.shift.held").withStyle(ChatFormatting.BLUE));
                components.add(Component.translatable(entity.toString()).withStyle(ChatFormatting.GREEN));

            }
            else {

                boolean hostileMobs = ConfigFile.animalNetHostileMobs.get();
                boolean waterMobs = ConfigFile.animalNetWaterMobs.get();
                boolean animalMobs = ConfigFile.animalNetAnimalMobs.get();
                boolean villagerMobs = ConfigFile.animalNetVillagerMobs.get();

                components.add(Component.translatable("tooltips.animal_net.mob_types").withStyle(ChatFormatting.BLUE));

                if (hostileMobs) {
                    components.add(Component.translatable("tooltips.animal_net.hostile_mobs").withStyle(ChatFormatting.GREEN));
                }
                if (waterMobs) {
                    components.add(Component.translatable("tooltips.animal_net.water_mobs").withStyle(ChatFormatting.GREEN));
                }
                if (animalMobs) {
                    components.add(Component.translatable("tooltips.animal_net.animal_mobs").withStyle(ChatFormatting.GREEN));
                }
                if (villagerMobs) {
                    components.add(Component.translatable("tooltips.animal_net.villager_mobs").withStyle(ChatFormatting.GREEN));
                }

                components.add(Component.translatable("tooltips.animal_net.no_mob").withStyle(ChatFormatting.RED));
            }
        }
        else {
            components.add(Component.translatable("tooltips.animal_net.hover.shift").withStyle(ChatFormatting.BLUE));

        }
        super.appendHoverText(stack, level, components, flag);
    }
}
