# JourNow

## Building
JDK 11 or newer is required to properly build the application.

A complete build can be issued with:
```
gradlew build
```

## Running
JRE 11 or newer is required to properly run the application.

The easiest way to run JourNow is to use `run` task:
```
gradlew run
```

Otherwise, the application can be packed into a single .jar file with:
```
gradlew uberJar
```
Then, it can be found in `journow-ui/build/libs/journow-ui-<version>-uber.jar`
