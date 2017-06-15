UNIT al5image;
(*<This unit registers bitmap format handlers for @link(al_load_bitmap), @link(al_load_bitmap_f), @link(al_save_bitmap), @link(al_save_bitmap_f).

   The following types are built into the Allegro image addon and guaranteed to be available: BMP, PCX, TGA. Every platform also supports JPEG and PNG via external dependencies.

   Other formats may be available depending on the operating system and installed libraries, but are not guaranteed and should not be assumed to be universally available. *)
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
    al5base;

(* Initializes the image addon. *)
  FUNCTION al_init_image_addon: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_IMAGE_LIB_NAME;

(* Shut down the image addon. This is done automatically at program exit, but can be called any time the user wishes as well. *)
  PROCEDURE al_shutdown_image_addon; CDECL;
    EXTERNAL ALLEGRO_IMAGE_LIB_NAME;

(* Returns the (compiled) version of the addon, in the same format as @link(al_get_allegro_version). *)
  FUNCTION al_get_allegro_image_version: AL_UINT32; CDECL;
    EXTERNAL ALLEGRO_IMAGE_LIB_NAME;

IMPLEMENTATION

  USES
  { Next isn't needed but it must be added to prevent a "/usr/lib/nvidia-96/libGLcore.so.1: undefined reference to `__moddi3'" error. (?) }
{$IFDEF FPC}
    GL;
{$ELSE}
    OpenGL;
{$ENDIF}

END.
