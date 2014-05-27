/**
 * Utterance Speech to Text and Text to Speech
 * Copyright (c) 2010-2014 by Benjamin Bahrenburg. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package bencoding.utterance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollPropertyChange;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.KrollProxyListener;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiActivityResultHandler;
import org.appcelerator.titanium.util.TiActivitySupport;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.util.Log;

@Kroll.proxy(creatableInModule=UtteranceModule.class)
public class SpeechToTextProxy extends KrollProxy implements TiActivityResultHandler, KrollProxyListener{
	private Boolean _isSupported = true;
	private static final String PROPERTY_PROMPT = "promptText";
	private static final String MAX_RESULTS = "maxResults";
	private static final String LANGUAGE_MODEL = "languageModel";
	private static final String EVENT_COMPLETED = "completed";
	private static final String EVENT_STARTED = "started";
	private static final int INTENT_ID = 30987;
	
	@Kroll.constant public static final String LANGUAGE_MODEL_FREE_FORM = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
	@Kroll.constant public static final String LANGUAGE_MODEL_WEB_SEARCH = RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH;
	
	public SpeechToTextProxy(){		
		super();

		//find out whether speech recognition is supported
		PackageManager packManager = TiApplication.getInstance().getPackageManager();

		List<ResolveInfo> intActivities = packManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		_isSupported = (intActivities.size() != 0) ;
		
	}
	
	@Kroll.method @Kroll.getProperty
	public Boolean isSupport(){
		return _isSupported;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Kroll.method
	public void startSpeechToText(HashMap hm) {
		KrollDict args = new KrollDict(hm);
		
		//start the speech recognition intent passing required data
		Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		//indicate package
		listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
		
		if(args.containsKeyAndNotNull(PROPERTY_PROMPT)){
			//message to display while listening
			listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, args.getString(PROPERTY_PROMPT));			
		}

		//set speech model
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, args.optString(LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM));
		
		//specify number of results to retrieve
		listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, args.optInt(MAX_RESULTS, 10));

		final Activity activity = TiApplication.getAppCurrentActivity();
		final TiActivitySupport activitySupport = (TiActivitySupport) activity;
		activitySupport.launchActivityForResult(listenIntent, INTENT_ID, this);
		
		if (hasListeners(EVENT_STARTED)) {
			HashMap<String, Object> event = new HashMap<String, Object>();
			event.put("success",true);
			fireEvent(EVENT_STARTED, event);
			Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_STARTED+ " fired");
		}else{
			Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_STARTED+ " not found");
		}  

	}

	@Override
	public void onError(Activity activity, int requestCode, Exception error) {
		Log.d(UtteranceModule.MODULE_FULL_NAME, "onError : Errored");
		if (hasListeners(EVENT_COMPLETED)) {
			HashMap<String, Object> event = new HashMap<String, Object>();
			event.put("success",false);
			event.put("message",error.getMessage());
			fireEvent(EVENT_COMPLETED, event);
			Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_COMPLETED+ " fired");
		}else{
			Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_COMPLETED+ " not found");
		}  		
	}

	@Override
	public void onResult(Activity activity, int requestCode, int resultCode, Intent data){

		//store the returned word list as an ArrayList
		ArrayList<String> suggestedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		int wordCount = suggestedWords.size();
        Object[] results = new Object[wordCount];
        if (suggestedWords != null && wordCount > 0) {            	  
          	  for (int iLoop = 0; iLoop < wordCount; iLoop++) {
          		results[iLoop]=suggestedWords.get(iLoop);
          		}
        }
		if (hasListeners(EVENT_COMPLETED)) {
			HashMap<String, Object> event = new HashMap<String, Object>();
			event.put("success",true);
			event.put("wordCount",wordCount);
			event.put("words",results);
			fireEvent(EVENT_COMPLETED, event);
			Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_COMPLETED+ " fired");
		}else{
			Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_COMPLETED+ " not found");
		}  
	}

	@Override
	public void listenerAdded(String arg0, int arg1, KrollProxy arg2) {}

	@Override
	public void listenerRemoved(String arg0, int arg1, KrollProxy arg2) {}

	@Override
	public void processProperties(KrollDict arg0) {}

	@Override
	public void propertiesChanged(List<KrollPropertyChange> arg0, KrollProxy arg1) {}

	@Override
	public void propertyChanged(String arg0, Object arg1, Object arg2, KrollProxy arg3) {}

}
