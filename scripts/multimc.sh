#!/bin/sh

LOADER_VERSION=$(grep -Po "loader_version = \K.+" ../gradle.properties)
MC_VERSION=$(grep -Po "minecraft_version = \K.+" ../gradle.properties)
MOD_VERSION=$(grep -Po "mod_version = \K.+" ../gradle.properties)

INSTANCE_URL=https://github.com/Legacy-Fabric/multimc-instance-creator/releases/download
INSTANCE_URL=$INSTANCE_URL/$LOADER_VERSION/legacyfabric-$MC_VERSION+loader.$LOADER_VERSION.zip
OPTIFABRIC_URL=https://github.com/RedLime/OptiFabric-Pre1.14/releases/download/
OPTIFABRIC_URL=$OPTIFABRIC_URL/1.0.5/optifabric-1.0.5+$MC_VERSION.jar

# download base instance
wget -O fabricinstance.zip $INSTANCE_URL
if [ $(file -b --mime-type fabricinstance.zip) != "application/zip" ]; then
    exit 1
fi
rm -r instance
unzip fabricinstance.zip -d instance
cp -v instance-template/* instance/

# remove legacy-fixes
rm instance/.minecraft/mods/legacy-fixes-1.0.0.jar

# install bungleware
cp -v ../build/libs/bungleware-$MOD_VERSION.jar instance/.minecraft/mods

# pack the instance
cd instance
zip -r ../bungleware-$MOD_VERSION-multimc.zip .
cd ..

# install optifine
# optifine jar can't be downloaded
cp -v optifine.jar instance/.minecraft/mods/
wget -O instance/.minecraft/mods/optifabric.jar $OPTIFABRIC_URL

# pack the optifine instance
cd instance
zip -r ../bungleware-$MOD_VERSION-multimc-optifine.zip .
cd ..
