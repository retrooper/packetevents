!!!!! v2.13.0

# PacketEvents 2.13.0 is finally out! 🎉

This minor release includes mainly support for Minecraft 26.2, but also includes a few smaller fixes and improvements.

Recent donors: **@PebbleHost**\
If you intend on donating and do not wish to be mentioned, please add such a note on your donation.

# Announcement 📢

Visit our website: https://packetevents.com/ \
Documentation: https://docs.packetevents.com/ \
Javadoc: https://javadocs.packetevents.com/

Thanks to all contributors who helped make this release possible:
- @booky10
- @Jan1k1
- @linsaftw
- @rafi67000
- @retrooper
- @Rothes
- @TheFaser

## What's Changed

* Improve chunk reading performance ([#1526](https://github.com/retrooper/packetevents/pull/1526))
* Add PluginMessage Common Wrapper ([#1523](https://github.com/retrooper/packetevents/pull/1523))
* Fix custom world clock use in dimension types ([`7c4dd0b`](https://github.com/retrooper/packetevents/commit/7c4dd0b039e695e5ae7d9c0562082f913cbb2677))
* Add support for 26.2 ([#1529](https://github.com/retrooper/packetevents/pull/1529), [`471a9e2`](https://github.com/retrooper/packetevents/commit/471a9e21144f0ea56e4053bda0477cca8402dc69), [`b1bbbae`](https://github.com/retrooper/packetevents/commit/b1bbbaed2f7bd8e09ff758bfa02f9b63df21c3b1))
* Update Gradle, Gradle plugins and actions ([#1533](https://github.com/retrooper/packetevents/pull/1533), [#1534](https://github.com/retrooper/packetevents/pull/1534))
* Fix nullability annotations for ProtcolManager/PlayerManager ([#1535](https://github.com/retrooper/packetevents/pull/1535))
* Ignore udp listeners during injection ([#1537](https://github.com/retrooper/packetevents/pull/1537))

**View Full Changelog**: [`v2.12.2...v2.13.0`](https://github.com/retrooper/packetevents/compare/v2.12.2...v2.13.0)

!!!!! v2.12.2
# PacketEvents 2.12.2 is finally out! 🎉

This patch release contains a few critical bug fixes as well as some optimizations and reworks.

Recent donors: **@PebbleHost, @perlsol, @TRGReal**\
If you intend on donating and do not wish to be mentioned, please add such a note on your donation.

Unfortunately, the PacketEvents PayPal account is no longer active. I hope to resolve any concerns with PayPal, who has frozen funds and activity on the account. If you want to donate, please use [GitHub Sponsors](https://github.com/sponsors/retrooper) or the newly setup [BuyMeACoffee](https://buymeacoffee.com/retrooper).

Thanks to all contributors who helped make this release possible:
- @booky10
- @Beaness
- @SamB440
- @retrooper
- @LeonTG

## What's Changed
* Rework logging system in PacketEvents (now more consistently used across API) [`a1067f5`](https://github.com/retrooper/packetevents/commit/a1067f5e4f53bcd7fbeaa19fb4d8046a3f6248ca)
* Optimize SpigotReflectionUtil.generateEntityId() method [`95afd3f`](https://github.com/retrooper/packetevents/commit/95afd3fce8bfbf7087233a3bd31e0705cd1e4df5)
* Fix transmission/copying of packet wrappers when dealing with multiple packet listeners for the following wrappers: WrapperLoginServerLoginSuccess, WrapperPlayServerDeclareCommands, WrapperPlayServerMapData, WrapperPlayServerPlayerRotation, WrapperPlayServerSpawnWeatherEntity [`20d2bd3`](https://github.com/retrooper/packetevents/commit/20d2bd34326ea9fab602ff07333fcfc713ae6ced)
* Allow for the customization of chat message length limit for legacy Minecraft versions [`a1067f5`](https://github.com/retrooper/packetevents/commit/a1067f5e4f53bcd7fbeaa19fb4d8046a3f6248ca)
* Multiple fixes for WrapperPlayServerSoundEffect [`66d768d`](https://github.com/retrooper/packetevents/commit/66d768d00f6f69c71a9ba27998976a6d2b5020f7), [`73603f8`](https://github.com/retrooper/packetevents/commit/73603f89b7aafab6ff7eed7be488b03fa5e064dc)
* Fix incorrect swapping of pitch and yaw for WrapperPlayServerSpawnEntity on Minecraft versions lower than 1.15 [`0634827`](https://github.com/retrooper/packetevents/commit/063482716d0fd858bef8e65b9c4714c8e1fd4aa9)
* Fix injection cleanup issues on Velocity ([#1489](https://github.com/retrooper/packetevents/issues/1489), [`cb8fb8b`](https://github.com/retrooper/packetevents/commit/cb8fb8b9268f7fb83f2a931432757d7d36ae220b))

**View Full Changelog**: [`v2.12.1...v2.12.2`](https://github.com/retrooper/packetevents/compare/v2.12.1...v2.12.2)

!!!!! v2.12.1

# PacketEvents 2.12.1 is finally out! 🎉

This patch release includes mainly bugfixes related to Minecraft 26.1, but also includes a few smaller features.

Recent donors: **PebbleHost, JustUsBuilds, matsu1213**\
If you intend on donating and do not wish to be mentioned, please add such a note on your donation.

# Announcement 📢

Visit our website: https://packetevents.com/ \
Documentation: https://docs.packetevents.com/ \
Javadoc: https://javadocs.packetevents.com/

Thanks to all contributors which helped make this release possible:
- @Beaness
- @booky10
- @ieatglu3
- @ManInMyVan
- @rafi67000
- @retrooper
- @roggy666
- @vadim-soude
- @Vrganj
- @ytnoos

## What's Changed

* Fix minor issues related to 26.1 ([#1483](https://github.com/retrooper/packetevents/pull/1483), [`f2974ec`](https://github.com/retrooper/packetevents/commit/f2974eca7c919ca60266016339991ea0e28a17bb))
* Add WrapperPlayServerChunkBiomes ([#1383](https://github.com/retrooper/packetevents/pull/1383))
* Mark 26.1.2 as fully supported ([`4d1938b`](https://github.com/retrooper/packetevents/commit/4d1938b97564633900d3a73061ad137b680e8fa1))
* Track chunk section fluid count ([#1477](https://github.com/retrooper/packetevents/pull/1477))
* Add support for adventure v5 ([`bbeec89...c1163ee`](https://github.com/retrooper/packetevents/compare/bbeec896384e22844a51ab28b9bc4c91f8f1daf0...c1163ee7232c9084ae25b3e2e2a6f44d602ec737))

**View Full Changelog**: [`v2.12.0...v2.12.1`](https://github.com/retrooper/packetevents/compare/v2.12.0...v2.12.1)

!!!!! v2.12.0

# PacketEvents 2.12.0 is finally out! 🎉

This update primarily adds support for Minecraft 26.1.
Again, huge thanks to all users who have donated their hard-earned money to PacketEvents since the previous release. It's been received & it's very much appreciated.

Recent donors: PebbleHost, Dylan\
If you intend on donating and do not wish to be mentioned, please add such a note on your donation.

Also, thanks to the contributors that made this update possible, especially @booky10. Open-source software thrives when it receives contributions from the community.

# Announcement 📢

Visit our website: https://packetevents.com\
Documentation: https://docs.packetevents.com\
JavaDocs: https://javadocs.packetevents.com

## What's Changed (Summary)
* Added support for Minecraft 26.1 (and 26.1.1)
* Added support for changes to Fabric, leading to the addition of new internal modules.

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.11.2...v2.12.0

### If you want to support PacketEvents, consider donating to us on [PayPal](https://paypal.me/packetevents), [Streamlabs](https://streamlabs.com/retrooper), or [GitHub Sponsors](https://github.com/sponsors/retrooper).

## Maven/Gradle Dependency Setup Guide

**Check it out**: https://docs.packetevents.com/introduction/development-setup/


!!!!! v2.11.2

# PacketEvents 2.11.2 is finally out! 🎉

This update fixes various bugs. It's encouraged that users update.
Again, huge thanks to all users who have donated their hard-earned money to PacketEvents since the previous release. It's been received & it's very much appreciated.

Recent donors: PebbleHost, SSomar1607, ipavdev, Meed, frap

Also, thanks to the contributors that made this update possible.

If you intend on donating and do not wish to be mentioned, please add such a note on your donation.

# Announcement 📢⚠️

Visit our website: https://packetevents.com\
Documentation: https://docs.packetevents.com\
JavaDocs: https://javadocs.packetevents.com\
I've released a new YouTube [video](https://youtu.be/G7qYRHpZVIo).

## What's Changed (Summary)
* Fixed SynchronizedRegistriesHandler issues
* Fixed various equality/nullability issues with Registry values
* Explicitly specify 'Locale#ROOT' for all String#toLowerCaser() calls
* Removed 'end skylight' for 1.21.8 and below
* Log warning message when failing to uninject from velocity initializer
* Recommend release builds (and fallback to development builds) to users who run outdated software.
* Add 'CHAIN' to the 'CHAINS' tag
* Add new offset(Blockface face, int i) method to Vector3f/d/i classes
* Fixed 'infiniburn' tag parsing for 1.18.1 and older
* Fixed unexpected NPE if bukkit entity is null during unsafe lookup

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.11.1...v2.11.2

## If you want to support PacketEvents, consider donating to us on [PayPal](https://paypal.me/packetevents) or sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper).

## Maven/Gradle Dependency Setup Guide

**Check it out**: https://docs.packetevents.com/introduction/development-setup/


!!!!! v2.11.1

# PacketEvents 2.11.1 is finally out! 🎉

This update fixes various bugs. It's encouraged that users update.
Thanks to all users who donated after the previous release announcement. It's been received & it's appreciated.

Recent donors: TrekCraft, 3add, Vadim Soudé

If you intend on donating and do not wish to be mentioned, please add such a note on your donation.

# Brief Announcement 📢⚠️

### Visit our website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed (Summary)
* Fixed 'Can't resolve 'minecraft:day' in 'minecraft:timeline' for V_1_21_11'
* Fixed decoding & encoding for WrapperLoginServerPluginRequest & WrapperLoginClientPluginResponse 
* Improve StateTypes testing
* Added system property packetevents.debug.nbt-codec-trace
* Fixed Sound Codec

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.11.0...v2.11.1

## If you want to support PacketEvents, consider donating to us on [PayPal](https://paypal.me/packetevents) or sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.11.0

# PacketEvents 2.11.0 is finally out! 🎉

This update adds support for Minecraft 1.21.11.

# Brief Announcement 📢⚠️

### Visit our website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed (Summary)

* Added support for Minecraft 1.21.11

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.10.1...v2.11.0

## If you want to support PacketEvents, consider donating to us on [PayPal](https://paypal.me/packetevents) or sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.10.1

# PacketEvents 2.10.1 is finally out! 🎉

This update includes some bug fixes.

# Brief Announcement 📢⚠️

### Visit our website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed (Summary)

* Fixed for certain particle types on 1.21.9 & 1.21.10 Minecraft
* Fixed the bee's component on 1.21.9 & 1.21.10 Minecraft
* Correction of 'mul' calculation in LpVector3d

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.10.0...v2.10.1

## If you want to support PacketEvents, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.10.0

# PacketEvents 2.10.0 is finally out! 🎉

This update includes critical bug fixes & minor additions to the API.

# Brief Announcement 📢⚠️

### We have our very own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed (Summary)

* Added 1.21.9 & 1.21.10 support
* Various bug fixes (such as TPS reading, PlayerModelType reading/writing, and more)
* Update adventure API

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.9.5...v2.10.0

## If you want to support PacketEvents, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.9.5

# PacketEvents 2.9.5 is out now! 🎉

This update includes critical bug fixes & minor additions to the API.

# Brief Announcement 📢⚠️

### We have our very own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed (Summary)

* Injector bug fix (for Spigot)
* WolfVariant issue fixed
* Chunk Data error fixed
* NBT tag issues fixed
* Direction Y/Z axis being swapped fixed
* Added WrappedBlockState to constructor of WrapperPlayServerBlockChange

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.9.4...v2.9.5

## If you want to support PacketEvents, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.9.4

# PacketEvents 2.9.4 is out now! 🎉

This update includes minor bug fixes & minor additions to the API.

# Brief Announcement 📢⚠️

### We have our very own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed (Summary)

* Added SpigotConversionUtil#getEntityMetadata to retrieve metadata from a Bukkit entity.
* Fixed outdated 1.8 Spigot forks from being unable to load on PacketEvents.
* Fixed WrapperPlayServerWorldBorder for versions lower than 1.12.

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.9.3...v2.9.4

## If you want to support PacketEvents, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.9.3

# PacketEvents 2.9.3 is out now! 🎉

This is an update that adds support for Minecraft 1.21.8 server builds.

# Brief Announcement 📢⚠️

### We have our very own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed (Summary)

* Added support for 1.21.8 Minecraft server builds.

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.9.2...v2.9.3

## If you want to support PacketEvents, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.9.2

# PacketEvents 2.9.2 is out now! 🎉

This is a patch update that fixes issues with UserLoginEvent (which previously contained null user data) and with importing Adventure serializers.

# Brief Announcement 📢⚠️

### We have our very own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed (Summary)

* Added support for 1.7.2+ Minecraft protocol (useful for proxies)
* Fixed UserLoginEvent. It is now (always) triggered by PlayerJoinEvent to prevent User instances from containing null data.
* Fixed importing Adventure serializers.

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.9.1...v2.9.2

## If you want to support PacketEvents, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.9.1

# PacketEvents 2.9.1 is out now! 🎉

This is a hotfix patch update to fix issues with 2.9.0 release assets. See below for the 2.9.0 changelog.

### New update now supports Minecraft version 1.21.6 & 1.21.7. We suggest that you update, as numerous bugs have been fixed.

# Brief Announcement 📢⚠️

### We have our very own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed? (Summary)

* Added 1.21.6 support
* Added 1.21.7 support
* Bug fixes

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.8.0...v2.9.1

## If you want to support PacketEvents, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.9.0

# PacketEvents 2.9.0 is out now! 🎉

### New update now supports Minecraft version 1.21.6 & 1.21.7. We suggest that you update, as numerous bugs have been fixed.

# Brief Announcement 📢⚠️

### We have our very own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed? (Summary)

* Added 1.21.6 support
* Added 1.21.7 support
* Bug fixes

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.8.0...v2.9.0

## If you want to support PacketEvents, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.8.0

# PacketEvents 2.8.0 is out now! 🎉

### New update now supports Minecraft version 1.21.5. Please update, as numerous bugs have been fixed.

# Brief Announcement 📢⚠️

### We have our very own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed? (Summary)

* Added 1.21.5 support
* Various bugs fixed

**View Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.7.0...v2.8.0

## Contributors 🏅

This update wouldn't be possible without these contributors ❤️: @booky10, @Axionize, @Bram1903, @ManInMyVan, @ytnoos,
@AoElite, @HaHaWTH, @divinepablo, @scienziatopazzo, @bridgelol, @MachineBreaker, @Luuzzi, @Beaness, @Rubenicos, @Loyisa

## New Contributors

* @HaHaWTH made their first contribution in https://github.com/retrooper/packetevents/pull/1097
* @divinepablo made their first contribution in https://github.com/retrooper/packetevents/pull/1098
* @scienziatopazzo made their first contribution in https://github.com/retrooper/packetevents/pull/1105
* @bridgelol made their first contribution in https://github.com/retrooper/packetevents/pull/1199
* @Luuzzi made their first contribution in https://github.com/retrooper/packetevents/pull/1197
* @Beaness made their first contribution in https://github.com/retrooper/packetevents/pull/1212
* @Rubenicos made their first contribution in https://github.com/retrooper/packetevents/pull/1162
* @Loyisa made their first contribution in https://github.com/retrooper/packetevents/pull/1220

## If you want to support PacketEvents, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.7.0

# PacketEvents v2.7.0 is out now! 🎉

### New update with support of the latest Minecraft version. In addition to that, countless bugs were fixed.

# Brief Announcement 📢⚠️

### We have our own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed? (Summary)

* Added 1.21.4 support
* Fix: Potion contents item component for 1.21.2/1.21.3
* Fix: NPC objects to have unique server team names
* Add missing ServerVersion#V_1_9_1
* Validation checks added for window click type

**Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.6.0...v2.7.0

## New Contributors

* @Axionize made their first contribution in https://github.com/retrooper/packetevents/pull/1048
* @ShreyasAyyengar made their first contribution in https://github.com/retrooper/packetevents/pull/1044
* @mfnalex made their first contribution in https://github.com/retrooper/packetevents/pull/1076

## Contributors 🏅

This update wouldn't be possible without these contributors ❤️: @booky10, @rafi67000, @ShreyasAyyengar, @AoElite,
@Tecnio, @Axionize, @mfnalex

### If you want to support PacketEvents, and any upcoming events we plan for the future, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!! v2.6.0

# PacketEvents v2.6.0 is out now! 🎉

### New update with support of new platforms & the latest Minecraft version. In addition to that, countless bugs were fixed.

# Brief Announcement 📢⚠️

### We have our own website: https://packetevents.com

### Documentation: https://docs.packetevents.com

### JavaDocs: https://javadocs.packetevents.com

## What's Changed? (Summary)

* 1.21.2 and 1.21.3 Minecraft support
* Sponge support added
* Fabric support added (also server-side)
* Utilizing the proper bStats API
* Registry element issues fixed
* Mappings issues fixed
* AdvancedSlimePaper issues fixed
* BungeeCord issues fixed

### **Full Changelog**: https://github.com/retrooper/packetevents/compare/v2.5.0...v2.6.0

## Contributors 🏅

This update wouldn't be possible without these contributors ❤️: @booky10, @ManInMyVan, @SamB440, @rafi67000, @Bram1903,
@AbhigyaKrishna

### If you want to support PacketEvents, and any upcoming events we plan for the future, consider sponsoring us on [GitHub Sponsors](https://github.com/sponsors/retrooper)

## Maven/Gradle Dependency

### **Check out**: https://docs.packetevents.com/getting-started

!!!!!
