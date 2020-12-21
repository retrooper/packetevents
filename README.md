# packetevents

Some tasks require you to work at the packet level.
It is very common for people to write their own packet system and event manager.
PacketEvents is here to save you a ton of work.
We provide high level packet-wrappers.
The PacketEvents API focuses on providing you easy to use packet-wrappers supporting 1.7.10 spigots up to the current latest version!
This API even comes with two event systems.
The first one (currently deprecated) is extremely similar to Bukkit's event system making it really easy to use for
experienced Bukkit plugin developers.
The second one (recommended) prioritizes performance.
PacketEvents listens to its community. 
Many users love shading the API into their plugin, so that their customers won't need to
download the API as an external dependency.
We have gone out of our way to support multiple plugins shading the API (resulting in duplicate PacketEvents APIs)
and they all work together.

### Resources
* [Website](https://packetevents.github.io)
* [SpigotMC](https://www.spigotmc.org/resources/packetevents-api.80279/)
* [JavaDoc](https://retrooper.github.io/packetevents)

### Licensing
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)\
PacketEvents is licensed under the MIT License.
We intentionally selected this license compared to stricter options.\
[Summary of the license](https://tldrlegal.com/license/mit-license)

### Contributing
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v2.0%20adopted-ff69b4.svg)](CONTRIBUTOR_CODE_OF_CONDUCT.md)\
PacketEvents is an open source project.
We appreciate contributions.
It is amazing that you want to take an extra step to contribute a change to the API that will
hopefully benefit the majority of users.
Before taking this step, please read the Contributor Code Of Conduct. (click on the badge above)
Please also read [this](CONTRIBUTING.md) if you consider contributing.
Our discord server has a role for contributors.\
[Learn more about it in our discord server](https://discord.me/packetevents)

### Forking
We also appreciate forks.
It is always great news to us knowing someone is building their own API off of the PacketEvents API.
If you are developing a feature that is only going to benefit you and not the majority of the community,
please do not contribute!
Make a fork instead!
Our discord server has a discord role for forkers.\
[Learn more about it in our discord server](https://discord.me/packetevents)

### Compilation
PacketEvents is built with Maven and all past releases have always used maven.
The majority of users (currently) in the Bukkit community use Maven.
It should be easier for most people to contribute if we use Maven too.
Just because PacketEvents is built with Maven doesn't mean you can't use other dependency managers to use the API.

### Setup
You can either use the API with a dependency manager(like Maven or Gradle), depend on a pre-built build or compile it yourself with the source code.
We recommend using a dependency manager.
#### 1. Using a dependency manager
[![](https://jitpack.io/v/retrooper/packetevents.svg)](https://jitpack.io/#retrooper/packetevents)

#### 2. Using as an external pre-built dependency.
* Download the [latest build from SpigotMC](https://www.spigotmc.org/resources/packetevents-api.80279/)
* Add PacketEvents as a depend or soft-depend to your plugin.yml like this:
```yml
depend: [packetevents]
```
Your customers are now required to download this external pre-built dependency on their minecraft server.\
It is an API and a Bukkit plugin at the same time.
The version of the pre-built dependency is recommended to be the same as the version
of the API you used in your plugin incase there have been API changes.

#### 3. Compiling source code.
When compiling the source code yourself, you are required to use Maven. (unless you switch yourself)\
We currently do not have a guide showing you how to do this, but it should be self explanatory if you know what you are doing.

### Author
PacketEvents was started by retrooper.
This API was initially for his private anti-cheat he was developing just to test around with.
He has quit anti-cheat development and is now prioritizing this API and a few other private projects.
PacketEvents now has a handfull of contributors improving the API.
Share the API, donate, contribute, fork.
Thanks. 
