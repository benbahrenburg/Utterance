//image sample provided by http://ny-pictures.com/nyc/photo/picture/42553/nostalgic_view_famous_hall

var utterance = require('bencoding.utterance'),
	speechToText = utterance.createSpeechToText();

// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white', title:"Android Blur Demos"
});

var infoLabel = Ti.UI.createLabel({
	text:'Speech to Text example. Press the button to start speaking and check the console for the results.',
	left:10, right:10, height:65, top:45
});

var startButton = Ti.UI.createButton({
	title:"Press to speaking", 
	left:10, right:10, height:65, top:150
});
win.add(startButton);

startButton.addEventListener('click',function(e){
	speechToText.startSpeechToText({
		promptText:"Say something interesting",
		maxResults: 10
	});
});

speechToText.addEventListener('completed',function(e){
	Ti.API.info(JSON.stringify(e));	
});

win.open();

