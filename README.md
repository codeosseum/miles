<div align="center">
  <a href="https://github.com/codeosseum">
    <img alt="Codeosseum" src="docs/img/logo.png" width="250">
  </a>
</div>

<div align="center">

[![Build Status](https://dev.azure.com/codeosseum/Miles/_apis/build/status/codeosseum.miles?branchName=master)](https://dev.azure.com/codeosseum/Miles/_build/latest?definitionId=2&branchName=master)
[![License](https://img.shields.io/github/license/codeosseum/miles.svg?label=license)](LICENSE)

</div>

<div align="center">
Miles: Dedicated game server for Codeosseum.
</div>

# Miles

Miles is the dedicated game server of the Codeosseum platform. Matches made by Ares can be played on one of the managed Miles instances.

Miles is a event-diven [Spark](http://sparkjava.com/)-based monolith running on [GraalVM](http://www.graalvm.org/).

## Dependencies

Miles can only be run on GraalVM. Thankfully, GraalVM can now run Linux, Mac and Windows! Check out the [GraalVM Releases](https://github.com/oracle/graal/releases) page.

## Run

Create an executable JAR:

~~~~
$ ./mvnw clean install
~~~~

You can run it using `java -jar` in the `target` directory.

## Configuration

Miles can be configured through the following properties:

~~~~JSON5
{
  "self": {
    // The port Miles will listen to.
    "port": 3001,
    // URI on which Miles can be reached by the players.
    "uri": "http://127.0.0.1",
    // Unique identifier of the Miles instance.
    "identifier": "server-01"
  },
  "ares": {
    // The URI on which Miles can report events to Ares.
    "eventUri": "http://127.0.0.1:8000/api/event"
  },
  "quaestiones": {
    // The Quaestiones git repository URI.
    "remoteRepositoryUri": "https://github.com/codeosseum/quaestiones.git",
    // Path into which the repository will be cloned.
    "localRepositoryPath": "challenges",
    // The treelike to check out.
    "treelike": "master"
  },
  "faultSeeding": {
    // Runtime of a Fault Seeding match.
    "runtimeSeconds": 60,
    // Time to wait after all players have joined.
    "startingCountdownSeconds": 5
  }
}
~~~~

## License

Miles is licensed under the [Apache License 2.0](LICENSE).

## Acknowledgements

<div align="center">
  <img alt="Ministry of Human Capacities" src="docs/img/ministry.png" width="200">
</div>

<div align="center">
    Supported by the ÚNKP-18-2 New National Excellence Program of the Ministry of Human Capacities.
</div>
