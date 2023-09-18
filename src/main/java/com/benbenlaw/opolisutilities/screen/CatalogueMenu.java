package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.recipe.CatalogueRecipe;
import com.google.common.collect.Lists;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CatalogueMenu extends   AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private final Level level;
    private List<CatalogueRecipe> recipes = Lists.newArrayList();
    private CatalogueRecipe lastUsedRecipe = null;
    private int recipesSize = 0;
    private ItemStack input = ItemStack.EMPTY;
    private ItemStack lastNonAirInput = ItemStack.EMPTY;
    private ItemStack lastInput = ItemStack.EMPTY;
    long lastSoundTime;
    final Slot inputSlot;
    final Slot resultSlot;
    Runnable slotUpdateListener = () -> {
    };
    public final Container container = new SimpleContainer(1) {
        public void setChanged() {
            super.setChanged();
            CatalogueMenu.this.slotsChanged(this);
            CatalogueMenu.this.slotUpdateListener.run();
        }
    };
    public final ResultContainer resultContainer = new ResultContainer();

    public CatalogueMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public CatalogueMenu(int pContainerId, Inventory pPlayerInventory, final ContainerLevelAccess pAccess) {
        super(ModMenuTypes.CATALOGUE_MENU.get(), pContainerId);
        this.access = pAccess;
        this.level = pPlayerInventory.player.level();
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 26, 44));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 142, 56) {

            public boolean mayPlace(@NotNull ItemStack p_40362_) {
                return false;
            }

            @Override
            public boolean mayPickup(@NotNull Player pPlayer) {
                if(CatalogueMenu.this.selectedRecipeIndex.get() == -1 || recipes.isEmpty() || recipes.size() < CatalogueMenu.this.selectedRecipeIndex.get() || CatalogueMenu.this.container.getItem(0).getCount() < recipes.get(CatalogueMenu.this.selectedRecipeIndex.get()).itemInCount)
                    return false;

                return super.mayPickup(pPlayer);
            }

            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                CatalogueMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
                ItemStack itemstack = CatalogueMenu.this.inputSlot.remove(recipes.get(CatalogueMenu.this.selectedRecipeIndex.get()).itemInCount);
                if (!itemstack.isEmpty()) {
                    CatalogueMenu.this.setupResultSlot();
                }

                pAccess.execute((p_40364_, p_40365_) -> {
                    long l = p_40364_.getGameTime();
                    if (CatalogueMenu.this.lastSoundTime != l) {
                        p_40364_.playSound(null, p_40365_, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                        CatalogueMenu.this.lastSoundTime = l;
                    }

                });
                CatalogueMenu.this.slotsChanged(CatalogueMenu.this.container);
                super.onTake(player, stack);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(CatalogueMenu.this.inputSlot.getItem());
            }

        });

        for(int i = 0; i < 3; ++i) {
            for(int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(pPlayerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(pPlayerInventory, i, 8 + i * 18, 144));

        }

        this.addDataSlot(this.selectedRecipeIndex);
        this.selectedRecipeIndex.set(-1);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<CatalogueRecipe> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }

    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(this.access, pPlayer, ModBlocks.CATALOGUE.get());
    }

    public boolean clickMenuButton(@NotNull Player pPlayer, int pId) {
        if (this.isValidRecipeIndex(pId)) {
            this.selectedRecipeIndex.set(pId);
            this.setupResultSlot();
        }

        return true;
    }

    private boolean isValidRecipeIndex(int pRecipeIndex) {
        return pRecipeIndex >= 0 && pRecipeIndex < this.recipes.size();
    }

    public void slotsChanged(@NotNull Container pInventory) {
        ItemStack itemstack = this.inputSlot.getItem();
        this.setupRecipeList(pInventory, itemstack);
        this.input = itemstack.copy();

    }

    private void setupRecipeList(Container pContainer, ItemStack pStack) {
        this.recipes = new ArrayList<>();

        if(!this.input.is(pStack.getItem())) {
            if(!this.input.is(Items.AIR) && !this.input.is(lastInput.getItem()) && !this.lastInput.is(Items.AIR))
                this.selectedRecipeIndex.set(-1);
            if(!this.input.is(lastInput.getItem()))
                this.lastInput = this.input;
        }
        if (!pStack.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(CatalogueRecipe.Type.INSTANCE, pContainer, this.level);
            this.recipes = this.recipes.stream().filter((recipe) ->
                    container.getItem(0).getCount() >= recipe.itemInCount).toList();
        }
        if(this.recipesSize != this.recipes.size() && this.selectedRecipeIndex.get() != -1)
        {
            for(int i = 0; i < this.recipes.size(); i++){
                if(this.recipes.get(i) == this.lastUsedRecipe) {
                    this.selectedRecipeIndex.set(i);
                    break;
                }
            }
        }
        this.recipesSize = this.recipes.size();

        if(!this.input.is(pStack.getItem()) || pStack.getCount() != this.input.getCount()) {
            this.resultSlot.set(ItemStack.EMPTY);
            if(this.lastInput.is(Items.AIR))
                setupResultSlot();
        }
    }

    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            CatalogueRecipe catalogueRecipe = this.recipes.get(this.selectedRecipeIndex.get());
            this.resultContainer.setRecipeUsed(catalogueRecipe);
            this.lastUsedRecipe = catalogueRecipe;
            this.resultSlot.set(catalogueRecipe.assemble(this.container, this.level.registryAccess()));
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.lastNonAirInput = this.input;

        this.broadcastChanges();
    }

    public @NotNull MenuType<?> getType() {
        return ModMenuTypes.CATALOGUE_MENU.get();
    }

    public void registerUpdateListener(Runnable pListener) {
        this.slotUpdateListener = pListener;
    }

    public boolean canTakeItemForPickAll(@NotNull ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultContainer && super.canTakeItemForPickAll(pStack, pSlot);
    }

    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();
            if (pIndex == 1) {
                item.onCraftedBy(itemstack1, pPlayer.level(), pPlayer);
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (pIndex == 0) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.level.getRecipeManager().getRecipeFor(CatalogueRecipe.Type.INSTANCE, new SimpleContainer(itemstack1), this.level).isPresent()) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 2 && pIndex < 29) {
                if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 29 && pIndex < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
            this.broadcastChanges();
        }

        return itemstack;
    }

    public void removed(@NotNull Player pPlayer) {
        super.removed(pPlayer);
        this.selectedRecipeIndex.set(-1);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((p_40313_, p_40314_) -> {
            this.clearContainer(pPlayer, this.container);
        });
    }

}