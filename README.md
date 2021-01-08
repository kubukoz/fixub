# fixub

A program for fixing Polish characters in subtitles.

# Build native image

```bash
sbt graalvm-native-image:packageBin
```

# Usage

The program now reads input from stdin and writes to stdout, for maximum flexibility.

`./run [input-charset:Windows-1250] < input > output`
