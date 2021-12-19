package com.wildermods.wilderforge.launch.coremods;

import com.wildermods.wilderforge.api.modLoadingV1.CoremodInfo;
import com.wildermods.wilderforge.api.modLoadingV1.MissingCoremod;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public final class Coremods {

	private Coremods() {}
	
	public static CoremodInfo[] getAllCoremods() {
		System.out.println(CoremodInfo.class.getClassLoader());
		ModContainer[] modContainers = FabricLoader.getInstance().getAllMods().toArray(new ModContainer[]{});
		CoremodInfo[] coremods = new CoremodInfo[modContainers.length];
		for(int i = 0; i < modContainers.length; i++) {
			coremods[i] = new CoremodInfo(modContainers[i]);
		}
		return coremods;
	}
	
	public static CoremodInfo getCoremod(String modid) {
		ModContainer modContainer = FabricLoader.getInstance()
				.getModContainer(modid).orElse(null);
		
		return modContainer != null ? 
				new CoremodInfo(modContainer) : 
					new MissingCoremod();
	}
	
	public static int getCoremodCount() {
		return FabricLoader.getInstance().getAllMods().size();
	}
	
}
