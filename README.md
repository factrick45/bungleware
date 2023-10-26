# Bungleware
[![Build](https://github.com/factrick45/bungleware/actions/workflows/build.yml/badge.svg)](https://github.com/factrick45/bungleware/actions/workflows/build.yml)

A client utility mod for Minecraft 1.8.9.

Bungleware provides convenience features such as an autoclicker, automatic disconnection when low on health, a free camera, and a light overlay for spawn proofing.

## Installation

Bungleware requires [Legacy Fabric](https://legacyfabric.net/), Minecraft 1.8.9 and Java 8 (aka 1.8). Newer versions of Java will break networking.

For your convenience, the [releases page](https://github.com/factrick45/bungleware/releases) provides premade instances for MultiMC and launchers forked from it. They already come with Fabric installed and are optionally bundled with Optifine. This is the recommended method of installation.

## Compilation

In a shell, run gradlew or gradlew.bat depending on your platform. Java 8 or later is required.

    ./gradlew build

The mod can be tested with:

    ./gradlew build runClient
