# awsscala

[![Build Status](https://travis-ci.org/99taxis/awsscala.svg?branch=master)](https://travis-ci.org/99taxis/awsscala "Travis CI") [![Codacy Badge](https://api.codacy.com/project/badge/grade/106d6d09bfe746aa85a1d6c51803e01b)](https://www.codacy.com/app/99taxis/awsscala) [![Coverage Status](https://coveralls.io/repos/github/99taxis/awsscala/badge.svg?branch=master)](https://coveralls.io/github/99taxis/awsscala?branch=master "Coveralls") [![Dependencies](https://app.updateimpact.com/badge/704215565069324288/awsscala.svg?config=compile)](https://app.updateimpact.com/latest/704215565069324288/awsscala) [![Dependency Status](https://www.versioneye.com/user/projects/56e7b4d296f80c00362cd919/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56e7b4d296f80c00362cd919) [![Join the chat at https://gitter.im/99taxis/awsscala](https://badges.gitter.im/99taxis/awsscala.svg)](https://gitter.im/99taxis/awsscala?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

AWS SDK Java to Scala translations, wrappers and reinventions

[![License](http://img.shields.io/:license-Apache%202-red.svg)](https://github.com/99taxis/awsscala/blob/master/LICENSE "Apache 2.0 Licence") [![Bintray](https://img.shields.io/bintray/v/99taxis/maven/awsscala.svg)](https://bintray.com/99taxis/maven/awsscala/_latestVersion) [![Maintenance](https://img.shields.io/maintenance/yes/2016.svg)](https://github.com/99taxis/awsscala/commits/master)

## Installation

For sbt builds, add the following to your build.sbt:

```scala
resolvers += "bintray.99taxis OS releases" at "http://dl.bintray.com/content/99taxis/maven"
libraryDependencies += "com.taxis99" %% "awsscala" % "X.Y.Z",
```

The version comes from the corresponding Git tag.

## Usage

Assuming you have aws credentials available, substitute the `accessKey = "@key"` and `secretKey = "@secret"` for yours:

```scala
implicit val awsCredentials = BasicAWSCredentialsProvider(accessKey = "@key", secretKey = "@secret")
val sqsClient = new SQSClient(queueName = "@queue", sqsEndpoint = "@sqsEndpoint")
val messages = sqsClient.fetchMessages(maxNumberOfMessages = 10)
```

For more details see the [tests](https://github.com/99taxis/awsscala/tree/master/src/test/scala/com/taxis99/aws) or the [API docs](http://dev.99taxis.com/awsscala/latest/api/#com.taxis99.aws.package).

## Instructions for Development

We recommend you to publish your library to a [BinTray](https://bintray.com/) public repository .
To publish your library to BinTray follow these steps:

* [Create a BinTray account](https://bintray.com/)
* In BinTray get your API Key from [your profile page](https://bintray.com/profile/edit)
* From the command line login to BinTray: `activator bintray::changeCredentials`
* Change the version via Git:  `git tag vX.Y.Z`
* Publish your library: `activator +bintray::publish`
                Note: The `+` publishes the cross-versioned (e.g. Scala 2.10 & 2.11) builds.
                
                
To enable others to use your library you can either have them add a new resolver / repository to their build or you can [add your library to Maven Central via jCenter](http://blog.bintray.com/2014/02/11/bintray-as-pain-free-gateway-to-maven-central/).

For the former option do have your scala users add the following to their build.sbt:

```scala
resolvers += "YOUR_BINTRAY_USERNAME" at "http://dl.bintray.com/content/YOUR_BINTRAY_USERNAME/maven"
```

## License

`awsscala` is open source software released under the Apache 2.0 License.

See the [LICENSE](https://github.com/99taxis/awsscala/blob/master/LICENSE) file for details.
