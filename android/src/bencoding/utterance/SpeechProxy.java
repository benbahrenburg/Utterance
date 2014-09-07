/**
 * Utterance Speech to Text and Text to Speech
 * Copyright (c) 2010-2014 by Benjamin Bahrenburg. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package bencoding.utterance;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollPropertyChange;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.KrollProxyListener;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiLifecycle;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;

@Kroll.proxy(creatableInModule=UtteranceModule.class)
public class SpeechProxy extends KrollProxy implements TiLifecycle.OnLifecycleEvent, KrollProxyListener,OnInitListener, OnUtteranceCompletedListener   {
	private String _logName = UtteranceModule.MODULE_FULL_NAME;
	private TextToSpeech _tts  = null;
	private String _text = "";
	private String _voice = "";

	//Add properties for iOS compatability
	@Kroll.constant public static final int DEFAULT_SPEECH_RATE = 0;
	@Kroll.constant public static final int MIN_SPEECH_RATE = 0;
	@Kroll.constant public static final int MAX_SPEECH_RATE = 0;
	@Kroll.constant public static final int SPEECH_BOUNDARY_IMMEDIATE = 0;
	@Kroll.constant public static final int SPEECH_BOUNDARY_WORD = 0;
	
	public SpeechProxy(){		
		super();
		_tts=new TextToSpeech(TiApplication.getInstance().getApplicationContext(),this);
	}

	@Override
	public void onUtteranceCompleted(String utteranceId) {
		Log.d(_logName, "utterance completed");
		doListener("completed");		
	}	
	
	@Override
	public void onInit(int status) {
        if (status == TextToSpeech.LANG_MISSING_DATA
                || status == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e(_logName, "This Language is not supported");
    		if (hasListeners("completed")) {
    			HashMap<String, Object> event = new HashMap<String, Object>();
    			event.put("success",false);
    			event.put("message","This Language is not supported");
    			event.put("text",_text);
    			event.put("voice",_voice);
    			
    			fireEvent("completed", event);
    		}				                
        }
            
    	if(status == TextToSpeech.SUCCESS) {
    		//Log.d(_logName, "Adding OnUtteranceCompletedListener");  
    		_tts.setOnUtteranceCompletedListener(this);
    		_voice = _tts.getLanguage().toString();			    		
    	}		
	}
	

	
	private void doListener(String eventName){
		if (hasListeners(eventName)) {
			HashMap<String, Object> event = new HashMap<String, Object>();
			event.put("success",true);
			event.put("speaking",_tts.isSpeaking());
			event.put("text",_text);
			event.put("voice",_voice);
			fireEvent(eventName, event);
			Log.d(_logName,"event: " + eventName+ " fired");
		}else{
			Log.d(_logName,"event: " + eventName+ " not found");
		}
	}
    
	@Kroll.getProperty @Kroll.method
	public Boolean isSpeaking()
	{
		if(_tts==null){
			return false;
		}else{
			return _tts.isSpeaking();
		}
	}

	public static Locale toLocale(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len != 2 && len != 5 && len < 7) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 2) {
            return new Locale(str, "");
        } else {
            if (str.charAt(2) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            char ch3 = str.charAt(3);
            if (ch3 == '_') {
                return new Locale(str.substring(0, 2), "", str.substring(4));
            }
            char ch4 = str.charAt(4);
            if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 5) {
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            } else {
                if (str.charAt(5) != '_') {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
            }
        }
    }
	
	@Kroll.method
	public boolean isLanguageAvailable(String language)
	{
		int check =_tts.isLanguageAvailable(toLocale(language));
		return ((check!= TextToSpeech.LANG_MISSING_DATA) && (check!=TextToSpeech.LANG_NOT_SUPPORTED));
	}
	
	@Kroll.method
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void startSpeaking(HashMap hm){
		KrollDict args = new KrollDict(hm);
		if(!args.containsKeyAndNotNull("text")){
			Log.e(_logName,"the text parameter is required");
			return;
		}
		_text = args.getString("text");
		_voice = "auto";
		if(args.containsKeyAndNotNull("voice") || args.containsKeyAndNotNull("language")){
			if(args.containsKeyAndNotNull("language")){
				_voice = args.getString("language");
			}else{
				_voice = args.getString("voice");
			}
			if(_voice!="auto"){
				if(isLanguageAvailable(_voice)){
					_tts.setLanguage(toLocale(_voice));	
				}else{
					Log.e(_logName,"Unsupported Language provided.");					
				}				
			}			
		}
				
		if(args.containsKeyAndNotNull("rate")){
			double dRate = args.getDouble("rate");
			_tts.setSpeechRate((float)(dRate));			
		}
		if(args.containsKeyAndNotNull("pitch")){
			double dPitch = args.getDouble("pitch");
			_tts.setPitch((float)(dPitch));		
		}		
		
		//Need to add this so OnUtteranceCompletedListener will fire
		HashMap<String, String> options = new HashMap<String, String>();
		options.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "FINISHED PLAYING");
		  
		_tts.speak(_text, TextToSpeech.QUEUE_FLUSH, options);
		
		doListener("started");
	
	}
	
	@Kroll.method
	@SuppressWarnings("rawtypes")
	public void pauseSpeaking(@Kroll.argument(optional=true) HashMap hm){
		Log.d(_logName, "Android does not support pauseSpeaking, this method is for parity only");
	}
	
	@Kroll.method
	@SuppressWarnings("rawtypes")
	public void stopSpeaking(@Kroll.argument(optional=true) HashMap hm){
		if(_tts.isSpeaking()){
			_tts.stop();
		}
		doListener("stopped");
	}


	@Override
	public void onDestroy(Activity arg0) {
		if(_tts!=null){
			if(_tts.isSpeaking()){
				_tts.stop();
			}	
			_tts.shutdown();
		}
	}


	@Override
	public void onPause(Activity arg0) {}

	@Override
	public void onResume(Activity arg0) {}


	@Override
	public void onStart(Activity arg0) {}


	@Override
	public void onStop(Activity arg0) {	}


	@Override
	public void listenerAdded(String arg0, int arg1, KrollProxy arg2) {}


	@Override
	public void listenerRemoved(String arg0, int arg1, KrollProxy arg2) {}


	@Override
	public void processProperties(KrollDict arg0) {}

	@Override
	public void propertiesChanged(List<KrollPropertyChange> arg0,
			KrollProxy arg1) {}


	@Override
	public void propertyChanged(String arg0, Object arg1, Object arg2,
			KrollProxy arg3) {}
	
}
