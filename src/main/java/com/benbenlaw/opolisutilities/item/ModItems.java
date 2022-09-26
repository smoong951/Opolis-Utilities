package com.benbenlaw.opolisutilities.item;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.item.custom.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, OpolisUtilities.MOD_ID);

    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
            () -> new Item(new Item.Properties().tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> WOODEN_SHEARS = ITEMS.register("wooden_shears",
            () -> new ShearsItem(new Item.Properties().durability(96)
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> HOME_STONE = ITEMS.register("home_stone",
            () -> new HomeStoneItem(new Item.Properties().durability(32)
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> ADVANCED_HOME_STONE = ITEMS.register("advanced_home_stone",
            () -> new AdvancedHomeStoneItem(new Item.Properties().durability(32)
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> SUPER_HOME_STONE = ITEMS.register("super_home_stone",
            () -> new SuperHomeStoneItem(new Item.Properties().durability(32)
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> FLOATING_BLOCK = ITEMS.register("floating_block_item",
            () -> new FloatingBlockItem(new Item.Properties()
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> BUNDLED_FLESH = ITEMS.register("bundled_flesh",
            () -> new FloatingBlockItem(new Item.Properties()
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> ENDER_PEARL_FRAGMENT = ITEMS.register("ender_pearl_fragment",
            () -> new FloatingBlockItem(new Item.Properties()
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> JERKY = ITEMS.register("jerky",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(4).saturationMod(7.0F).build())
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));


//CORES + FRAGMENTS

    public static final RegistryObject<Item> EMPTY_CORE = ITEMS.register("empty_core",
            () -> new ResourceGeneratorCoreItem(new Item.Properties()
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> STONE_CORE = ITEMS.register("stone_core",
            () -> new ResourceGeneratorCoreItem(new Item.Properties().durability(8)
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));

    public static final RegistryObject<Item> STONE_FRAGMENT = ITEMS.register("stone_fragment",
            () -> new ResourceGeneratorCoreItem(new Item.Properties()
                    .tab(ModCreativeModTab.OPOLIS_UTILITIES)));








    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }



}
