# Utterance    [![Titanium](http://www-static.appcelerator.com/badges/titanium-git-badge-sq.png)](http://www.appcelerator.com/titanium/)

Utterance lets you use your device's native Text to Speech and Speech to Text capabilities in your Titanium projects.

See the Android example video [here](http://youtu.be/Nlrp7L2eeJs)


<h2>Before you start</h2>
* You need Titanium SDK 3.2.1.GA or greater
* If using iOS, you need iOS 7 or greater
* If using Android, you need Android 4 or greater
* Before using this module you first need to install the package. If you need instructions on how to install a 3rd party module please read this installation guide.

<h2>Download the compiled release</h2>

Download the platform you wish to use:

* [iOS Dist](https://github.com/benbahrenburg/Utterance/tree/master/ios/dist)
* [Android Dist](https://github.com/benbahrenburg/Utterance/tree/master/android/dist)
* Install from gitTio    [![gitTio](http://gitt.io/badge.png)](http://gitt.io/component/bencoding.utterance)

<h2>Building from source?</h2>

If you are building from source you will need to do the following:

Import the project into Xcode:

* Modify the titanium.xcconfig file with the path to your Titanium installation

Import the project into Eclipse:

* Update the .classpath
* Update the build properties

<h2>Setup</h2>

* Download the latest release from the releases folder ( or you can build it yourself )
* Install the Utterance module. If you need help here is a "How To" [guide](https://wiki.appcelerator.org/display/guides/Configuring+Apps+to+Use+Modules). 
* You can now use the module via the commonJS require method, example shown below.

<h2>Importing the module using require</h2>
<pre><code>
var utterance = require('bencoding.utterance');
</code></pre>

<h2>Text to Speech</h2>

Supported Platforms :  iOS | Android

The Speech proxy provides Text to Speech capabilities using the device's native platform Text To Speech Engine.

To learn more about Text to Speech please read the documentation:

* [Android](https://github.com/benbahrenburg/Utterance/tree/master/android/documentation/tts.md)
* [iOS](https://github.com/benbahrenburg/Utterance/tree/master/ios/documentation/tts.md)


<h2>Speech To Text</h2>

Supported Platforms :  Android

The SpeechToText proxy provides Speech to Text capabilities using the device's native platform speech recognizer.

To learn more about Text to Speech please read the documentation:

* [Android](https://github.com/benbahrenburg/Utterance/tree/master/android/documentation/stt.md)


<h2>Learn More</h2>

<h3>Examples</h3>

Please check the module's example folder :

* [iOS](https://github.com/benbahrenburg/Utterance/tree/master/ios/example)
* [Android](https://github.com/benbahrenburg/Utterance/tree/master/android/example)

<h3>Credits</h3>
The language detection snippet is from [Eric Wolfe's](https://github.com/ericrwolfe) contribution to [Hark](https://github.com/kgn/Hark)

<h3>Twitter</h3>

Please consider following the [@benCoding Twitter](http://www.twitter.com/benCoding) for updates 
and more about Titanium.

<h3>Blog</h3>

For module updates, Titanium tutorials and more please check out my blog at [bencoding.com](http://bencoding.com).

<h2>License</h2>
Utterance is available under the Apache 2.0 license.

Copyright 2014 Benjamin Bahrenburg

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
