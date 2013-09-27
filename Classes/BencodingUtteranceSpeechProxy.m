/**
 * Copyright (c) 2013 by Benjamin Bahrenburg. All Rights Reserved.
 * Licensed under the terms of the Apache 2.0 License
 * Please see the LICENSE included with this distribution for details.
 *
 * Available at https://github.com/benbahrenburg/Utterance
 *
 */

#import "BencodingUtteranceSpeechProxy.h"
#import "TiUtils.h"
@implementation BencodingUtteranceSpeechProxy

int const cSpeechBoundaryImmeiate = 0;
int const cSpeechBoundaryWord = 1;


-(void)_configure
{
    _isSupported = NO;
    
    if(NSClassFromString(@"AVSpeechSynthesizer"))
    {
        _isSupported=YES;
    }
    
    if(_isSupported){
        self.speechSynthesizer = [AVSpeechSynthesizer new];
        self.speechSynthesizer.delegate = self;
    }

	[super _configure];
}

-(void)_destroy
{
    self.speechSynthesizer = nil;
    _text = nil;
    _voice = nil;
    
	[super _destroy];
}

/**
 * Language detection taken from Eric Wolfe's contribution to Hark https://github.com/kgn/Hark
 */
- (NSString *)voiceLanguageForText:(NSString *)text
{
    CFRange range = CFRangeMake(0, MIN(400, text.length));
    NSString *currentLanguage = [AVSpeechSynthesisVoice currentLanguageCode];
    NSString *language = (NSString *)CFBridgingRelease(CFStringTokenizerCopyBestStringLanguage((CFStringRef)text, range));
    if(language && ![currentLanguage hasPrefix:language]){
        NSArray *availableLanguages = [[AVSpeechSynthesisVoice speechVoices] valueForKeyPath:@"language"];
        if([availableLanguages containsObject:language]){
            return language;
        }
        
        // TODO: also support Cantonese (zh-HK)
        // Language code translations for simplified and traditional Chinese
        if([language isEqualToString:@"zh-Hans"]){
            return @"zh-CN";
        }
        if([language isEqualToString:@"zh-Hant"]){
            return @"zh-TW";
        }
        
        // Fall back to searching for languages starting with the current language code
        NSString *languageCode = [[language componentsSeparatedByString:@"-"] firstObject];
        for(NSString *language in availableLanguages){
            if([language hasPrefix:languageCode]){
                NSLog(@"[DEBUG] using default: %@", language);
                return language;
            }
        }
    }
    
    return currentLanguage;
}

#pragma Public APIs

-(NSNumber*) isSupported:(id)unused
{
    return NUMBOOL(_isSupported);
}

-(void)startSpeaking:(id)args
{
    ENSURE_SINGLE_ARG(args,NSDictionary);
    
    if(!_isSupported){
        if ([self _hasListeners:@"errored"]) {
            NSDictionary *errorEvent = [NSDictionary dictionaryWithObjectsAndKeys:
                                        @"iOS 7 or greater is required for this feature",@"error",
                                        nil
                                        ];
            
            [self fireEvent:@"errored" withObject:errorEvent];
        }
        return;
    }
    
    if(_isSpeaking){
        NSLog(@"[DEBUG] Already speaking");
        return;
    }
    
    if([args valueForKey:@"text"] == nil){
        if ([self _hasListeners:@"errored"]) {
            NSDictionary *errorEvent = [NSDictionary dictionaryWithObjectsAndKeys:
                                        @"text parameter is required",@"error",
                                        nil
                                        ];
            
            [self fireEvent:@"errored" withObject:errorEvent];
        }
        return;
    }
    
    _text = [TiUtils stringValue:@"text" properties:args];
    _voice = [TiUtils stringValue:@"voice" properties:args def:@"auto"];
    
    AVSpeechUtterance *utterance = [AVSpeechUtterance speechUtteranceWithString:_text];
    
    if( [_voice caseInsensitiveCompare:@"auto"] == NSOrderedSame )
    {
        _voice = [self voiceLanguageForText:_text];
    }
    
    utterance.voice = [AVSpeechSynthesisVoice voiceWithLanguage:_voice];
    
    if([args valueForKey:@"rate"] != nil){
        float rate = [TiUtils floatValue:@"rate" properties:args def:AVSpeechUtteranceDefaultSpeechRate];
        if((rate >=AVSpeechUtteranceMinimumSpeechRate)||(rate<=AVSpeechUtteranceMaximumSpeechRate)){
            utterance.rate = rate;
        }else{
            NSLog(@"[ERROR] provided rate %f must be between %f and %f", rate,AVSpeechUtteranceMinimumSpeechRate,AVSpeechUtteranceMaximumSpeechRate);
        }
    }
    
    if([args valueForKey:@"pitchMultiplier"] != nil){
        float pitchMultiplier = [TiUtils floatValue:@"pitchMultiplier" properties:args def:1];
        if((pitchMultiplier >=0.5f)||(pitchMultiplier<=2.0f)){
            utterance.pitchMultiplier = pitchMultiplier;
        }else{
            NSLog(@"[ERROR] provided pitchMultiplier %f must be between 0.5 and 2", pitchMultiplier);
        }
    }
    
    if([args valueForKey:@"volume"] != nil){
        float volume = [TiUtils floatValue:@"volume" properties:args def:1];
        if((volume >=0.0f)||(volume<=1.0f)){
            utterance.volume = volume;
        }else{
            NSLog(@"[ERROR] provided volume %f must be between 0 and 1", volume);
        }
    }
    
    if([args valueForKey:@"preUtteranceDelay"] != nil){
        float preUtteranceDelay = [TiUtils floatValue:@"preUtteranceDelay" properties:args def:0.0f];
        utterance.preUtteranceDelay = [[NSNumber numberWithFloat:preUtteranceDelay] doubleValue];
    }
    
    if([args valueForKey:@"postUtteranceDelay"] != nil){
        float postUtteranceDelay = [TiUtils floatValue:@"postUtteranceDelay" properties:args def:0.0f];
        utterance.postUtteranceDelay = [[NSNumber numberWithFloat:postUtteranceDelay] doubleValue];
    }
    
    [self.speechSynthesizer speakUtterance:utterance];
    _isSpeaking = YES;
}

-(void)continueSpeaking:(id)unused
{
    if(!_isSpeaking){
        [self.speechSynthesizer continueSpeaking];
    }
    _isSpeaking = YES;
}
-(void)pauseSpeaking:(id)value
{
    if(_isSpeaking){
        if(value !=nil){
            ENSURE_SINGLE_ARG(value, NSNumber);
            if([value integerValue] == cSpeechBoundaryWord){
                [self.speechSynthesizer pauseSpeakingAtBoundary:AVSpeechBoundaryWord];
                NSLog(@"[DEBUG] pausing at word boundary");
            }else{
                [self.speechSynthesizer pauseSpeakingAtBoundary:AVSpeechBoundaryImmediate];
                NSLog(@"[DEBUG] pausing immediately");
            }
        }else{
            [self.speechSynthesizer pauseSpeakingAtBoundary:AVSpeechBoundaryImmediate];
        }
    }
    _isSpeaking = NO;
}
-(void)stopSpeaking:(id)value
{
    if(_isSpeaking){
        if(value !=nil){
            ENSURE_SINGLE_ARG(value, NSNumber);
            if([value integerValue] == cSpeechBoundaryWord){
                [self.speechSynthesizer stopSpeakingAtBoundary:AVSpeechBoundaryWord];
                NSLog(@"[DEBUG] stopped at word boundary");
            }else{
                [self.speechSynthesizer stopSpeakingAtBoundary:AVSpeechBoundaryImmediate];
                NSLog(@"[DEBUG] stopped immediately");
            }
        }else{
            [self.speechSynthesizer stopSpeakingAtBoundary:AVSpeechBoundaryImmediate];
        }
    }
    _isSpeaking = NO;
}

-(id)isSpeaking
{
	return NUMBOOL(_isSpeaking);
}

-(void) doCallListener:(NSString*)name
{
    if ([self _hasListeners:name]) {
        NSDictionary *event = [NSDictionary dictionaryWithObjectsAndKeys:
                               NUMBOOL(YES),@"success",
                               NUMBOOL(_isSpeaking),@"speaking",
                               _text,@"text",
                               _voice,@"voice",
                               nil
                               ];
        
        [self fireEvent:name withObject:event];
    }
}
- (void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didCancelSpeechUtterance:(AVSpeechUtterance *)utterance{
    _isSpeaking = NO;
    [self doCallListener:@"canceled"];
}

- (void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didContinueSpeechUtterance:(AVSpeechUtterance *)utterance{
    _isSpeaking = YES;
    [self doCallListener:@"continued"];
}

- (void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didFinishSpeechUtterance:(AVSpeechUtterance *)utterance{
    _isSpeaking = NO;
    [self doCallListener:@"completed"];
}

- (void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didPauseSpeechUtterance:(AVSpeechUtterance *)utterance{
    _isSpeaking = NO;
    [self doCallListener:@"paused"];
}

- (void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didStartSpeechUtterance:(AVSpeechUtterance *)utterance{
    _isSpeaking = YES;
    [self doCallListener:@"started"];
}

MAKE_SYSTEM_PROP(DEFAULT_SPEECH_RATE,AVSpeechUtteranceDefaultSpeechRate);
MAKE_SYSTEM_PROP(MIN_SPEECH_RATE,AVSpeechUtteranceMinimumSpeechRate);
MAKE_SYSTEM_PROP(MAX_SPEECH_RATE,AVSpeechUtteranceMaximumSpeechRate);

MAKE_SYSTEM_PROP(SPEECH_BOUNDARY_IMMEDIATE,cSpeechBoundaryImmeiate);
MAKE_SYSTEM_PROP(SPEECH_BOUNDARY_WORD,cSpeechBoundaryWord);

@end
