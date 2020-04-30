# packetevents by purplex
Listen to minecraft packets.

PacketEvents coded by purplex.

Coding help discord server: https://discord.gg/ZWTkg4v

Examples can be found in this class->
me/purplex/packetevents/example/TestExample.java


How to use with gradle, maven, sbt and leiningen.
The last stable tested versions are displayed below. 
Updated daily.

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

[![](https://jitpack.io/v/purplexdev/packetevents.svg)](https://jitpack.io/#purplexdev/packetevents)



