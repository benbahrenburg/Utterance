'use strict';

var utterance = require('bencoding.utterance'),
	speech = utterance.createSpeech();


var win = Ti.UI.createWindow({
    backgroundColor: 'white',
});

var inputText = Ti.UI.createTextArea({
	value:"Hello world. How are you?",backgroundColor:"#999",
	top:60, bottom:60, width:Ti.UI.FILL,
	borderStyle: Ti.UI.INPUT_BORDERSTYLE_ROUNDED,
});
win.add(inputText);

var vwButtonContainer = Ti.UI.createView({
	height:50, layout:'horizontal',bottom:5	
});

win.add(vwButtonContainer);

var playButton = Ti.UI.createButton({
	title:"Play", left:5, width:70, height:50
});
vwButtonContainer.add(playButton);

playButton.addEventListener('click',function(){
	
	if(speech.isSpeaking){
		Ti.API.info("already speaking");
	}
	
	speech.startSpeaking({
		text:inputText.value
	});	
});

var pauseButton = Ti.UI.createButton({
	title:"Pause", left:5, width:80, height:50
});
vwButtonContainer.add(pauseButton);

pauseButton.addEventListener('click',function(){
	
	if(!speech.isSpeaking){
		Ti.API.info("Nothing to pause, press play first");
		return;
	}	
	speech.pauseSpeaking();	
});

var continueButton = Ti.UI.createButton({
	title:"Continue", left:5, width:100, height:50
});
vwButtonContainer.add(continueButton);

continueButton.addEventListener('click',function(){
	
	if(speech.isSpeaking){
		Ti.API.info("Already speaking, nothing to continue");
		return;
	}	
	speech.continueSpeaking();	
});

var stopButton = Ti.UI.createButton({
	title:"Stop", left:5, width:80, height:50
});
vwButtonContainer.add(stopButton);

stopButton.addEventListener('click',function(){
	
	if(!speech.isSpeaking){
		Ti.API.info("nothing to stop, press play first");
		return;
	}	
	speech.stopSpeaking();	
});

win.addEventListener('open',function(){
	if(!utterance.isSupported()){
		alert("Sorry your device does not support text to speech");
	}
});

speech.addEventListener('started',function(d){
	Ti.API.info(JSON.stringify(d));
});
speech.addEventListener('completed',function(d){
	Ti.API.info(JSON.stringify(d));
});
speech.addEventListener('paused',function(d){
	Ti.API.info(JSON.stringify(d));
});
speech.addEventListener('canceled',function(d){
	Ti.API.info(JSON.stringify(d));
});
speech.addEventListener('continued',function(d){
	Ti.API.info(JSON.stringify(d));
});

win.open();
