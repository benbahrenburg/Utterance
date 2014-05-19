/**
 * Copyright (c) 2013 by Benjamin Bahrenburg. All Rights Reserved.
 * Licensed under the terms of the Apache 2.0 License
 * Please see the LICENSE included with this distribution for details.
 *
 * Available at https://github.com/benbahrenburg/Utterance
 *
 */
#import "BencodingUtteranceModule.h"
#import "TiBase.h"
#import "TiHost.h"
#import "TiUtils.h"

@implementation BencodingUtteranceModule

#pragma mark Internal

// this is generated for your module, please do not change it
-(id)moduleGUID
{
	return @"3ffcde48-e965-4c88-8dc6-2c7150da60fb";
}

// this is generated for your module, please do not change it
-(NSString*)moduleId
{
	return @"bencoding.utterance";
}

#pragma mark Lifecycle

-(void)startup
{
    _isSupported = NO;

    if(NSClassFromString(@"AVSpeechSynthesizer"))
    {
        _isSupported=YES;
    }

	// you *must* call the superclass
	[super startup];
}

-(void)shutdown:(id)sender
{	
	// you *must* call the superclass
	[super shutdown:sender];
}

#pragma mark Cleanup 


#pragma mark Internal Memory Management

-(void)didReceiveMemoryWarning:(NSNotification*)notification
{
	// optionally release any resources that can be dynamically
	// reloaded once memory is available - such as caches
	[super didReceiveMemoryWarning:notification];
}


#pragma Public APIs

-(NSNumber*) isSupported:(id)unused
{
    return NUMBOOL(_isSupported);
}

@end
