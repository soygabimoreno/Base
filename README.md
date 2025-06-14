# Base
This is the base of the multi-module repo.

## Prerequisites

- **JDK 21**. Make sure `java --version` reports a JDK 21 installation.
- **Android SDK 35** or newer installed via Android Studio or the
  command line tools.

Update your `local.properties` with your keys and any other secrets as
described below so the project can be built and signed.

## App Los androides by Gabi Moreno
This is the module corresponding to the app Los androides by Gabi Moreno.

Download it from [Google Play](https://gabimoreno.soy/app).

### Stack
It's using:

- MVVM with coroutines
- Clean Architecture
- Jetpack Compose
- Firebase
- Hilt
- Arrow
- Retrofit
- Moshi
- ExoPlayer
- Glide
- Datastore
- MockK
- Kluent
- MockWebServer
- ...

### Collaborate
I am [Gabi Moreno](https://gabimoreno.soy).

This is an open source project. Feel free to request **[issues](https://github.com/soygabimoreno/Base/issues)**.

And for sure, **[Pull Requests](https://github.com/soygabimoreno/Base/pulls)** are opened and super-well received.

#### Configuration
Take into account you should add something like this in your `local.properties` to build the
project:

```
CLARITY_PROJECT_ID=foo
MASTER_KEY=foo
keystore.keyAlias=foo
keystore.storeFile=foo
keystore.storePassword=foo
keystore.keyPassword=foo
```

## Building with Gradle

From the repository root run:

```bash
./gradlew assembleDebug
```

This compiles all modules. You can build a specific sample app by
providing its Gradle path, for example `:gabimoreno` or `:bike`.

### Running sample apps

Deploy the debug build of a module to a connected device or emulator:

```bash
./gradlew :gabimoreno:installDebug   # Los androides
./gradlew :bike:installDebug         # Bike demo
```

### Running unit tests

Execute all unit tests across the project with:

```bash
./gradlew test
```

You can run tests for a single module using its path, e.g.
`./gradlew :modules:core:test`.

Thank you very much for being part of this project! 🤗
