# Utterance    [![Titanium](http://www-static.appcelerator.com/badges/titanium-git-badge-sq.png)](http://www.appcelerator.com/titanium/)

Utterance lets you use Speech to Text in your Android Titanium projects.  This uses android.speech.RecognizerIntent on Android.

<h2>Importing the module using require</h2>
<pre><code>
var utterance = require('bencoding.utterance');
</code></pre>

<h2>Working with the SpeechToText Proxy</h2>

The Speech to Text proxy uses Android's underlying speech recognizer to convert speech into text.  A new instance of this is created when you call createSpeechToText.

<b>Example</b>
<pre><code>

var speech = utterance.createSpeechToText();

</code></pre>

<h3>startSpeechToText</h3>

The startSpeechToText method will begin to listen to the user and recognize speech..

<b>Parameters</b> 

The startSpeechToText method, takes  a dictionary as a parameter with the following fields:

<b>promptText</b> : String :<b>Required</b>

The text of the message displayed on the Android recording screen.

<b>maxResults</b> : Int :<b>Optional</b>

The max number of matching results returned by the speech recognizer.

<b>languageModel</b> : Property :<b>Optional</b>

The language model used by the speech recognizer. The parameter uses properties on the speechToText proxy.

The value options are:

* LANGUAGE_MODEL_WEB_SEARCH - Use a language model based on web search terms.
* LANGUAGE_MODEL_FREE_FORM - Use a language model based on free-form speech recognition.


This method sets the speech pitch for the SpeechToText engine.

<b>Example</b>
<pre><code>
	if(!speechToText.isSupport()){
		Ti.API.info("Your device does not support Speech to Text");
		return;
	}
	
	speechToText.startSpeechToText({
		promptText:"Say something interesting",
		maxResults: 10,
		languageModel : speechToText.LANGUAGE_MODEL_WEB_SEARCH
	});

</code></pre>


<h3>isSupport</h3>

This method provides a boolean return if Speech to Text is support by the device.

<b>Parameters</b>

<b>None</b> 

<b>Example</b>
<pre><code>
	if(speech.isSupport() === false){
		Ti.API.info("Your device does not support Speech to Text");
	}	
</code></pre>


<h2>Events</h2>

<b>started</b>

This event is fired once the speech recognizer has started listening

<b>Example</b>
<pre><code>

var utterance = require('bencoding.utterance'),
	speech2Text = utterance.createSpeechToText();

speech2Text.addEventListener('started',function(d){
	Ti.API.info(JSON.stringify(d));
});

</code></pre>

<b>completed</b>

This event is fired once the speech recognizer has finished listening and has created text from the user's speech

<b>Example</b>
<pre><code>

var utterance = require('bencoding.utterance'),
	speech2Text = utterance.createSpeechToText();

speech2Text.addEventListener('completed',function(d){
	Ti.API.info(JSON.stringify(d));
});

</code></pre>

<h2>Properties</h2>

<b>LANGUAGE_MODEL_WEB_SEARCH</b>

This property can be used when setting the languageModel value for the startSpeechToText method.  Use a language model based on web search terms.

<b>LANGUAGE_MODEL_FREE_FORM</b>

This property can be used when setting the languageModel value for the startSpeechToText method.  Use a language model based on free-form speech recognition.


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

