

	
exports.createWindow = function(){

	var utterance = require('bencoding.utterance'),
		speechToText = utterance.createSpeechToText();
			
	// open a single window
	var win = Ti.UI.createWindow({
		backgroundColor:'white', title:"Android Speech to Text"
	});
	
	var infoLabel = Ti.UI.createLabel({
		text:'Speech to Text example. Press the button to start speaking and check the console for the results.',
		left:10, right:10, height:65, top:45
	});
	
	var startButton = Ti.UI.createButton({
		title:"Press and start speaking", 
		left:10, right:10, height:65, top:150
	});
	win.add(startButton);
	
	startButton.addEventListener('click',function(e){
		speechToText.startSpeechToText({
			promptText:"Say something interesting",
			maxResults: 10
		});
	});

	speechToText.addEventListener('started',function(e){
		Ti.API.info(JSON.stringify(e));	
	});
		
	speechToText.addEventListener('completed',function(e){
		Ti.API.info(JSON.stringify(e));	
		if(e.success && (e.wordCount > 0)){
			alert("Speech Recognized " + e.wordCount + " matches found: "  + JSON.stringify(e.words));
		}else{
			alert("Unable to recognize your speech");
		}
	});
	
	return win;
	
};




