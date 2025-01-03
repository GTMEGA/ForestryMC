/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 * 
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

public interface IForestryConstants {

	/**
	 * @return The villager ID for the Apiarist Villager.
	 */
    int getApicultureVillagerID();
	
	/**
	 * @return The villager ID for the Arborist Villager.
	 */
    int getArboricultureVillagerID();
	
	/**
	 * @return The ChestGenHooks key for adding items to the Forestry Villager chest.
	 */
    String getVillagerChestGenKey();
}
