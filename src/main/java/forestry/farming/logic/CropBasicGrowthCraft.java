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
package forestry.farming.logic;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import forestry.core.config.Constants;
import forestry.core.proxy.Proxies;
import forestry.core.utils.vect.IVect;
import forestry.core.utils.vect.Vect;

public class CropBasicGrowthCraft extends Crop {

	private final Block block;
	private final int meta;
	private final boolean isRice;
	private final boolean isGrape;

	public CropBasicGrowthCraft(World world, Block block, int meta, Vect position, boolean isRice, boolean isGrape) {
		super(world, position);
		this.block = block;
		this.meta = meta;
		this.isRice = isRice;
		this.isGrape = isGrape;
	}

	@Override
	protected boolean isCrop(IVect pos) {
		return getBlock(pos) == block && getBlockMeta(pos) == meta;
	}

	@Override
	protected Collection<ItemStack> harvestBlock(IVect pos) {
		ArrayList<ItemStack> harvest = block.getDrops(world, pos.getX(), pos.getY(), pos.getZ(), meta, 0);
		if (harvest.size() > 1) {
			harvest.remove(0); //Hops have rope as first drop.
		}
		Proxies.common.addBlockDestroyEffects(world, pos.getX(), pos.getY(), pos.getZ(), block, 0);
		if (isGrape) {
			world.setBlockToAir(pos.getX(), pos.getY(), pos.getZ());

		} else {
			world.setBlockMetadataWithNotify(pos.getX(), pos.getY(), pos.getZ(), 0, Constants.FLAG_BLOCK_SYNCH);
		}

		if (isRice) {
			world.setBlockMetadataWithNotify(pos.getX(), pos.getY() - 1, pos.getZ(), 7, Constants.FLAG_BLOCK_SYNCH);
		}

		return harvest;
	}

	@Override
	public String toString() {
		return String.format("CropBasicGrowthCraft [ position: [ %s ]; block: %s; meta: %s ]", position.toString(), block.getUnlocalizedName(), meta);
	}
}
