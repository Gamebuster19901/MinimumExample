package com.wildermods.wilderforge.launch;

import net.fabricmc.loader.api.FabricLoader;

public final class Main {

	public static int x = 0;
	
	public static void main(String[] args) {
		System.out.println(FabricLoader.getInstance().getClass().getClassLoader());
		
		System.out.println("The classloader hierarchy is as follows: ");
		printClassLoaderHierarchy(FabricLoader.getInstance().getClass().getClassLoader());
	}
	
	public static void printClassLoaderHierarchy(ClassLoader classloader) {
		ClassLoader currentLoader = classloader;
		while(currentLoader != null) {
			System.out.println(currentLoader);
			currentLoader = currentLoader.getParent();
		}
	}
	
}
