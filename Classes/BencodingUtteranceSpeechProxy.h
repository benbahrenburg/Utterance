/**
 * Copyright (c) 2013 by Benjamin Bahrenburg. All Rights Reserved.
 * Licensed under the terms of the Apache 2.0 License
 * Please see the LICENSE included with this distribution for details.
 *
 * Available at https://github.com/benbahrenburg/Utterance
 *
 */
#import "TiProxy.h"

#import <AVFoundation/AVFoundation.h>
@interface BencodingUtteranceSpeechProxy : TiProxy<AVSpeechSynthesizerDelegate> {

@private
    BOOL _isSpeaking;
    BOOL _isSupported;
    NSString* _text;
    NSString *_voice;
}
@property (strong, nonatomic) AVSpeechSynthesizer *speechSynthesizer;

@end
