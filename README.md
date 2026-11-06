# create-more-package-couriers-fix

Server-side patches for [Create More: Package Couriers](https://github.com/rekales/create-more-package-couriers) 2.3.1 (NeoForge 1.21.1), run on the Createrington server.

**Original author:** Krei ([rekales](https://github.com/rekales)). Package Couriers is their work, licensed under the Lambda License (`LICENSE.txt`), and this fork stays under it. `main` is the upstream `1.21.1-neo` history at the commit the official 2.3.1 jar was built from (`c521503`, every class matches the CurseForge/Modrinth jar). Our changes go in through pull requests, so the diff against upstream is the commit list on top of it.

The patched jar only runs on the server. Players use the official jar from CurseForge or Modrinth: the changes are server logic only, and they keep the mod's registries, packets and synced data as they are.

## Changes

- **Curios location transmitter.** The transmitter's Curios tick returned early on the server and ran on the client instead, so a transmitter in the belt slot never made its wearer a plane target. It now registers on the server.
- **Depots on Sable sub-levels** ([upstream #79](https://github.com/rekales/create-more-package-couriers/issues/79)). Blocks on a sub-level sit in its plot, far from where the sub-level is shown. The destination check asked the distance manager whether the plot chunk was entity-ticking, which is always false for plot chunks (Sable answers through `isPositionTicking`), so signs on vehicles never registered. Planes now fly to the target's world position, follow it while the vehicle moves, and deliver to the depot in the plot. Planes ejected from a vehicle start at the ejector's world position and heading.
- **`planeMaxDistance`** in `serverconfig/cmpackagecouriers-server.toml`: launches whose target is further than this many blocks from the launch point are refused (ejectors then throw the plane as a normal item). The limit itself is allowed. `-1`, the default, keeps the unlimited range. It is only checked within one dimension, so set `planeCrossDimTransport = false` as well to make it a hard limit.
- **Launch failure messages.** A hand-thrown plane that is refused says why in the action bar: `Target is in another dimension` or `Target is 734 blocks away, planes reach 500`. Other refusals keep the mod's `No Valid Targets`. The text is sent as a literal, so stock clients show it without a language entry.

`sable-companion` is bundled jar-in-jar, as other mods do, so the Sable code is a no-op without Sable.

## Versioning

The patched jar is `2.3.1.2`. FML keeps the highest version when two jars share a mod id, so it wins over a stock jar in `mods/`. Clients on 2.3.1 can join: the mod's packet channel version does not change with the mod version. Clients ignore the extra `planeMaxDistance` key in the synced server config.

## Build

Needs JDK 21.

```
./gradlew jar
```

The jar lands in `build/libs/`.
