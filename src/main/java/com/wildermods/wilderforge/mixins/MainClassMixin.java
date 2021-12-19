package com.wildermods.wilderforge.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import com.wildermods.wilderforge.launch.Main;
import com.wildermods.wilderforge.launch.coremods.Coremods;

import net.fabricmc.loader.api.FabricLoader;

@Mixin(value = Main.class, remap = false)
public class MainClassMixin {

	@Inject(
		at = @At(
			value = "HEAD"
		),
		method = "main([Ljava/lang/String;)V",
		require = 1
	)
	private static void injectMain(String[] args, CallbackInfo c) {
		System.out.println("Classloader is " + Main.class.getClassLoader());
		System.out.println("There are " + Coremods.getAllCoremods().length + " coremods loaded");
		System.out.println("Classloader for FabricLoader.getInstance() is " + FabricLoader.getInstance().getClass().getClassLoader());
		System.out.println("Builtin coremod 'wildermyth' is " + FabricLoader.getInstance().getModContainer("wildermyth").orElse(null));
		System.out.println("Builtin coremod 'fabricloader' is " + FabricLoader.getInstance().getModContainer("fabricloader").orElse(null));
		System.out.println("Builtin coremod 'wilderforge' is " + FabricLoader.getInstance().getModContainer("wilderforge").orElse(null));
		System.out.println("Builtin coremod 'java' is " + FabricLoader.getInstance().getModContainer("java").orElse(null));
	}
	
}
