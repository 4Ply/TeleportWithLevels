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
