package de.dafuqs.spectrum.blocks.stonesetting_workshop;

import de.dafuqs.spectrum.items.CrystalGraceItem;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.id.incubus_core.be.IncubusBaseBE;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class StonesettingWorkshopBlockEntity extends IncubusBaseBE {

    public static final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);
    private static final int GRACE_SLOTS_END = 7, SHARD_SLOT = 8, TOOL_SLOT = 9, MIDNIGHT_SLOT = 10;

    public StonesettingWorkshopBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntityRegistry.STONESETTING_WORKSHOP, pos, state);
    }

    /**
     * Attempts to insert an item, returns whether that insertion was successful.
     */
    public boolean tryInsertItem(ItemStack stack, PlayerEntity player, Hand hand) {

        if (world == null || world.isClient())
            return false;

        var item = stack.getItem();
        var success = world.isClient();

        // Try to insert crystal graces in one of the first 8 slots
        if (item instanceof CrystalGraceItem) {

            success = tryInsertGrace(stack);
            if (success)
                clearHand(player, hand);

        }

        //Any crystal shards in the 9th slot?
        if (stack.isIn(SpectrumItemTags.GEMSTONE_SHARDS) && inventory.get(SHARD_SLOT).isEmpty()) {

            inventory.set(SHARD_SLOT, stack);
            stack.decrement(1);

            success = true;
        }

        //If this is what we are infusing, try to put it in the last slot
        if (item instanceof ToolItem || item instanceof ArmorItem && inventory.get(TOOL_SLOT).isEmpty()) {

            inventory.set(TOOL_SLOT, stack);
            clearHand(player, hand);

            success = true;
        }

        sync();

        return success;
    }

    /**
     * Oppositely, tries to extract items
     */

    private void clearHand(PlayerEntity player, Hand hand) {
        player.setStackInHand(hand, ItemStack.EMPTY);
    }

    private boolean tryInsertGrace(ItemStack stack) {
        for (int i = 0; i <= GRACE_SLOTS_END; i++) {

            if (!inventory.get(i).isEmpty())
                continue;

            inventory.set(i, stack);
            return true;
        }
        return false;
    }

    DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public void save(NbtCompound nbt) {
        super.save(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void load(NbtCompound nbt) {
        super.load(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public void saveClient(NbtCompound nbt) {
        save(nbt);
    }

    @Override
    public void loadClient(NbtCompound nbt) {
        load(nbt);
    }
}
