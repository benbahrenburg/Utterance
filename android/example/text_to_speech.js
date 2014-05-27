'use strict';


exports.createWindow = function(){

	var utterance = require('bencoding.utterance'),
		textToSpeech = utterance.createSpeech();
	
	var win = Ti.UI.createWindow({
		backgroundColor:'white', title:"Android Text to Speech"
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
		
		if(textToSpeech.isSpeaking()){
			Ti.API.info("already speaking");
			return;
		}
		
		textToSpeech.startSpeaking({
			text:inputText.value
		});	
	});
	
	var pauseButton = Ti.UI.createButton({
		title:"Pause", left:5, width:80, height:50
	});
	vwButtonContainer.add(pauseButton);
	
	pauseButton.addEventListener('click',function(){
		
		if(!textToSpeech.isSpeaking()){
			Ti.API.info("Nothing to pause, press play first");
			return;
		}	
		textToSpeech.pauseSpeaking();	
	});
	
	var continueButton = Ti.UI.createButton({
		title:"Continue", left:5, width:100, height:50
	});
	vwButtonContainer.add(continueButton);
	
	continueButton.addEventListener('click',function(){
		
		if(textToSpeech.isSpeaking()){
			Ti.API.info("Already speaking, nothing to continue");
			return;
		}	
		textToSpeech.continueSpeaking();	
	});
	
	var stopButton = Ti.UI.createButton({
		title:"Stop", left:5, width:80, height:50
	});
	vwButtonContainer.add(stopButton);
	
	stopButton.addEventListener('click',function(){
		
		if(!textToSpeech.isSpeaking()){
			Ti.API.info("nothing to stop, press play first");
			return;
		}	
		textToSpeech.stopSpeaking();	
	});
	
	win.addEventListener('open',function(){
		if(!utterance.isSupported()){
			alert("Sorry your device does not support text to speech");
		}
	});
	
	textToSpeech.addEventListener('started',function(d){
		Ti.API.info(JSON.stringify(d));
	});
	textToSpeech.addEventListener('completed',function(d){
		Ti.API.info(JSON.stringify(d));
	});
	textToSpeech.addEventListener('paused',function(d){
		Ti.API.info(JSON.stringify(d));
	});
	textToSpeech.addEventListener('canceled',function(d){
		Ti.API.info(JSON.stringify(d));
	});
	textToSpeech.addEventListener('continued',function(d){
		Ti.API.info(JSON.stringify(d));
	});
	
	return win;	
};