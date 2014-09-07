/**
 * Utterance Speech to Text and Text to Speech
 * Copyright (c) 2010-2014 by Benjamin Bahrenburg. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package bencoding.utterance;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;

import android.os.Build;

@Kroll.module(name="Utterance", id="bencoding.utterance")
public class UtteranceModule extends KrollModule
{

	public static final String MODULE_FULL_NAME = "bencoding.utterance";

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;
	
	public UtteranceModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		//Log.d(TAG, "inside onAppCreate");
		// put module init code that needs to run when the application is created
	}
	
	@Kroll.getProperty @Kroll.method
	public Boolean isSupported(){
		return (Build.VERSION.SDK_INT > 18);
	}	
}

