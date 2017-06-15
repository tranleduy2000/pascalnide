UNIT al5nativedlg;
(*<See readme.txt for copyright information.
 *)

{$include allegro.cfg}

INTERFACE

  USES
    allegro5, al5base;

  TYPE
    ALLEGRO_FILECHOOSERptr = AL_POINTER;
    ALLEGRO_TEXTLOGptr = AL_POINTER;

  CONST
    ALLEGRO_FILECHOOSER_FILE_MUST_EXIST = 1;
    ALLEGRO_FILECHOOSER_SAVE            = 2;
    ALLEGRO_FILECHOOSER_FOLDER          = 4;
    ALLEGRO_FILECHOOSER_PICTURES        = 8;
    ALLEGRO_FILECHOOSER_SHOW_HIDDEN     = 16;
    ALLEGRO_FILECHOOSER_MULTIPLE        = 32;

    ALLEGRO_MESSAGEBOX_WARN             = 1 SHL 0;
    ALLEGRO_MESSAGEBOX_ERROR            = 1 SHL 1;
    ALLEGRO_MESSAGEBOX_OK_CANCEL        = 1 SHL 2;
    ALLEGRO_MESSAGEBOX_YES_NO           = 1 SHL 3;
    ALLEGRO_MESSAGEBOX_QUESTION         = 1 SHL 4;

    ALLEGRO_TEXTLOG_NO_CLOSE            = 1 SHL 0;
    ALLEGRO_TEXTLOG_MONOSPACE           = 1 SHL 1;

    ALLEGRO_EVENT_NATIVE_DIALOG_CLOSE   = 600;



  FUNCTION al_create_native_file_dialog (CONST initial_path, title, patterns: STRING; Mode: AL_INT): ALLEGRO_FILECHOOSERptr; INLINE;
  FUNCTION al_show_native_file_dialog (display: ALLEGRO_DISPLAYptr; dialog: ALLEGRO_FILECHOOSERptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME;
  FUNCTION al_get_native_file_dialog_count (CONST dialog: ALLEGRO_FILECHOOSERptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME;
  FUNCTION al_get_native_file_dialog_path (CONST dialog: ALLEGRO_FILECHOOSERptr; index: AL_SIZE_T): STRING; INLINE;
  PROCEDURE al_destroy_native_file_dialog (dialog: ALLEGRO_FILECHOOSERptr); CDECL;
    EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME;

  FUNCTION al_show_native_message_box (display: ALLEGRO_DISPLAYptr; CONST title, heading, str, buttons: STRING; flags: AL_INT): AL_INT; INLINE;

  FUNCTION al_open_native_text_log (CONST title: STRING; flags: AL_INT): ALLEGRO_TEXTLOGptr; INLINE;
  PROCEDURE al_close_native_text_log (textlog: ALLEGRO_TEXTLOGptr); CDECL;
    EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME;
  PROCEDURE al_append_native_text_log (textlog: ALLEGRO_TEXTLOGptr; CONST str: STRING); INLINE;
  FUNCTION al_get_native_text_log_event_source (textlog: ALLEGRO_TEXTLOGptr): ALLEGRO_EVENT_SOURCEptr; CDECL;
    EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME;

  FUNCTION al_get_allegro_native_dialog_version: AL_UINT32; CDECL;
    EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME;

IMPLEMENTATION

  FUNCTION _al_create_native_file_dialog_ (CONST initial_path, title, patterns: AL_STRptr; Mode: AL_INT): ALLEGRO_FILECHOOSERptr; CDECL;
  EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME NAME 'al_create_native_file_dialog';

  FUNCTION al_create_native_file_dialog (CONST initial_path, title, patterns: STRING; Mode: AL_INT): ALLEGRO_FILECHOOSERptr;
  BEGIN
    al_create_native_file_dialog := _al_create_native_file_dialog_ (AL_STRptr (initial_path), AL_STRptr (title), AL_STRptr (patterns), Mode);
  END;



  FUNCTION _al_get_native_file_dialog_path_ (CONST dialog: ALLEGRO_FILECHOOSERptr; index: AL_SIZE_T): AL_STRptr; CDECL;
  EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME NAME 'al_get_native_file_dialog_path';

  FUNCTION al_get_native_file_dialog_path (CONST dialog: ALLEGRO_FILECHOOSERptr; index: AL_SIZE_T): STRING;
  BEGIN
    al_get_native_file_dialog_path := _al_get_native_file_dialog_path_ (dialog, index);
  END;



  FUNCTION _al_show_native_message_box (display: ALLEGRO_DISPLAYptr; CONST title, heading, str, buttons: AL_STRptr; flags: AL_INT): AL_INT; CDECL;
  EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME NAME 'al_show_native_message_box';

  FUNCTION al_show_native_message_box (display: ALLEGRO_DISPLAYptr; CONST title, heading, str, buttons: STRING; flags: AL_INT): AL_INT;
  VAR
    ButtonsPtr: AL_STRptr;
  BEGIN
    IF buttons <> '' THEN
      ButtonsPtr := AL_STRptr (buttons)
    ELSE
      ButtonsPtr := NIL;
    al_show_native_message_box := _al_show_native_message_box (
       display, AL_STRptr (Title), AL_STRptr (Heading), AL_STRptr (Str), ButtonsPtr, flags
    );
  END;



  FUNCTION _al_open_native_text_log_ (CONST title: AL_STRptr; flags: AL_INT): ALLEGRO_TEXTLOGptr; CDECL;
  EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME NAME 'al_open_native_text_log';

  FUNCTION al_open_native_text_log (CONST title: STRING; flags: AL_INT): ALLEGRO_TEXTLOGptr;
  BEGIN
    al_open_native_text_log := _al_open_native_text_log_ (AL_STRptr (Title), flags);
  END;



  PROCEDURE _al_append_native_text_log_ (textlog: ALLEGRO_TEXTLOGptr; CONST str: AL_STRptr); CDECL;
  EXTERNAL ALLEGRO_NATIVE_DLG_LIB_NAME NAME 'al_append_native_text_log';

  PROCEDURE al_append_native_text_log (textlog: ALLEGRO_TEXTLOGptr; CONST str: STRING);
  BEGIN
    _al_append_native_text_log_ (textlog, AL_STRptr (Str));
  END;

END.
