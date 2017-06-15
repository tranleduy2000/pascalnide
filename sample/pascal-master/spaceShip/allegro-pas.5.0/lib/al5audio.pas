UNIT al5audio;
(* See readme.txt for copyright information.
 *)
(* Copyright (c) 2012 Guillermo Martínez J.

  This software is provided 'as-is', without any express or implied
  warranty. In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

    1. The origin of this software must not be misrepresented; you must not
    claim that you wrote the original software. If you use this software
    in a product, an acknowledgment in the product documentation would be
    appreciated but is not required.

    2. Altered source versions must be plainly marked as such, and must not be
    misrepresented as being the original software.

    3. This notice may not be removed or altered from any source
    distribution.
 *)


{$include allegro.cfg}

INTERFACE

  USES
    Allegro5, al5base;

  CONST
  (* Internal, used to communicate with acodec.
   * Must be in 512 <= n < 1024 *)
    _KCM_STREAM_FEEDER_QUIT_EVENT_TYPE = 512;

  (* User event type emitted when a stream fragment is ready to be
   * refilled with more audio data.
   * Must be in 512 <= n < 1024 *)
    ALLEGRO_EVENT_AUDIO_STREAM_FRAGMENT = 513;
    ALLEGRO_EVENT_AUDIO_STREAM_FINISHED = 514;

    ALLEGRO_MAX_CHANNELS = 8;

  TYPE
  (* Sample depth and type, and signedness. Mixers only use 32-bit signed float
    (-1..+1). The unsigned value is a bit-flag applied to the depth value.
   *)
    ALLEGRO_AUDIO_DEPTH = (
      ALLEGRO_AUDIO_DEPTH_INT8     := $00,
      ALLEGRO_AUDIO_DEPTH_INT16    := $01,
      ALLEGRO_AUDIO_DEPTH_INT24    := $02,
      ALLEGRO_AUDIO_DEPTH_FLOAT32  := $03,

      ALLEGRO_AUDIO_DEPTH_UNSIGNED := $08,

    (* For convenience *)
      ALLEGRO_AUDIO_DEPTH_UINT16 := $09, {<ALLEGRO_AUDIO_DEPTH_INT16 OR ALLEGRO_AUDIO_DEPTH_UNSIGNED, }
      ALLEGRO_AUDIO_DEPTH_UINT24 := $0A  {<ALLEGRO_AUDIO_DEPTH_INT24 OR ALLEGRO_AUDIO_DEPTH_UNSIGNED  }
    );

  CONST
    ALLEGRO_AUDIO_DEPTH_UINT8  = ALLEGRO_AUDIO_DEPTH_UNSIGNED; {<ALLEGRO_AUDIO_DEPTH_INT8 OR ALLEGRO_AUDIO_DEPTH_UNSIGNED, }



  TYPE
  (* Speaker configuration (mono, stereo, 2.1, 3, etc). With regards to
    behavior, most of this code makes no distinction between, say, 4.1 and 5
    speaker setups.. they both have 5 "channels". However, users would like the
    distinction, and later when the higher-level stuff is added, the
    differences will become more important. (v>>4)+(v&0xF) should yield the
    total channel count.
   *)
    ALLEGRO_CHANNEL_CONF = (
      ALLEGRO_CHANNEL_CONF_1   := $10,
      ALLEGRO_CHANNEL_CONF_2   := $20,
      ALLEGRO_CHANNEL_CONF_3   := $30,
      ALLEGRO_CHANNEL_CONF_4   := $40,
      ALLEGRO_CHANNEL_CONF_5_1 := $51,
      ALLEGRO_CHANNEL_CONF_6_1 := $61,
      ALLEGRO_CHANNEL_CONF_7_1 := $71
    );



    ALLEGRO_PLAYMODE = (
      ALLEGRO_PLAYMODE_ONCE   := $100,
      ALLEGRO_PLAYMODE_LOOP   := $101,
      ALLEGRO_PLAYMODE_BIDIR  := $102
    {
      _ALLEGRO_PLAYMODE_STREAM_ONCE   = 0x103,   /* internal */
      _ALLEGRO_PLAYMODE_STREAM_ONEDIR = 0x104    /* internal */
    }
    );



    ALLEGRO_MIXER_QUALITY = (
      ALLEGRO_MIXER_QUALITY_POINT   := $110,
      ALLEGRO_MIXER_QUALITY_LINEAR  := $111
    );

  CONST
    ALLEGRO_AUDIO_PAN_NONE: AL_FLOAT = -1000.0;

  TYPE
    ALLEGRO_SAMPLEptr = AL_POINTER;



    ALLEGRO_SAMPLE_IDptr = ^ALLEGRO_SAMPLE_ID;
    ALLEGRO_SAMPLE_ID = RECORD
      _index, _id: AL_INT;
    END;

    ALLEGRO_SAMPLE_INSTANCEptr = AL_POINTER;

    ALLEGRO_AUDIO_STREAMptr = AL_POINTER;

    ALLEGRO_MIXERptr = AL_POINTER;

    ALLEGRO_VOICEptr = AL_POINTER;

    ALLEGRO_MIXER_CALLBACK = PROCEDURE (buf: AL_VOIDptr; samples: AL_UINT; data: AL_VOIDptr); CDECL;

    ALLEGRO_SAMPLE_LOADER = FUNCTION (CONST filename: AL_STRptr): ALLEGRO_SAMPLEptr; CDECL;

    ALLEGRO_SAMPLE_SAVER = FUNCTION (CONST filename: AL_STRptr; spl: ALLEGRO_SAMPLEptr): AL_BOOL; CDECL;

    ALLEGRO_AUDIO_STREAM_LOADER = FUNCTION (CONST filename: AL_STRptr; buffer_count: AL_SIZE_T; samples: AL_UINT): ALLEGRO_AUDIO_STREAMptr;



(* Sample functions *)
  FUNCTION al_create_sample (buf: AL_VOIDptr; samples, freq: AL_UINT; depth: ALLEGRO_AUDIO_DEPTH; chan_conf: ALLEGRO_CHANNEL_CONF; free_buf: AL_BOOL): ALLEGRO_SAMPLEptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_destroy_sample (spl: ALLEGRO_SAMPLEptr); CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

(* Sample instance functions *)
  FUNCTION al_create_sample_instance (data: ALLEGRO_SAMPLEptr): ALLEGRO_SAMPLE_INSTANCEptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_destroy_sample_instance (spl: ALLEGRO_SAMPLE_INSTANCEptr); CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_sample_frequency (CONST spl: ALLEGRO_SAMPLEptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_length (CONST spl: ALLEGRO_SAMPLEptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_depth (CONST spl: ALLEGRO_SAMPLEptr): ALLEGRO_AUDIO_DEPTH; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_channels (CONST spl: ALLEGRO_SAMPLEptr): ALLEGRO_CHANNEL_CONF; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_data (CONST spl: ALLEGRO_SAMPLEptr): AL_VOIDptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_sample_instance_frequency (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_instance_length (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_instance_position (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_sample_instance_speed (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_FLOAT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_instance_gain (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_FLOAT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_instance_pan (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_FLOAT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_instance_time (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_FLOAT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_sample_instance_depth (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): ALLEGRO_AUDIO_DEPTH; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_instance_channels (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): ALLEGRO_CHANNEL_CONF; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_instance_playmode (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): ALLEGRO_PLAYMODE; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_sample_instance_playing (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample_instance_attached (CONST spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_set_sample_instance_position (spl: ALLEGRO_SAMPLE_INSTANCEptr; val: AL_UINT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_sample_instance_length (spl: ALLEGRO_SAMPLE_INSTANCEptr; val: AL_UINT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_set_sample_instance_speed (spl: ALLEGRO_SAMPLE_INSTANCEptr; val: AL_FLOAT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_sample_instance_gain (spl: ALLEGRO_SAMPLE_INSTANCEptr; val: AL_FLOAT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_sample_instance_pan (spl: ALLEGRO_SAMPLE_INSTANCEptr; val: AL_FLOAT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_set_sample_instance_playmode (spl: ALLEGRO_SAMPLE_INSTANCEptr; val: ALLEGRO_PLAYMODE): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_set_sample_instance_playing (spl: ALLEGRO_SAMPLE_INSTANCEptr; val: AL_BOOL): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_detach_sample_instance (spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_set_sample (spl: ALLEGRO_SAMPLE_INSTANCEptr; data: ALLEGRO_SAMPLEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_sample (spl: ALLEGRO_SAMPLE_INSTANCEptr): ALLEGRO_SAMPLEptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_play_sample_instance (spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_stop_sample_instance (spl: ALLEGRO_SAMPLE_INSTANCEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

(* Stream functions *)
  FUNCTION al_create_audio_stream (buffer_count: AL_SIZE_T; samples, freq: AL_UINT; depth: ALLEGRO_AUDIO_DEPTH; chan_conf: ALLEGRO_CHANNEL_CONF): ALLEGRO_AUDIO_STREAMptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_destroy_audio_stream (stream: ALLEGRO_AUDIO_STREAMptr); CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_drain_audio_stream (stream: ALLEGRO_AUDIO_STREAMptr); CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_audio_stream_frequency (CONST stream: ALLEGRO_AUDIO_STREAMptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_stream_length (CONST stream: ALLEGRO_AUDIO_STREAMptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_stream_fragments (CONST stream: ALLEGRO_AUDIO_STREAMptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_available_audio_stream_fragments (CONST stream: ALLEGRO_AUDIO_STREAMptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_audio_stream_speed (CONST stream: ALLEGRO_AUDIO_STREAMptr): AL_FLOAT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_stream_gain (CONST stream: ALLEGRO_AUDIO_STREAMptr): AL_FLOAT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_stream_pan (CONST stream: ALLEGRO_AUDIO_STREAMptr): AL_FLOAT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_audio_stream_channels (CONST stream: ALLEGRO_AUDIO_STREAMptr): ALLEGRO_CHANNEL_CONF; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_stream_depth (CONST stream: ALLEGRO_AUDIO_STREAMptr): ALLEGRO_AUDIO_DEPTH; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_stream_playmode (CONST stream: ALLEGRO_AUDIO_STREAMptr): ALLEGRO_PLAYMODE; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_audio_stream_playing (CONST spl: ALLEGRO_AUDIO_STREAMptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_stream_attached (CONST spl: ALLEGRO_AUDIO_STREAMptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_audio_stream_fragment (CONST stream: ALLEGRO_AUDIO_STREAMptr): AL_VOIDptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_set_audio_stream_speed (stream: ALLEGRO_AUDIO_STREAMptr; val: AL_FLOAT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_audio_stream_gain (stream: ALLEGRO_AUDIO_STREAMptr; val: AL_FLOAT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_audio_stream_pan (stream: ALLEGRO_AUDIO_STREAMptr; val: AL_FLOAT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_set_audio_stream_playmode (stream: ALLEGRO_AUDIO_STREAMptr; val: ALLEGRO_PLAYMODE): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_set_audio_stream_playing (stream: ALLEGRO_AUDIO_STREAMptr; val: AL_BOOL): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_detach_audio_stream (stream: ALLEGRO_AUDIO_STREAMptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_audio_stream_fragment (stream: ALLEGRO_AUDIO_STREAMptr; val: AL_VOIDptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_rewind_audio_stream (stream: ALLEGRO_AUDIO_STREAMptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_seek_audio_stream_secs (stream: ALLEGRO_AUDIO_STREAMptr; time: AL_DOUBLE): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_stream_position_secs (stream: ALLEGRO_AUDIO_STREAMptr): AL_DOUBLE; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_stream_length_secs (stream: ALLEGRO_AUDIO_STREAMptr): AL_DOUBLE; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_audio_stream_loop_secs (stream: ALLEGRO_AUDIO_STREAMptr; start, finish: AL_DOUBLE): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_audio_stream_event_source (stream: ALLEGRO_AUDIO_STREAMptr): ALLEGRO_EVENT_SOURCEptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

(* Mixer functions *)
  FUNCTION al_create_mixer (freq: AL_UINT; depth: ALLEGRO_AUDIO_DEPTH; chan_conf: ALLEGRO_CHANNEL_CONF): ALLEGRO_MIXERptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_destroy_mixer (mixer: ALLEGRO_MIXERptr); CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_attach_sample_instance_to_mixer (sample: ALLEGRO_SAMPLE_INSTANCEptr; mixer: ALLEGRO_MIXERptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_attach_audio_stream_to_mixer (stream: ALLEGRO_AUDIO_STREAMptr; mixer: ALLEGRO_MIXERptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_attach_mixer_to_mixer (mixerA, mixerB: ALLEGRO_MIXERptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_mixer_postprocess_callback (mixer: ALLEGRO_MIXERptr; cb: ALLEGRO_MIXER_CALLBACK; data: AL_VOIDptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_mixer_frequency (CONST mixer: ALLEGRO_MIXERptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_mixer_channels (CONST mixer: ALLEGRO_MIXERptr): ALLEGRO_CHANNEL_CONF; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_mixer_depth (CONST mixer: ALLEGRO_MIXERptr): ALLEGRO_AUDIO_DEPTH; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_mixer_quality (CONST mixer: ALLEGRO_MIXERptr): ALLEGRO_MIXER_QUALITY; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_mixer_gain (CONST mixer: ALLEGRO_MIXERptr): AL_FLOAT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_mixer_playing (CONST mixer: ALLEGRO_MIXERptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_mixer_attached (CONST mixer: ALLEGRO_MIXERptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_mixer_frequency (mixer: ALLEGRO_MIXERptr; val: AL_UINT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_mixer_quality (mixer: ALLEGRO_MIXERptr; val: ALLEGRO_MIXER_QUALITY): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_mixer_gain (mixer: ALLEGRO_MIXERptr; gain: AL_FLOAT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_mixer_playing (mixer: ALLEGRO_MIXERptr; val: AL_BOOL): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_detach_mixer (mixer: ALLEGRO_MIXERptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

(* Voice functions *)
  FUNCTION al_create_voice (freq: AL_UINT; depth: ALLEGRO_AUDIO_DEPTH; chan_conf: ALLEGRO_CHANNEL_CONF): ALLEGRO_VOICEptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_destroy_voice (voice: ALLEGRO_VOICEptr); CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_attach_sample_instance_to_voice (sample: ALLEGRO_SAMPLE_INSTANCEptr; voice: ALLEGRO_VOICEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_attach_audio_stream_to_voice (stream: ALLEGRO_AUDIO_STREAMptr; voice: ALLEGRO_VOICEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_attach_mixer_to_voice (mixer: ALLEGRO_MIXERptr; voice: ALLEGRO_VOICEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_detach_voice (voice: ALLEGRO_VOICEptr); CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_voice_frequency (CONST voice: ALLEGRO_VOICEptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_voice_position (CONST voice: ALLEGRO_VOICEptr): AL_UINT; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_voice_channels (CONST voice: ALLEGRO_VOICEptr): ALLEGRO_CHANNEL_CONF; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_voice_depth (CONST voice: ALLEGRO_VOICEptr): ALLEGRO_AUDIO_DEPTH; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_voice_playing (CONST voice: ALLEGRO_VOICEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_voice_position (voice: ALLEGRO_VOICEptr; val: AL_UINT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_voice_playing (voice: ALLEGRO_VOICEptr; val: AL_BOOL): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

(* Misc. audio functions *)
  FUNCTION al_install_audio: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_uninstall_audio; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_is_audio_installed: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_allegro_audio_version: AL_UINT32; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

  FUNCTION al_get_channel_count (conf: ALLEGRO_CHANNEL_CONF): AL_SIZE_T; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_audio_depth_size (conf: ALLEGRO_AUDIO_DEPTH): AL_SIZE_T; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

(* Simple audio layer *)
  FUNCTION al_reserve_samples (reserve_samples: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_get_default_mixer: ALLEGRO_MIXERptr; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_set_default_mixer (mixer: ALLEGRO_MIXERptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_restore_default_mixer: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  FUNCTION al_play_sample (data: ALLEGRO_SAMPLEptr; gain, pan, speed: AL_FLOAT; loop: ALLEGRO_PLAYMODE; ret_id: ALLEGRO_SAMPLE_IDptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_stop_sample (spl_id: ALLEGRO_SAMPLE_IDptr); CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;
  PROCEDURE al_stop_samples; CDECL;
    EXTERNAL ALLEGRO_AUDIO_LIB_NAME;

(* File type handlers *)
  FUNCTION al_register_sample_loader (CONST ext: STRING; loader: ALLEGRO_SAMPLE_LOADER): AL_BOOL; INLINE;
  FUNCTION al_register_sample_saver (CONST ext: STRING; saver: ALLEGRO_SAMPLE_SAVER): AL_BOOL; INLINE;
  FUNCTION al_register_audio_stream_loader (CONST ext: STRING; stream_loader: ALLEGRO_AUDIO_STREAM_LOADER): AL_BOOL; INLINE;

  {TODO: These needs Allegro's file access.

ALLEGRO_KCM_AUDIO_FUNC(bool, al_register_sample_loader_f, (const char *ext,
	ALLEGRO_SAMPLE *( *loader)(ALLEGRO_FILE *fp)));
ALLEGRO_KCM_AUDIO_FUNC(bool, al_register_sample_saver_f, (const char *ext,
	bool ( *saver)(ALLEGRO_FILE *fp, ALLEGRO_SAMPLE *spl)));
ALLEGRO_KCM_AUDIO_FUNC(bool, al_register_audio_stream_loader_f, (const char *ext,
	ALLEGRO_AUDIO_STREAM *( *stream_loader)(ALLEGRO_FILE *fp,
	    size_t buffer_count, unsigned int samples)));
	  }

  FUNCTION al_load_sample (CONST filename: STRING): ALLEGRO_SAMPLEptr; INLINE;
  FUNCTION al_save_sample (CONST filename: STRING; spl: ALLEGRO_SAMPLEptr): AL_BOOL; INLINE;
  FUNCTION al_load_audio_stream (CONST filename: STRING; buffer_count: AL_SIZE_T; samples: AL_UINT): ALLEGRO_AUDIO_STREAMptr; INLINE;

  {TODO: These needs Allegro's file access.

ALLEGRO_KCM_AUDIO_FUNC(ALLEGRO_SAMPLE *, al_load_sample_f, (ALLEGRO_FILE* fp, const char *ident));
ALLEGRO_KCM_AUDIO_FUNC(bool, al_save_sample_f, (ALLEGRO_FILE* fp, const char *ident,
	ALLEGRO_SAMPLE *spl));
ALLEGRO_KCM_AUDIO_FUNC(ALLEGRO_AUDIO_STREAM *, al_load_audio_stream_f, (ALLEGRO_FILE* fp, const char *ident,
	size_t buffer_count, unsigned int samples));
      }

IMPLEMENTATION

(* File type handlers *)
  FUNCTION _al_register_sample_loader_ (CONST ext: AL_STRptr; loader: ALLEGRO_SAMPLE_LOADER): AL_BOOL; CDECL;
  EXTERNAL ALLEGRO_AUDIO_LIB_NAME NAME 'al_register_sample_loader';

  FUNCTION al_register_sample_loader (CONST ext: STRING; loader: ALLEGRO_SAMPLE_LOADER): AL_BOOL;
  BEGIN
    al_register_sample_loader := _al_register_sample_loader_ (AL_STRptr (Ext), loader);
  END;



  FUNCTION _al_register_sample_saver_ (CONST ext: AL_STRptr; saver: ALLEGRO_SAMPLE_SAVER): AL_BOOL; CDECL;
  EXTERNAL ALLEGRO_AUDIO_LIB_NAME NAME 'al_register_sample_saver';

  FUNCTION al_register_sample_saver (CONST ext: STRING; saver: ALLEGRO_SAMPLE_SAVER): AL_BOOL;
  BEGIN
    al_register_sample_saver := _al_register_sample_saver_ (AL_STRptr (Ext), saver);
  END;



  FUNCTION _al_register_audio_stream_loader_ (CONST ext: AL_STRptr; stream_loader: ALLEGRO_AUDIO_STREAM_LOADER): AL_BOOL; CDECL;
  EXTERNAL ALLEGRO_AUDIO_LIB_NAME NAME 'al_register_audio_stream_loader';

  FUNCTION al_register_audio_stream_loader (CONST ext: STRING; stream_loader: ALLEGRO_AUDIO_STREAM_LOADER): AL_BOOL;
  BEGIN
    al_register_audio_stream_loader := _al_register_audio_stream_loader_ (AL_STRptr (Ext), stream_loader);
  END;



  FUNCTION _al_load_sample_ (CONST filename: AL_STRptr): ALLEGRO_SAMPLEptr; CDECL;
  EXTERNAL ALLEGRO_AUDIO_LIB_NAME NAME 'al_load_sample';

  FUNCTION al_load_sample (CONST filename: STRING): ALLEGRO_SAMPLEptr;
  BEGIN
    al_load_sample := _al_load_sample_ (AL_STRptr (filename));
  END;



  FUNCTION _al_save_sample_ (CONST filename: AL_STRptr; spl: ALLEGRO_SAMPLEptr): AL_BOOL; CDECL;
  EXTERNAL ALLEGRO_AUDIO_LIB_NAME NAME 'al_save_sample';

  FUNCTION al_save_sample (CONST filename: STRING; spl: ALLEGRO_SAMPLEptr): AL_BOOL;
  BEGIN
    al_save_sample := _al_save_sample_ (AL_STRptr (filename), spl);
  END;



  FUNCTION _al_load_audio_stream_ (CONST filename: AL_STRptr; buffer_count: AL_SIZE_T; samples: AL_UINT): ALLEGRO_AUDIO_STREAMptr; CDECL;
  EXTERNAL ALLEGRO_AUDIO_LIB_NAME NAME 'al_load_audio_stream';

  FUNCTION al_load_audio_stream (CONST filename: STRING; buffer_count: AL_SIZE_T; samples: AL_UINT): ALLEGRO_AUDIO_STREAMptr;
  BEGIN
    al_load_audio_stream := _al_load_audio_stream_ (AL_STRptr (Filename), buffer_count, samples);
  END;

END.
