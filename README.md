# MinimumExample

1. Clone this repo
2. run `./gradlew build`
3. navigate to `build/libs`, and delete `wilderforge-0.0.0.0.jar`. Do not delete the fat jar.
4. add fabric loader 0.12.12 into `build/libs`
5. run `java -Dfabric.addMods=./wilderforge-0.0.0.0-all.jar -jar wilderforge-0.0.0.0-all.jar` inside of `build/libs`

6. notice this output:

```
Launch directory is /home/gamebuster/Desktop/Modding/WilderForge4/build/libs
[03:56:19] [INFO] [FabricLoader/GameProvider]: Loading Wildermyth 0.0.0.0+example with Fabric Loader 0.12.12
entrypoint is com.wildermods.wilderforge.launch.Main
Main method is main([Ljava/lang/String;)V
[03:56:20] [INFO] [FabricLoader/]: Loading 4 mods:
	- fabricloader 0.12.12
	- java 11
	- wilderforge ${WILDERFORGE_VERSION}
	- wildermyth 0.0.0.0+example
[03:56:20] [WARN] [FabricLoader/Metadata]: Mod `wilderforge` (${WILDERFORGE_VERSION}) does not respect SemVer - comparison support is limited.
[03:56:20] [WARN] [FabricLoader/Metadata]: Mod `wildermyth` (0.0.0.0+example) uses more dot-separated version components than SemVer allows; support for this is currently not guaranteed.
[03:56:20] [INFO] [FabricLoader/Mixin]: SpongePowered MIXIN Subsystem Version=0.8.5 Source=file:/home/gamebuster/Desktop/Modding/WilderForge4/build/libs/wilderforge-0.0.0.0-all.jar Service=Knot/Fabric Env=CLIENT
Classloader is net.fabricmc.loader.impl.launch.knot.KnotClassLoader@7a4f0f29
net.fabricmc.loader.impl.launch.knot.KnotClassLoader@7a4f0f29
There are 0 coremods loaded
Classloader for FabricLoader.getInstance() is net.fabricmc.loader.impl.launch.knot.KnotClassLoader@7a4f0f29
Builtin coremod 'wildermyth' is null
Builtin coremod 'fabricloader' is null
Builtin coremod 'wilderforge' is null
Builtin coremod 'java' is null
test

```
