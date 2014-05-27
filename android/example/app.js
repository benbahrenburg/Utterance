'use strict';

var tts = require('text_to_speech'),
	stt = require('speech_to_text');


var win = Ti.UI.createWindow({
	backgroundColor:'white', title:"Android Utterance Demos"
});

var btnTTS = Ti.UI.createButton({
	title:"Text to Speech",
	left:15, right:15, height:65, top:50
});
win.add(btnTTS);
btnTTS.addEventListener('click',function(e){
	tts.createWindow().open();
});

var btnSTT = Ti.UI.createButton({
	title:"Speech to Text",
	left:15, right:15, height:65, top:150
});
win.add(btnSTT);
btnSTT.addEventListener('click',function(e){
	stt.createWindow().open();
});

win.open();
