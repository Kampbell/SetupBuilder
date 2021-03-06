Gradle Setup Builder Plugin
====

[![Build Status](https://travis-ci.org/i-net-software/SetupBuilder.svg)](https://travis-ci.org/i-net-software/SetupBuilder)
[![License](https://img.shields.io/badge/license-Apache_License_2.0-blue.svg)](https://github.com/i-net-software/SetupBuilder/blob/master/license.txt)

The Setup Builder is a plugin for Gradle which can create a native setups for different platforms like Windows, Linux and OSX. The output is a *.msi, a *.deb, a *.rpm or a *.dmg file. The target is an installer for Java applications.

System Requirements
----
| Platform  | Requirement                                                          |
| :---------| :------------------------------------------------------------------- |
| all       | Java 8 or higher. Gradle must run with Java 8                        |
| Windows   | Wix Toolset or WixEdit must be installed                             |
| Linux     | Lintian, FakeRoot <br> on Ubuntu: `apt-get install lintian fakeroot` |
| Linux     | dpkg for creating Debian packages: `apt-get install dpkg`         |
| Linux     | rpm for creating RPM packages: `apt-get install rpm`              |

Plugin and Gradle Version
----
| Plugin Version | Gradle Version |
| :--------------| :------------- |
| <= 1.5         | 2.3 - 2.11     |
| 1.6            | 2.12 - 2.13    |
| 1.7            | 2.14           |
| 1.8            | 3.0            |

Tasks
----
The plugin add the follow tasks:
* msi
* deb
* rpm
* dmg

Sample Usage
----
### Base Sample
    plugins {
        id "de.inetsoftware.setupbuilder" version "1.8"
    }
    
    setupBuilder {
        vendor = 'i-net software'
        application = "SetupBuilder Plugin"
        appIdentifier = "SetupBuilder"
        version = '1.0'
        licenseFile = 'license.txt'
        // icons in different sizes for different usage. you can also use a single *.ico or *.icns file
        icons = ['icon16.png', 'icon32.png', 'icon48.png', 'icon128.png']
        // all files for all platforms
        from( 'source' ) {
            include 'files/*.jar'
        }
        bundleJre = 1.8
    }
    
    msi {
        // files only for the Windows platform
        from( 'windows' ) {
            include 'foo.exe'
            rename { 'bar.exe' }
        }
    }

More properties can be found in the sources of [setupBuilder][setupBuilder], [msi][msi], [deb][deb], [rpm][rpm] and [dmg][dmg].

### Zip Sample
Create a zip file with the same files define in setupBuilder extension.

    ...
    setupBuilder {
        ...
    }
    task zip(type: Zip) {
        with setupBuilder
        doLast {
            artifacts {
                archives zip
            }
        }
    }


License
----
Apache License, Version 2.0

[setupBuilder]: https://github.com/i-net-software/SetupBuilder/blob/master/src/com/inet/gradle/setup/SetupBuilder.java
[msi]: https://github.com/i-net-software/SetupBuilder/blob/master/src/com/inet/gradle/setup/msi/Msi.java
[deb]: https://github.com/i-net-software/SetupBuilder/blob/master/src/com/inet/gradle/setup/deb/Deb.java
[rpm]: https://github.com/i-net-software/SetupBuilder/blob/master/src/com/inet/gradle/setup/rpm/Rpm.java
[dmg]: https://github.com/i-net-software/SetupBuilder/blob/master/src/com/inet/gradle/setup/dmg/Dmg.java
