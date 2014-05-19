<h1>Utterance</h1>

Utterance lets you use iOS7's Speech Synthesizer in your Titanium project.

<h2>Before you start</h2>
* This is an iOS module only
* You need Titanium SDK 3.1.3.GA or greater
* You need iOS 7 or greater
* Before using this module you first need to install the package. If you need instructions on how to install a 3rd party module please read this installation guide.

<h2>Download the compiled release</h2>

Download the platform you wish to use:

* [iOS Dist](https://github.com/benbahrenburg/Utterance/tree/master/dist)

<h2>Building from source?</h2>

If you are building from source you will need to do the following:

Import the project into Xcode:

* Modify the titanium.xcconfig file with the path to your Titanium installation


<h2>Setup</h2>

* Download the latest release from the releases folder ( or you can build it yourself )
* Install the Utterance module. If you need help here is a "How To" [guide](https://wiki.appcelerator.org/display/guides/Configuring+Apps+to+Use+Modules). 
* You can now use the module via the commonJS require method, example shown below.

<h2>Importing the module using require</h2>
<pre><code>
var utterance = require('bencoding.utterance');
</code></pre>

<h2>Working with the Speech Proxy</h2>
The Speech proxy provides access to the native iOS Speech Synthesizer.  A new instance of this is created when you call createSpeech.

<b>Example</b>
<pre><code>

var speech = utterance.createSpeech();

</code></pre>

<h3>startSpeaking</h3>

The startSpeaking method will begin to speak the text provided.

<b>Parameters</b> 

The startSpeaking method, takes  a dictionary as a parameter with the following fields:

<b>text</b> : String :<b>Required</b>

The text to be spoken

<b>voice</b> : String :<b>Optional</b>

The voice to be used in speaking.  If this parameter is not defined, the proper language will be inferred.

<b>rate</b> : float :<b>Optional</b>

The speed which the text will be read. This is a value between 0 and 1.  You can use the DEFAULT_SPEECH_RATE, MIN_SPEECH_RATE, and MAX_SPEECH_RATE properties as well.

<b>volume</b> : float :<b>Optional</b>

The volume of the reader.  This is a value between 0 and 1.  By default a value of 1 will be used.

<b>preUtteranceDelay</b> : float :<b>Optional</b>

This places a delay at the beginning of the utterance.

<b>postUtteranceDelay</b> : float :<b>Optional</b>

This places a delay at the end of the utterance.

<b>Example</b>
<pre><code>
	if(speech.isSpeaking){
		Ti.API.info("already speaking");
	}
	
	speech.startSpeaking({
		text:"Hello World"
	});	
</code></pre>


<h3>continueSpeaking</h3>

This method continues reading any speech that has been paused.

<b>Parameters</b>

<b>None</b> 

<b>Example</b>
<pre><code>
	if(speech.isSpeaking){
		Ti.API.info("Already speaking, nothing to continue");
		return;
	}	
	speech.continueSpeaking();	
</code></pre>

<h3>pauseSpeaking</h3>

This method pauses the speaker.  This method has an optional parameter that allows you to pause immediately or at the end of a word.  By default this method will pause immediately.

<b>Example - Without optional parameter</b>
<pre><code>
	
	speech.pauseSpeaking();

</code></pre>

<b>Example - Optional parameter pause immediate</b>
<pre><code>
	
	speech.pauseSpeaking(speech.SPEECH_BOUNDARY_IMMEDIATE);

</code></pre>

<b>Example - Without optional parameter pause at end of word</b>
<pre><code>
	
	speech.pauseSpeaking(speech.SPEECH_BOUNDARY_WORD);

</code></pre>

<h3>stopSpeaking</h3>

This method stops the speaker.  This method has an optional parameter that allows you to pause immediately or at the end of a word.  By default this method will stop immediately.

<b>Example - Without optional parameter</b>
<pre><code>
	
	speech.stopSpeaking();

</code></pre>

<b>Example - Optional parameter pause immediate</b>
<pre><code>
	
	speech.stopSpeaking(speech.SPEECH_BOUNDARY_IMMEDIATE);

</code></pre>

<b>Example - Without optional parameter pause at end of word</b>
<pre><code>
	
	speech.stopSpeaking(speech.SPEECH_BOUNDARY_WORD);

</code></pre>

<h2>Properties</h2>

<b>DEFAULT_SPEECH_RATE</b>

This property can be used when setting the rate value for the startSpeaking method.  This defaults the rate to the current device/system value.

<b>MIN_SPEECH_RATE</b>

This property can be used when setting the rate value for the startSpeaking method.  This value is the slowest reading rate the device supports.

<b>MAX_SPEECH_RATE</b>

This property can be used when setting the rate value for the startSpeaking method.  This value is the fastest reading rate the device supports.

<b>SPEECH_BOUNDARY_IMMEDIATE</b>

This property can be used when calling the stopSpeaking or pauseSpeaking methods. This will pause or stop the speaker immediately.  This is the default configuration if no parameters are provided for the stopSpeaking or pauseSpeaking methods.

<b>SPEECH_BOUNDARY_WORD</b>

This property can be used when calling the stopSpeaking or pauseSpeaking methods. This will pause or stop the speaker after the current word. 

<h2>Events</h2>

<b>started</b>

This event is fired once the speaker has started reading the text.

<b>Example</b>
<pre><code>

var utterance = require('bencoding.utterance'),
	speech = utterance.createSpeech();

speech.addEventListener('started',function(d){
	Ti.API.info(JSON.stringify(d));
});

</code></pre>

<b>completed</b>

This event is fired once the speaker has finished reading the text.

<b>Example</b>
<pre><code>

var utterance = require('bencoding.utterance'),
	speech = utterance.createSpeech();

speech.addEventListener('completed',function(d){
	Ti.API.info(JSON.stringify(d));
});

</code></pre>

<b>paused</b>

This event is fired once the speaker has been paused.

<b>Example</b>
<pre><code>

var utterance = require('bencoding.utterance'),
	speech = utterance.createSpeech();

speech.addEventListener('paused',function(d){
	Ti.API.info(JSON.stringify(d));
});

</code></pre>

<b>canceled</b>

This event is fired once the speaker has been canceled.

<b>Example</b>
<pre><code>

var utterance = require('bencoding.utterance'),
	speech = utterance.createSpeech();

speech.addEventListener('canceled',function(d){
	Ti.API.info(JSON.stringify(d));
});

</code></pre>

<b>continued</b>

This event is fired once the speaker has been continued.

<b>Example</b>
<pre><code>

var utterance = require('bencoding.utterance'),
	speech = utterance.createSpeech();

speech.addEventListener('continued',function(d){
	Ti.API.info(JSON.stringify(d));
});

</code></pre>

<h2>Learn More</h2>

<h3>Examples</h3>
Please check the module's example folder or [here](https://github.com/benbahrenburg/Utterance/tree/master/example) for samples on how to use this project.

<h3>Twitter</h3>

Please consider following the [@benCoding Twitter](http://www.twitter.com/benCoding) for updates 
and more about Titanium.

<h3>Blog</h3>

For module updates, Titanium tutorials and more please check out my blog at [benCoding.Com](http://benCoding.com).

<h2>License</h2>
Utterance is available under the Apache 2.0 license.

Copyright 2013 Benjamin Bahrenburg

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.