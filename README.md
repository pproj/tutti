# A CLI Tutter client

## Installation

### For Gen-Z kids

```sh
alias tutti='podman run -it pproj/tutti:latest'
```

### As God intended

```sh
cd ~/Downloads
wget https://git.sch.bme.hu/pp23/tutti/-/jobs/artifacts/main/download?job=jar -O tutti.zip
unzip tutti.zip
cp build/libs/*.jar tutti.jar
rm -rf build
alias tutti="java -jar $(pwd)/tutti.jar"
```

### Local dev

```sh
./gradlew jar
alias tutti="java -jar $(pwd)/build/libs/tutti-1.0-SNAPSHOT-standalone.jar"
``
```

## Configuration

You have to set the `TUTTI_AUTHOR` environment variable to your nick.

```sh
export TUTTI_AUTHOR="your_nick"
```

## Usage examples

```text
tutti --help
tutti create
tutti create --author your_nick
tutti list tutts
tutti list tutts --author your_nick
tutti list tutts --limit 10
tutti list tutts --tag wow
tutti list authors
tutti list tags
```
