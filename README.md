# packetevents by purplex

PacketListener coded by purplex.

Licensed with the GPL3 license.

[![](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Coding help discord server: https://discord.gg/ZWTkg4v

How to use with **Gradle, Maven, sbt and leiningen**.

The lastest stable tested versions are displayed below. 

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
        implementation 'com.github.purplexdev:packetevents:1.1.9'
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
            <version>1.1.9</version>
        </dependency>
</dependencies>
```

# sbt

```
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.purplexdev" % "packetevents" % "1.1.9"	
```

# leiningen
```
:repositories [["jitpack" "https://jitpack.io"]]

:dependencies [[com.github.purplexdev/packetevents "1.1.9"]]	
```



