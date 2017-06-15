UNIT al5font;
(*<Text font management *)
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


  TYPE
    ALLEGRO_FONT_VTABLEptr = AL_POINTER;



    ALLEGRO_FONTptr = ^ALLEGRO_FONT;
    ALLEGRO_FONT = RECORD
      data : AL_VOIDptr;
      height : AL_INT;
      vtable : ALLEGRO_FONT_VTABLEptr;
    END;



    FONT_LOADER_FUNCTION = FUNCTION (CONST filename: AL_STRptr; size, flags: AL_INT): ALLEGRO_FONTptr; CDECL;

  CONST
    ALLEGRO_ALIGN_LEFT    = 0;
    ALLEGRO_ALIGN_CENTRE  = 1;
    ALLEGRO_ALIGN_CENTER  = 1;
    ALLEGRO_ALIGN_RIGHT   = 2;
    ALLEGRO_ALIGN_INTEGER = 1;

  FUNCTION al_register_font_loader (CONST ext: STRING; load: FONT_LOADER_FUNCTION): AL_BOOL; INLINE;
  FUNCTION al_load_bitmap_font (CONST filename: STRING): ALLEGRO_FONTptr; INLINE;
  FUNCTION al_load_font (CONST filename: STRING; size, flags: AL_INT): ALLEGRO_FONTptr; INLINE;

  FUNCTION al_grab_font_from_bitmap (bmp: ALLEGRO_BITMAPptr; n: AL_INT; ranges: ARRAY OF AL_INT): ALLEGRO_FONTptr;

  PROCEDURE al_draw_ustr (CONST font: ALLEGRO_FONTptr; color: ALLEGRO_COLOR; x, y: AL_FLOAT; flags: AL_INT; CONST ustr: ALLEGRO_USTRptr); CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;
  PROCEDURE al_draw_text (CONST font: ALLEGRO_FONTptr; color: ALLEGRO_COLOR; x, y: AL_FLOAT; flags: AL_INT; CONST str: STRING); INLINE;
  PROCEDURE al_draw_justified_text (CONST font: ALLEGRO_FONTptr; color: ALLEGRO_COLOR; x1, x2, y, diff: AL_FLOAT; flags: AL_INT; CONST str: STRING); INLINE;
  PROCEDURE al_draw_justified_ustr (CONST font: ALLEGRO_FONTptr; color: ALLEGRO_COLOR; x1, x2, y, diff: SINGLE; flags: LONGINT; CONST str: ALLEGRO_USTRptr); CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;
{ No "format" procedures.  Use Pascal's Format function defined at sysutils unit instead. }
  FUNCTION al_get_text_width (CONST font: ALLEGRO_FONTptr; CONST str: STRING): AL_INT; INLINE;
  FUNCTION al_get_ustr_width (CONST font: ALLEGRO_FONTptr; CONST ustr: ALLEGRO_USTRptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;
  FUNCTION al_get_font_line_height (CONST font: ALLEGRO_FONTptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;
  FUNCTION al_get_font_ascent (CONST font: ALLEGRO_FONTptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;
  FUNCTION al_get_font_descent (CONST font: ALLEGRO_FONTptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;
  PROCEDURE al_destroy_font (font: ALLEGRO_FONTptr); CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;
  PROCEDURE al_get_ustr_dimensions (CONST f: ALLEGRO_FONTptr; CONST str: ALLEGRO_USTRptr; VAR bbx, bby, bbw, bbh: AL_INT); CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;
  PROCEDURE al_get_text_dimensions (CONST f: ALLEGRO_FONTptr; CONST str: STRING; VAR bbx, bby, bbw, bbh: AL_INT); INLINE;
  PROCEDURE al_init_font_addon; CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;
  PROCEDURE al_shutdown_font_addon; CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;

(* Returns the (compiled) version of the addon, in the same format as @link(al_get_allegro_version). *)
  FUNCTION al_get_allegro_font_version: AL_UINT32; CDECL;
    EXTERNAL ALLEGRO_FONT_LIB_NAME;

IMPLEMENTATION

  FUNCTION _al_register_font_loader_ (CONST ext: AL_STRptr; load: FONT_LOADER_FUNCTION): AL_BOOL; CDECL;
  EXTERNAL ALLEGRO_FONT_LIB_NAME NAME 'al_register_font_loader';

  FUNCTION al_register_font_loader (CONST ext: STRING; load: FONT_LOADER_FUNCTION): AL_BOOL;
  BEGIN
    al_register_font_loader := _al_register_font_loader_ (AL_STRptr (Ext), load);
  END;



  FUNCTION _al_load_bitmap_font_ (CONST filename: AL_STRptr): ALLEGRO_FONTptr; CDECL;
  EXTERNAL ALLEGRO_FONT_LIB_NAME NAME 'al_load_bitmap_font';

  FUNCTION al_load_bitmap_font (CONST filename: STRING): ALLEGRO_FONTptr;
  BEGIN
    al_load_bitmap_font := _al_load_bitmap_font_ (AL_STRptr (Filename));
  END;



  FUNCTION _al_load_font_ (CONST filename: AL_STRptr; size, flags: AL_INT): ALLEGRO_FONTptr; CDECL;
  EXTERNAL ALLEGRO_FONT_LIB_NAME NAME 'al_load_font';

  FUNCTION al_load_font (CONST filename: STRING; size, flags: AL_INT): ALLEGRO_FONTptr;
  BEGIN
    al_load_font := _al_load_font_ (AL_STRptr (Filename), size, flags);
  END;



  FUNCTION _al_grab_font_from_bitmap_ (bmp: ALLEGRO_BITMAPptr; n: AL_INT; ranges: AL_INTptr): ALLEGRO_FONTptr; CDECL;
  EXTERNAL ALLEGRO_FONT_LIB_NAME NAME 'al_grab_font_from_bitmap';

  FUNCTION al_grab_font_from_bitmap (bmp: ALLEGRO_BITMAPptr; n: AL_INT; ranges: ARRAY OF AL_INT): ALLEGRO_FONTptr;
  BEGIN
    al_grab_font_from_bitmap := _al_grab_font_from_bitmap_ (bmp, n, @ranges[0]);
  END;



  PROCEDURE _al_draw_text_ (CONST font: ALLEGRO_FONTptr; color: ALLEGRO_COLOR; x, y: AL_FLOAT; flags: AL_INT; CONST str: AL_STRptr); CDECL;
  EXTERNAL ALLEGRO_FONT_LIB_NAME NAME 'al_draw_text';

  PROCEDURE al_draw_text (CONST font: ALLEGRO_FONTptr; color: ALLEGRO_COLOR; x, y: AL_FLOAT; flags: AL_INT; CONST str: STRING);
  BEGIN
    _al_draw_text_ (font, color, x, y, flags, AL_STRptr (str));
  END;



  PROCEDURE _al_draw_justified_text_ (CONST font: ALLEGRO_FONTptr; color: ALLEGRO_COLOR; x1, x2, y, diff: AL_FLOAT; flags: AL_INT; CONST str: AL_STRptr); CDECL;
  EXTERNAL ALLEGRO_FONT_LIB_NAME NAME 'al_draw_justified_text';

  PROCEDURE al_draw_justified_text (CONST font: ALLEGRO_FONTptr; color: ALLEGRO_COLOR; x1, x2, y, diff: AL_FLOAT; flags: AL_INT; CONST str: STRING);
  BEGIN
    _al_draw_justified_text_ (font, color, x1, x2, y, diff, flags, AL_STRptr (str));
  END;



  FUNCTION _al_get_text_width_ (CONST font: ALLEGRO_FONTptr; CONST str: AL_STRptr): AL_INT; CDECL;
  EXTERNAL ALLEGRO_FONT_LIB_NAME NAME 'al_get_text_width';

  FUNCTION al_get_text_width (CONST font: ALLEGRO_FONTptr; CONST str: STRING): AL_INT;
  BEGIN
    al_get_text_width := _al_get_text_width_ (font, AL_STRptr (str));
  END;



  PROCEDURE _al_get_text_dimensions_ (CONST f: ALLEGRO_FONTptr; CONST str: AL_STRptr; VAR bbx, bby, bbw, bbh: AL_INT); CDECL;
  EXTERNAL ALLEGRO_FONT_LIB_NAME NAME 'al_get_text_dimensions';

  PROCEDURE al_get_text_dimensions (CONST f: ALLEGRO_FONTptr; CONST str: STRING; VAR bbx, bby, bbw, bbh: AL_INT);
  BEGIN
    _al_get_text_dimensions_ (f, AL_STRptr (str), bbx, bby, bbw, bbh);
  END;

END.
