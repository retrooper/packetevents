# PacketEvents
API for listenening to minecraft packets

Credit to TinyProtocol

PacketEvents coded by purplex
Discord server: https://discord.gg/ZWTkg4v

# Gradle

```gradle
allprojects {
        repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
        implementation 'com.github.purplexdev:packetevents:Tag'
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
            <artifactId>PacketEvents</artifactId>
            <version>1.0.6</version>
            <scope>provided</scope>
        </dependency>
</dependencies>
```

# sbt

```
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.purplexdev" % "packetevents" % "Tag"	
```

# leiningen
```
:repositories [["jitpack" "https://jitpack.io"]]

:dependencies [[com.github.purplexdev/packetevents "Tag"]]	
```




