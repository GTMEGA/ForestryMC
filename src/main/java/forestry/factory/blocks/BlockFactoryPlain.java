/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.factory.blocks;

import forestry.core.blocks.BlockBase;
import forestry.factory.tiles.TileWorktable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static forestry.core.utils.InventoryUtil.dropItemStackFromInventory;

public class BlockFactoryPlain extends BlockBase<BlockFactoryPlainType> {
    // Please forgive me SirSengir but this was the only way to keep track of the last player to touch this
    private static final ThreadLocal<WeakReference<EntityPlayer>> PLAYER_REF = new ThreadLocal<>();

    public BlockFactoryPlain() {
        super();
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        // Just to capture the reference of the player who broke it,
        PLAYER_REF.set(new WeakReference<>(player));
        return super.removedByPlayer(world, player, x, y, z); // During this call, the block is set to air!
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (world.isRemote) {
            return;
        }

        tryDropAsWorktable(world, x, y, z, block, meta);
        super.breakBlock(world, x, y, z, block, meta);
    }


    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        // Don't drop the block normally if the block is a worktable
        if (meta == BlockFactoryPlainType.WORKTABLE.getMeta())
            return new ArrayList<>();
        return super.getDrops(world, x, y, z, meta, fortune);
    }


    private void tryDropAsWorktable(World world, int posX, int posY, int posZ, Block block, int blockMeta) {
        if (blockMeta != BlockFactoryPlainType.WORKTABLE.getMeta())
            return;

        // Creates the item stack that will always be dropped
        ItemStack itemStack = new ItemStack(this, 1, BlockFactoryPlainType.WORKTABLE.getMeta());


        addNbt: {
            // Only add the NBT if we can find the player, and they are sneaking!
            WeakReference<EntityPlayer> playerRef = PLAYER_REF.get();
            if (playerRef == null)
                break addNbt;
            EntityPlayer player = playerRef.get();
            if (player == null)
                break addNbt;
            if (!player.isSneaking())
                break addNbt;

            // Find the tile entity, and grab the NBT From it
            TileEntity tile = world.getTileEntity(posX, posY, posZ);
            if (!(tile instanceof TileWorktable))
                break addNbt;
            TileWorktable worktable = (TileWorktable) tile;
            worktable.writeToItemNBT(itemStack);
        }

        // Then drop the item stack, with or without the NBT appended
        dropItemStackFromInventory(itemStack, world, posX, posY, posZ);
    }
}
