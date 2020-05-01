# packetevents by purplex

PacketListener coded by purplex.

Licensed with the GPL3 license.

License: GPL v3
[![](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Coding help discord server: https://discord.gg/ZWTkg4v

How to use with gradle, maven, sbt and leiningen.
The last stable tested versions are displayed below. 

[![](https://jitpack.io/v/purplexdev/packetevents.svg)](https://jitpack.io/#purplexdev/packetevents)

# Gradle

```gradle
allprojects {
        repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
        implementation 'com.github.purplexdev:packetevents:1.1.5'
}
```


# Maven

```xml
<repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

<dependencies>
        <dependency>
            <groupId>com.github.purplexdev</groupId>
            <artifactId>packetevents</artifactId>
            <version>1.1.5</version>
        </dependency>
</dependencies>
```

# sbt

```
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.purplexdev" % "packetevents" % "1.1.5"	
```

# leiningen
```
:repositories [["jitpack" "https://jitpack.io"]]

:dependencies [[com.github.purplexdev/packetevents "1.1.5"]]	
```



