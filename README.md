# MinimumExample

1. Clone this repo
2. run `./gradlew build`
3. navigate to `build/libs`, and delete `wilderforge-0.0.0.0.jar`. Do not delete the fat jar.
4. add fabric loader 0.12.12 into `build/libs`
5. run `java -Dfabric.addMods=./wilderforge-0.0.0.0-all.jar -jar wilderforge-0.0.0.0-all.jar` inside of `build/libs`

6. notice this output:

```
gamebuster@gamebuster-Ryzen9-3900X-RTX-3080:~/Desktop/Modding/MinimumExample/build/libs$ java -Dfabric.addMods=./wilderforge-0.0.0.0-all.jar -jar wilderforge-0.0.0.0-all.jar
Launch directory is /home/gamebuster/Desktop/Modding/MinimumExample/build/libs
[17:42:31] [INFO] [FabricLoader/GameProvider]: Loading Wildermyth 0.0.0.0+example with Fabric Loader 0.12.12
entrypoint is com.wildermods.wilderforge.launch.Main
Main method is main([Ljava/lang/String;)V
[17:42:31] [INFO] [FabricLoader/]: Loading 4 mods:
	- fabricloader 0.12.12
	- java 11
	- wilderforge ${WILDERFORGE_VERSION}
	- wildermyth 0.0.0.0+example
[17:42:31] [WARN] [FabricLoader/Metadata]: Mod `wilderforge` (${WILDERFORGE_VERSION}) does not respect SemVer - comparison support is limited.
[17:42:31] [WARN] [FabricLoader/Metadata]: Mod `wildermyth` (0.0.0.0+example) uses more dot-separated version components than SemVer allows; support for this is currently not guaranteed.
[17:42:32] [INFO] [FabricLoader/Mixin]: SpongePowered MIXIN Subsystem Version=0.8.5 Source=file:/home/gamebuster/Desktop/Modding/MinimumExample/build/libs/wilderforge-0.0.0.0-all.jar Service=Knot/Fabric Env=CLIENT
Classloader is net.fabricmc.loader.impl.launch.knot.KnotClassLoader@7a4f0f29
net.fabricmc.loader.impl.launch.knot.KnotClassLoader@7a4f0f29

There are 0 coremods loaded

Classloader for FabricLoader.getInstance() is net.fabricmc.loader.impl.launch.knot.KnotClassLoader@7a4f0f29
Builtin coremod 'wildermyth' is null
Builtin coremod 'fabricloader' is null
Builtin coremod 'wilderforge' is null
Builtin coremod 'java' is null

net.fabricmc.loader.impl.launch.knot.KnotClassLoader@7a4f0f29

The classloader hierarchy is as follows: 
net.fabricmc.loader.impl.launch.knot.KnotClassLoader@7a4f0f29
net.fabricmc.loader.impl.launch.knot.KnotClassLoader$DynamicURLClassLoader@bebdb06
net.fabricmc.loader.impl.launch.knot.DummyClassLoader@2328c243
jdk.internal.loader.ClassLoaders$AppClassLoader@55054057

```
