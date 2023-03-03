# Teleport with Levels

This Minecraft mod (Fabric) allows players to teleport using XP.
Teleportation cost is based on distance, and the cost scales exponentially over larger distances.

This mod needs to be installed on both the server and the clients in order to function properly.

Usage (in Minecraft) is simple. The command is `/xtp`, and follows the same rules as the normal 
`/tp` command but instead of requiring Op permissions, it allows any player to teleport as long
as they have sufficient XP.

For example, `/xtp ~ ~100 ~` will teleport the player 100 blocks up, based on their current location.

More examples:
```
# Teleport to X: 100, Y: 75, Z: 2000
/xtp 100 75 2000

# Teleport to a player named 4Ply
/xtp 4Ply

# Teleport 20 blocks down (try not to get squished)
/xtp ~ ~-20 ~
```


# Development

### Upgrading

1. Actually read https://fabricmc.net/wiki/tutorial:migratemappings, it has the upgrade steps.
2. `rm -rf .gradle/`
3. Open https://fabricmc.net/develop/, select the version to migrate to, and copy the Gradle command under “Mappings Migration”, for example gradlew migrateMappings --mappings "1.17.1+build.40". DO NOT modify your gradle.properties or build.gradle yet.
4. this should automatically update the versions in ./gradle.properties
   2. run `gradle migrateMappings --mappings "<value here from site>"` (either using the IDEA gradle runner, or by using `./gradlew`)
5. Updated source files should appear in ./remappedSrc. Copy the new code over the old sources.
6. `gradle --refresh-dependencies`
7. Generate the jar with `gradle remapJar` - it will appear in ./build/libs

### Notes
1. `gradle runServer` sometimes removes client-side build artifacts, causing Java compilation to fail. It's stupid. Use `gradle build` to fix.


### Future things

- Use https://github.com/marketplace/actions/publish-minecraft-mods to publish artifacts
