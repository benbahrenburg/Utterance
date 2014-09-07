package bencoding.utterance;

import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollPropertyChange;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.KrollProxyListener;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.util.TiActivityResultHandler;
import org.appcelerator.titanium.util.TiActivitySupport;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

@Kroll.proxy(creatableInModule=UtteranceModule.class)
public class InstallTTSDataProxy extends KrollProxy implements TiActivityResultHandler, KrollProxyListener {

	private static final int CHECK_INSTALL_INTENT_ID = 44400;
	private static final int INSTALL_INTENT_ID = 54400;
	private static final String EVENT_INSTALL_CHECK = "installcheck";
	
	public InstallTTSDataProxy(){		
		super();
	}

	@Kroll.method
	public void CheckDataInstalled()
	{
		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		final TiActivitySupport activitySupport = (TiActivitySupport) activity;
		activitySupport.launchActivityForResult(checkTTSIntent, CHECK_INSTALL_INTENT_ID, this);		
	}

	@Kroll.method
	public void InstallData()
	{
		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
		final TiActivitySupport activitySupport = (TiActivitySupport) activity;
		activitySupport.launchActivityForResult(checkTTSIntent, INSTALL_INTENT_ID, this);		
	}
	
	@Override
	public void listenerAdded(String arg0, int arg1, KrollProxy arg2) {
		
	}

	@Override
	public void listenerRemoved(String arg0, int arg1, KrollProxy arg2) {
		
	}

	@Override
	public void processProperties(KrollDict arg0) {}

	@Override
	public void propertiesChanged(List<KrollPropertyChange> arg0,KrollProxy arg1) {	}

	@Override
	public void propertyChanged(String arg0, Object arg1, Object arg2, KrollProxy arg3) {}

	@Override
	public void onError(Activity activity, int requestCode, Exception error) {	
		Log.d(UtteranceModule.MODULE_FULL_NAME, "onError : Errored");
		if (requestCode == CHECK_INSTALL_INTENT_ID) {
			if (hasListeners(EVENT_INSTALL_CHECK)) {
				HashMap<String, Object> event = new HashMap<String, Object>();
				event.put("success",false);
				event.put("requestCode",requestCode);
				event.put("message",error.getMessage());
				fireEvent(EVENT_INSTALL_CHECK, event);
				Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_INSTALL_CHECK+ " fired");
			}else{
				Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_INSTALL_CHECK+ " not found");
			}  			
		}
	}

	@Override
	public void onResult(Activity activity, int requestCode, int resultCode, Intent data) {
		Log.d(UtteranceModule.MODULE_FULL_NAME, "onResult : onResult");
		Log.d(UtteranceModule.MODULE_FULL_NAME, "onResult : requestCode" + requestCode);
		
		try{
		    if (requestCode == CHECK_INSTALL_INTENT_ID) {		    	
				if (hasListeners(EVENT_INSTALL_CHECK)) {
					String msg = (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) ? "TTS Data Installed" : "TTS Data Not installed";
					HashMap<String, Object> event = new HashMap<String, Object>();
					event.put("success",(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) );
					event.put("requestCode",requestCode);
					event.put("message",msg);
					fireEvent(EVENT_INSTALL_CHECK, event);
					Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_INSTALL_CHECK+ " fired");
				}else{
					Log.d(UtteranceModule.MODULE_FULL_NAME,"event: " + EVENT_INSTALL_CHECK+ " not found");
				}				
		    }
		    
		}catch(Exception error){
			Log.e(UtteranceModule.MODULE_FULL_NAME, error.getMessage());
			error.printStackTrace();
		}
	}

}
