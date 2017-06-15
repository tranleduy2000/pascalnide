UNIT al5color;
(*< *)
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


  FUNCTION al_get_allegro_color_version: AL_UINT32; CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;

  PROCEDURE al_color_hsv_to_rgb (h, s, v: AL_FLOAT; VAR r, g, b: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  PROCEDURE al_color_rgb_to_hsl (r, g, b: AL_FLOAT; VAR h, s, l: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  PROCEDURE al_color_rgb_to_hsv (r, g, b: AL_FLOAT; VAR h, s, v: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  PROCEDURE al_color_hsl_to_rgb (h, s, l: AL_FLOAT; VAR r, g, b: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  FUNCTION al_color_name_to_rgb (CONST name: STRING; VAR r, g, b: AL_FLOAT): AL_BOOL; INLINE;
  FUNCTION al_color_rgb_to_name (r, g, b: AL_FLOAT): STRING; INLINE;
  PROCEDURE al_color_cmyk_to_rgb (c, m, y, k: AL_FLOAT; VAR r, g, b: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  PROCEDURE al_color_rgb_to_cmyk (r, g, b: AL_FLOAT; VAR c, m, y, k: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  PROCEDURE al_color_yuv_to_rgb (y, u, v: AL_FLOAT;  VAR r, g, b: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  PROCEDURE al_color_rgb_to_yuv (r, g, b: AL_FLOAT; VAR y, u, v: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  PROCEDURE al_color_rgb_to_html (r, g, b: AL_FLOAT; VAR str: STRING); INLINE;
  PROCEDURE al_color_html_to_rgb (CONST str: STRING; VAR r, g, b: AL_FLOAT); INLINE;
  FUNCTION al_color_yuv (y, u, v: AL_FLOAT): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  FUNCTION al_color_cmyk (c, m, y, k: AL_FLOAT): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  FUNCTION al_color_hsl (h, s, l: AL_FLOAT): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  FUNCTION al_color_hsv (h, s, v: AL_FLOAT): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME;
  FUNCTION al_color_name (CONST name: STRING): ALLEGRO_COLOR; INLINE;
  FUNCTION al_color_html (CONST str: STRING): ALLEGRO_COLOR; INLINE;

IMPLEMENTATION

  FUNCTION _al_color_name_to_rgb_ (CONST name: AL_STRptr; r, g, b: AL_FLOATptr): AL_BOOL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME NAME 'al_color_name_to_rgb';

  FUNCTION al_color_name_to_rgb (CONST name: STRING; VAR r, g, b: AL_FLOAT): AL_BOOL;
  BEGIN
    al_color_name_to_rgb := _al_color_name_to_rgb_ (AL_STRptr (name), @r, @g, @b);
  END;



  FUNCTION _al_color_rgb_to_name_ (r, g, b: AL_FLOAT): AL_STRptr; CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME NAME 'al_color_rgb_to_name';

  FUNCTION al_color_rgb_to_name (r, g, b: AL_FLOAT): STRING;
  BEGIN
    al_color_rgb_to_name := _al_color_rgb_to_name_ (r, g, b);
  END;



  PROCEDURE _al_color_rgb_to_html_ (r, g, b: AL_FLOAT; str: AL_STRptr); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME NAME 'al_color_rgb_to_html';

  PROCEDURE al_color_rgb_to_html (r, g, b: AL_FLOAT; VAR str: STRING);
  BEGIN
    str := '      ';
    _al_color_rgb_to_html_ (r, g, b, AL_STRptr (str));
  END;



  PROCEDURE _al_color_html_to_rgb_ (CONST str: AL_STRptr; r, g, b: AL_FLOATptr); CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME NAME 'al_color_html_to_rgb';

  PROCEDURE al_color_html_to_rgb (CONST str: STRING; VAR r, g, b: AL_FLOAT);
  BEGIN
    _al_color_html_to_rgb_ (AL_STRptr (str), @r, @g, @b);
  END;



  FUNCTION _al_color_name_ (CONST name: AL_STRptr): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME NAME 'al_color_name';

  FUNCTION al_color_name (CONST name: STRING): ALLEGRO_COLOR;
  BEGIN
    al_color_name := _al_color_name_ (AL_STRptr (name));
  END;



  FUNCTION _al_color_html_ (CONST str: AL_STRptr): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_COLOR_LIB_NAME NAME 'al_color_html';

  FUNCTION al_color_html (CONST str: STRING): ALLEGRO_COLOR;
  BEGIN
    al_color_html := _al_color_html_ (AL_STRptr (str));
  END;

END.
