package com.wildermods.wilderforge.launch.services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;

import com.wildermods.wilderforge.api.versionV1.Version;
import com.wildermods.wilderforge.launch.exception.CoremodFormatError;
import com.wildermods.wilderforge.launch.exception.CoremodLinkageError;
import com.wildermods.wilderforge.launch.patch.LegacyPatch;

import net.fabricmc.loader.impl.FormattedException;
import net.fabricmc.loader.impl.game.GameProvider;
import net.fabricmc.loader.impl.game.GameProviderHelper;
import net.fabricmc.loader.impl.game.patch.GameTransformer;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.metadata.BuiltinModMetadata;
import net.fabricmc.loader.impl.util.Arguments;
import net.fabricmc.loader.impl.util.SystemProperties;

public class WildermythGameProvider implements GameProvider {

	private static final String[] ENTRYPOINTS = new String[]{"com.worldwalkergames.legacy.LegacyDesktop"};
	private static final HashSet<String> SENSITIVE_ARGS = new HashSet<String>(Arrays.asList(new String[] {}));
	
	private Arguments arguments;
	private String entrypoint;
	private Path launchDir;
	private Path libDir;
	private Path gameJar;
	private Version gameVersion;
	private final List<Path> miscGameLibraries = new ArrayList<>();
	
	private static final GameTransformer TRANSFORMER = new GameTransformer(new LegacyPatch());
	
	@Override
	public String getGameId() {
		return "wildermyth";
	}

	@Override
	public String getGameName() {
		return "Wildermyth";
	}

	@Override
	public String getRawGameVersion() {
		File versionFile = new File("./version.txt");
		try {
			if(versionFile.exists()) {
				return Version.getVersion(FileUtils.readFileToString(versionFile).split(" ")[0]).toString();
			}
		}
		catch(IOException e) {
			throw new CoremodLinkageError(e);
		}
		throw new CoremodFormatError("No version.txt detected for wildermyth!");
	}

	@Override
	public String getNormalizedGameVersion() {
		return getRawGameVersion();
	}

	@Override
	public Collection<BuiltinMod> getBuiltinMods() {
		BuiltinModMetadata.Builder metadata = new BuiltinModMetadata.Builder(getGameId(), getNormalizedGameVersion()).setName(getGameName());
		
		return Collections.singletonList(new BuiltinMod(gameJar, metadata.build()));
	}

	@Override
	public String getEntrypoint() {
		return entrypoint;
	}

	@Override
	public Path getLaunchDirectory() {
		if (arguments == null) {
			return Paths.get(".");
		}
		
		return getLaunchDirectory(arguments);
	}

	@Override
	public boolean isObfuscated() {
		return false;
	}

	@Override
	public boolean requiresUrlClassLoader() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean locateGame(FabricLauncher launcher, String[] args) {
		this.arguments = new Arguments();
		arguments.parse(args);
		
		Map<Path, ZipFile> zipFiles = new HashMap<>();
		
		try {
			String gameJarProperty = System.getProperty(SystemProperties.GAME_JAR_PATH);
			GameProviderHelper.FindResult result;
			if(gameJarProperty != null) {
				Path path = Paths.get(gameJarProperty);
				if (!Files.exists(path)) {
					throw new RuntimeException("Game jar configured through "+SystemProperties.GAME_JAR_PATH+" system property doesn't exist");
				}
				result = GameProviderHelper.findFirstClass(Collections.singletonList(path), zipFiles, ENTRYPOINTS);
			}
			else {
				result = GameProviderHelper.findFirstClass(launcher.getClassPath(), zipFiles, ENTRYPOINTS);
			}
			
			if(result == null) {
				return false;
			}
			
			entrypoint = result.className;
			gameJar = result.path;
			
		}
		finally {
			for(ZipFile f : zipFiles.values()) {
				try {
					f.close();
				}
				catch(IOException e) {
					//ignore
				}
			}
		}
		
		gameVersion = getGameVersion();
		
		processArgumentMap(arguments);
		
		locateFilesystemDependencies();
		
		return true;
		
	}

	private void locateFilesystemDependencies() {
		File lib = libDir.toFile();
		for(File dep : lib.listFiles()) {
			if(dep.getName().endsWith(".jar")) {
				miscGameLibraries.add(dep.toPath());
			}
			else {
				System.out.println("Skipping non-jar dependency " + dep);
			}
		}
		
		for(File dep : launchDir.toFile().listFiles()) {
			if(dep.getName().endsWith(".jar")) {
				if(dep.getName().equals("wildermyth.jar")) {
					System.out.println("Skipping wildermyth.jar");
				}
				else if (dep.getName().endsWith(".jar")) {
					miscGameLibraries.add(dep.toPath());
				}
			}
			else {
				System.out.println("Skipping non-jar file " + dep);
			}
		}
	}

	@Override
	public void initialize(FabricLauncher launcher) {
		TRANSFORMER.locateEntrypoints(launcher, gameJar);
	}

	@Override
	public GameTransformer getEntrypointTransformer() {
		return TRANSFORMER;
	}

	@Override
	public void unlockClassPath(FabricLauncher launcher) {
		launcher.addToClassPath(gameJar);
		
		for(Path lib : miscGameLibraries) {
			launcher.addToClassPath(lib);
		}
	}

	@Override
	public void launch(ClassLoader loader) {
		String targetClass = entrypoint;
		
		try {
			Class<?> c = loader.loadClass(targetClass);
			Method m = c.getMethod("main", String[].class);
			m.invoke(null, (Object) arguments.toArray());
		}
		catch(InvocationTargetException e) {
			throw new FormattedException("Wildermyth has crashed!", e.getCause());
		}
		catch(ReflectiveOperationException e) {
			throw new FormattedException("Failed to start Wildermyth", e);
		}
		
	}

	@Override
	public Arguments getArguments() {
		return arguments;
	}

	@Override
	public String[] getLaunchArguments(boolean sanitize) {
		if (arguments == null) return new String[0];

		String[] ret = arguments.toArray();
		if (!sanitize) return ret;

		int writeIdx = 0;

		for (int i = 0; i < ret.length; i++) {
			String arg = ret[i];

			if (i + 1 < ret.length
					&& arg.startsWith("--")
					&& SENSITIVE_ARGS.contains(arg.substring(2).toLowerCase(Locale.ENGLISH))) {
				i++; // skip value
			} else {
				ret[writeIdx++] = arg;
			}
		}

		if (writeIdx < ret.length) ret = Arrays.copyOf(ret, writeIdx);

		return ret;
	}
	
	private void processArgumentMap(Arguments arguments) {
		if (!arguments.containsKey("gameDir")) {
			arguments.put("gameDir", getLaunchDirectory(arguments).toAbsolutePath().normalize().toString());
		}
		
		launchDir = Path.of(arguments.get("gameDir"));
		System.out.println("Launch directory is " + launchDir);
		libDir = launchDir.resolve(Path.of("./lib"));
	}
	
	private static Path getLaunchDirectory(Arguments arguments) {
		return Paths.get(arguments.getOrDefault("gameDir", "."));
	}
	
	private Version getGameVersion() {
		File versionFile = new File("./version.txt");
		try {
			if(versionFile.exists()) {
				return Version.getVersion(FileUtils.readFileToString(versionFile).split(" ")[0]);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return Version.MISSING;
	}

}
