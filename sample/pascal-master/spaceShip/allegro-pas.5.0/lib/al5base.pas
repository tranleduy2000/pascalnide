UNIT al5base;
(*<Base definitions to interface with Allegro dynamic module.

  This unit includes definitions of data types used by Allegro.  They're used
  internally to be sure they're the correct in any platform (i.e. 32bit or
  64bit).  You may use them if you wish.
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

{$INCLUDE allegro.cfg}

INTERFACE

  (* Defines some constants with the names of the library files. *)
  CONST
{$IFDEF DEBUGMODE}
    { @exclude }
    _DBG_ = '-debug';
{$ELSE}
    { @exclude }
    _DBG_ = '';
{$ENDIF}

{$IF DEFINED(UNIX)}
    { @exclude }
    ALLEGRO_LIB_NAME            = 'liballegro'+_DBG_+'.so.5.0';
    { @exclude }
    ALLEGRO_ACODEC_LIB_NAME     = 'liballegro_acodec'+_DBG_+'.so.5.0';
    { @exclude }
    ALLEGRO_AUDIO_LIB_NAME      = 'liballegro_audio'+_DBG_+'.so.5.0';
    { @exclude }
    ALLEGRO_COLOR_LIB_NAME      = 'liballegro_color'+_DBG_+'.so.5.0';
    { @exclude }
    ALLEGRO_FONT_LIB_NAME       = 'liballegro_font'+_DBG_+'.so.5.0';
    { @exclude }
    ALLEGRO_IMAGE_LIB_NAME      = 'liballegro_image'+_DBG_+'.so.5.0';
    { @exclude }
    ALLEGRO_MAIN_LIB_NAME       = 'liballegro_main'+_DBG_+'.so.5.0';
    { @exclude }
    ALLEGRO_NATIVE_DLG_LIB_NAME = 'liballegro_dialog'+_DBG_+'.so.5.0';
    { @exclude }
    ALLEGRO_PRIMITIVES_LIB_NAME = 'liballegro_primitives'+_DBG_+'.so.5.0';
    { @exclude }
    ALLEGRO_TTF_LIB_NAME        = 'liballegro_ttf'+_DBG_+'.so.5.0';
{$ELSEIF DEFINED(WINDOWS)}
  {$IFDEF MONOLITH}
  {It uses monolith staticaly linked version. }
    { @exclude }
    ALLEGRO_LIB_NAME            = 'allegro-5.0.5-monolith-mt'+_DBG_+'.dll';
    { @exclude }
    ALLEGRO_ACODEC_LIB_NAME     = ALLEGRO_LIB_NAME;
    { @exclude }
    ALLEGRO_AUDIO_LIB_NAME      = ALLEGRO_LIB_NAME;
    { @exclude }
    ALLEGRO_COLOR_LIB_NAME      = ALLEGRO_LIB_NAME;
    { @exclude }
    ALLEGRO_FONT_LIB_NAME       = ALLEGRO_LIB_NAME;
    { @exclude }
    ALLEGRO_IMAGE_LIB_NAME      = ALLEGRO_LIB_NAME;
    { @exclude }
    ALLEGRO_MAIN_LIB_NAME       = ALLEGRO_LIB_NAME;
    { @exclude }
    ALLEGRO_NATIVE_DLG_LIB_NAME = ALLEGRO_LIB_NAME;
    { @exclude }
    ALLEGRO_PRIMITIVES_LIB_NAME = ALLEGRO_LIB_NAME;
    { @exclude }
    ALLEGRO_TTF_LIB_NAME        = ALLEGRO_LIB_NAME;
  {$ELSE}
  {It uses add-ons }
    { @exclude }
    ALLEGRO_LIB_NAME            = 'allegro'+_DBG_+'.5.0.dll';
    { @exclude }
    ALLEGRO_ACODEC_LIB_NAME     = 'allegro_acodec'+_DBG_+'.5.0.dll';
    { @exclude }
    ALLEGRO_AUDIO_LIB_NAME      = 'allegro_audio'+_DBG_+'.5.0.dll';
    { @exclude }
    ALLEGRO_COLOR_LIB_NAME      = 'allegro_color'+_DBG_+'.5.0.dll';
    { @exclude }
    ALLEGRO_FONT_LIB_NAME       = 'allegro_font'+_DBG_+'.5.0.dll';
    { @exclude }
    ALLEGRO_IMAGE_LIB_NAME      = 'allegro_image'+_DBG_+'.5.0.dll';
    { @exclude }
    ALLEGRO_MAIN_LIB_NAME       = 'allegro_main'+_DBG_+'.5.0.dll';
    { @exclude }
    ALLEGRO_NATIVE_DLG_LIB_NAME = 'allegro_dialog'+_DBG_+'.5.0.dll';
    { @exclude }
    ALLEGRO_PRIMITIVES_LIB_NAME = 'allegro_primitives'+_DBG_+'.5.0.dll';
    { @exclude }
    ALLEGRO_TTF_LIB_NAME        = 'allegro_primitives'+_DBG_+'.5.0.dll';
  {$ENDIF}
{$ENDIF}



(*
   Next are definitions of numeric data types.  We may use FPC's ctype unit,
   but Delphi doesn't has it so I prefer to do it this way.

   First: it defines some integers with specific lenght.
   Then: it defines the types used by C declarations.
 *)

  TYPE
  (* Generic pointer. *)
    AL_POINTER = POINTER;
  (* Signed 8bit integer values. *)
    AL_INT8 = SHORTINT;
  (* Unsigned 8bit integer values. *)
    AL_UINT8 = BYTE;
  (* Signed 16bit integer values. *)
    AL_INT16 = SMALLINT;
  (* Unsigned 16bit integer values. *)
    AL_UINT16 = WORD;
  (* Signed 32bit integer values. *)
    AL_INT32 = LONGINT;
  (* Unsigned 32bit integer values. *)
    AL_UINT32 = LONGWORD;
  (* Signed 64bit integer values. *)
    AL_INT64 = INT64;
  (* Unsigned 64bit integer values. *)
    AL_UINT64 = QWORD;



  (* Boolean result. *)
    AL_BOOL = BYTEBOOL;
  (* Signed 8bit integer.

    Note that it isn't Pascal's CHAR type! *)
    AL_CHAR = AL_INT8;
  (* Unsigned 8bit integer values. *)
    AL_UCHAR = AL_UINT8;
  (* Signed 16bit integer values. *)
    AL_SHORT = AL_INT16;
  (* Unsigned 16bit integer values. *)
    AL_USHORT = AL_UINT16;
  (* Signed 32bit integer values. *)
    AL_INT = AL_INT32;
  (* Unsigned 32bit integer values. *)
    AL_UINT = AL_UINT32;
{$IFDEF CPU64}
  {$IFDEF WINDOWS}
  (* Signed 32/64bit integer values. *)
    AL_LONG = AL_INT32;
  (* Unsigned 32/64bit integer values. *)
    AL_ULONG = AL_UINT32;
  {$ELSE}
  (* Signed 32/64bit integer values. *)
    AL_LONG = AL_INT64;
  (* Unsigned 32/64bit integer values. *)
    AL_ULONG = AL_UINT64;
  {$ENDIF}
(* size_t equivalent. *)
  AL_SIZE_T = AL_UINT64;
(* Fake pointer type.  It's needed because the need of pointer arithmetics in
  some inlined methods. *)
  AL_UINTPTR_T = AL_UINT64;
{$ELSE}
  (* Signed 32/64bit integer values. *)
    AL_LONG = AL_INT32;
  (* Unsigned 32/64bit integer values. *)
    AL_ULONG = AL_UINT32;
(* size_t equivalent. *)
  AL_SIZE_T = AL_UINT32;
(* Fake pointer type.  It's needed because the need of pointer arithmetics in
  some inlined methods. *)
  AL_UINTPTR_T = AL_UINT32;
{$ENDIF}

  (* Float value. *)
    AL_FLOAT = SINGLE;
  (* Double value. *)
    AL_DOUBLE = DOUBLE;

  (* Pointer. *)
    AL_VOIDptr = AL_POINTER;
  (* Pointer to text strings.  Used to convert Pascal's @code(STRING) to C
    @code(char * ) *)
    AL_STRptr = PCHAR;
  (* Pointer to integer. *)
    AL_INTptr = ^AL_INT;
  (* Pointer to float. *)
    AL_FLOATptr = ^AL_FLOAT;

IMPLEMENTATION

END.
