# PacketEvents
Listen to minecraft packets and send minecraft packets.

Credit to TinyProtocol

PacketEvents coded by purplex.

Coding help discord server: https://discord.gg/ZWTkg4v

Examples can be found at
me/purplex/packetevents/example/TestExample.java

# Gradle

```gradle
allprojects {
        repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
        implementation 'com.github.purplexdev:packetevents:1.0.9'
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
            <version>1.0.9</version>
        </dependency>
</dependencies>
```

# sbt

```
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.purplexdev" % "packetevents" % "1.0.9"	
```

# leiningen
```
:repositories [["jitpack" "https://jitpack.io"]]

:dependencies [[com.github.purplexdev/packetevents "1.0.9"]]	
```




