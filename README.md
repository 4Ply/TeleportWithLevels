# Development

### Upgrading

1. https://fabricmc.net/wiki/tutorial:migratemappings
2. https://fabricmc.net/develop/
3. `rm -rf .gradle/`
4. gradle --refresh-dependencies

### Notes
1. `gradle runServer` sometimes removes client-side build artifacts, causing Java compilation to fail. It's stupid. Use `gradle build` to fix.


### Future things

- Use https://github.com/marketplace/actions/publish-minecraft-mods to publish artifacts
