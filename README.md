# A CLI Tutter client

## Installation

You have two options:
- `podman pull robotka/tutti:latest`
- Download the JAR from git.sch

### Configuration

You have to set the `TUTTI_AUTHOR` environment variable to your nick.

```sh
export TUTTI_AUTHOR="your_nick"
```

## Usage

### Container-based

```sh
alias tutti='podman run -it robotka/tutti:latest'
tutti list tutts
```

### As God intended

```text
alias tutti='java -jar /path/to/tutti.jar'
tutti --help
```

## TODO

- container run
- bash completion
- watch
