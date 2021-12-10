package com.wildermods.wilderforge.launch;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.wildermods.wilderforge.launch.LoadStage.*;
import static com.wildermods.wilderforge.launch.coremods.WilderForge.EVENT_BUS;

import com.wildermods.wilderforge.api.modLoadingV1.event.PreInitializationEvent;
import com.wildermods.wilderforge.launch.coremods.Coremods;
import com.wildermods.wilderforge.launch.coremods.ModLauncher;
import com.wildermods.wilderforge.launch.coremods.WilderForge;
import com.wildermods.wilderforge.launch.logging.CrashInfo;
import com.worldwalkergames.legacy.LegacyDesktop;
import com.worldwalkergames.legacy.Version;

@InternalOnly
public final class Main {
	public static final Logger LOGGER = LogManager.getLogger(Main.class);
	@InternalOnly
	public static ReflectionsHelper reflectionsHelper;

	public static void main(String[] args) throws IOException {
		try {
			for(String s : args) {
				if(s.startsWith("modLauncherVersion:")) {
					ModLauncher.VERSION = s.replace("modLauncherVersion:", "");
				}
			}
			ClassLoader loader = checkClassloader();
			
			setupReflectionsHelper(loader);

			loadCoremods(loader);
			
			LoadStage.setLoadStage(PRE_INIT);
			EVENT_BUS.fire(new PreInitializationEvent());
			launchGame(args);
		}
		catch(Throwable t) {
			if(!(t instanceof OutOfMemoryError)) {
				new CrashInfo(t);
			}
			throw t;
		}
	}
	
	private static final ClassLoader checkClassloader() throws VerifyError {
		ClassLoader classloader = LegacyDesktop.class.getClassLoader();
		if(!(classloader.getClass().getName().equals("cpw.mods.modlauncher.TransformingClassLoader"))) { //Do not use instanceof or cast the classLoader, instanceof will always return false, and you will get ClassCastExceptions due to differing classLoaders
			Main.LOGGER.info("Classloader: " + LegacyDesktop.class.getClassLoader().getClass().getName());
			throw new VerifyError("Incorrect classloader. Mixins are not loaded. " + LegacyDesktop.class.getClassLoader());
		}
		Main.LOGGER.info("Correct classloader detected.");
		return classloader;
	}
	
	private static final void setupReflectionsHelper(ClassLoader classLoader) {
		reflectionsHelper = new ReflectionsHelper(classLoader);
	}
	
	private static final void loadCoremods(ClassLoader classLoader) {
		Coremods.loadCoremods(classLoader);
		WilderForge wf = (WilderForge) Coremods.getCoremod("wilderforge");
		Version.PATCHLINE = wf.getVersionString();
	}
	
	private static final void launchGame(String[] args) {
		LoadStage.setLoadStage(INIT);
		LegacyDesktop.main(args);
	}
	
	@InternalOnly
	public static ReflectionsHelper getReflectionsHelper() {
		return reflectionsHelper;
	}
	
}
