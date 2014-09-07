package bencoding.utterance;

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

@Kroll.proxy(creatableInModule=UtteranceModule.class)
public class InstallTTSDataProxy extends KrollProxy implements TiActivityResultHandler, KrollProxyListener {

	private static final int CHECK_INTENT_ID = 44400;
	private static final int INSTALL_INTENT_ID = 54400;
	
	public InstallTTSDataProxy(){		
		super();
		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		final TiActivitySupport activitySupport = (TiActivitySupport) activity;
		activitySupport.launchActivityForResult(checkTTSIntent, CHECK_INTENT_ID, this);		
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
	public void onError(Activity arg0, int arg1, Exception arg2) {	
		
	}

	@Override
	public void onResult(Activity activity, int requestCode, int resultCode, Intent data) {
	    if (requestCode == CHECK_INTENT_ID) {
	        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {      
	        	//Success
	        }
	        else {
	            Intent installTTSIntent = new Intent();
	            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	    		final TiActivitySupport activitySupport = (TiActivitySupport) activity;
	    		activitySupport.launchActivityForResult(installTTSIntent, INSTALL_INTENT_ID, this);	
	        }
	    }
	    
//	    if (requestCode == INSTALL_INTENT_ID) {
//	        if (resultCode == TextToSpeech.Engine.) {      
//
//	        }	    	
//	    }
	}

}
