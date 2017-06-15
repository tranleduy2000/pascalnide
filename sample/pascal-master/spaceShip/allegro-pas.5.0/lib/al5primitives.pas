UNIT al5primitives;
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

  TYPE
    ALLEGRO_PRIM_TYPE = (
      ALLEGRO_PRIM_LINE_LIST,
      ALLEGRO_PRIM_LINE_STRIP,
      ALLEGRO_PRIM_LINE_LOOP,
      ALLEGRO_PRIM_TRIANGLE_LIST,
      ALLEGRO_PRIM_TRIANGLE_STRIP,
      ALLEGRO_PRIM_TRIANGLE_FAN,
      ALLEGRO_PRIM_POINT_LIST,
      ALLEGRO_PRIM_NUM_TYPES
    );



    ALLEGRO_PRIM_ATTR = (
      ALLEGRO_PRIM_ATTR_NONE := 0,
      ALLEGRO_PRIM_POSITION := 1,
      ALLEGRO_PRIM_COLOR_ATTR,
      ALLEGRO_PRIM_TEX_COORD,
      ALLEGRO_PRIM_TEX_COORD_PIXEL,
      ALLEGRO_PRIM_ATTR_NUM
    );



    ALLEGRO_PRIM_STORAGE = (
      ALLEGRO_PRIM_FLOAT_2 := 0,
      ALLEGRO_PRIM_FLOAT_3,
      ALLEGRO_PRIM_SHORT_2
    );

  CONST
    ALLEGRO_PRIM_STORAGE_NONE = ALLEGRO_PRIM_FLOAT_2;
    ALLEGRO_VERTEX_CACHE_SIZE = 256;



    ALLEGRO_PRIM_QUALITY = 10;

  TYPE
    ALLEGRO_VERTEX_ELEMENTptr = ^ALLEGRO_VERTEX_ELEMENT;
    ALLEGRO_VERTEX_ELEMENT = RECORD
      attribute: ALLEGRO_PRIM_ATTR;
      storage: ALLEGRO_PRIM_STORAGE;
      offset: AL_INT;
    END;


    ALLEGRO_VERTEX_DECLptr = AL_POINTER;


    ALLEGRO_VERTEXptr = ^ALLEGRO_VERTEX;
    ALLEGRO_VERTEX = RECORD
      x, y, z: AL_FLOAT;
      u, v: AL_FLOAT;
      color: ALLEGRO_COLOR;
    END;



    ALLEGRO_INIT_TRIANGLE_PROC = PROCEDURE (state: AL_UINTPTR_T; v1, v2, v3: ALLEGRO_VERTEXptr); CDECL;
    ALLEGRO_FIRST_TRIANGLE_PROC = PROCEDURE (state: AL_UINTPTR_T; x, y, l1, l2: AL_INT); CDECL;
    ALLEGRO_DRAW_TRIANGLE_PROC = PROCEDURE (state: AL_UINTPTR_T; x1, y, x2: AL_INT); CDECL;

    ALLEGRO_FIRST_LINE_PROC = PROCEDURE (state: AL_UINTPTR_T; px, py: AL_INT; v1, v2: ALLEGRO_VERTEXptr); CDECL;
    ALLEGRO_DRAW_LINE_PROC = PROCEDURE (state: AL_UINTPTR_T; x, y: AL_INT); CDECL;

    ALLEGRO_STEP_PROC = PROCEDURE (state: AL_UINTPTR_T; _type: AL_INT); CDECL;

    ALLEGRO_SPLINE_CONTROL_POINTS = ARRAY [0..7] OF AL_FLOAT;

  FUNCTION al_get_allegro_primitives_version: AL_UINT32; CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;

(* Primary Functions *)
  FUNCTION al_init_primitives_addon: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_shutdown_primitives_addon; CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  FUNCTION al_draw_prim (CONST vtxs: AL_VOIDptr; CONST decl: ALLEGRO_VERTEX_DECLptr; texture: ALLEGRO_BITMAPptr; start, finish: AL_INT; _type: ALLEGRO_PRIM_TYPE): AL_INT; CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  FUNCTION al_draw_indexed_prim (CONST vtxs: AL_VOIDptr; CONST decl: ALLEGRO_VERTEX_DECLptr; texture: ALLEGRO_BITMAPptr; CONST indices: ARRAY OF AL_INT; num_vtx: AL_INT; _type: ALLEGRO_PRIM_TYPE): AL_INT; {TODO: Need to test if the "indices" parameter does work correctly }

  FUNCTION al_create_vertex_decl (CONST elements: ALLEGRO_VERTEX_ELEMENTptr; stride: AL_INT): ALLEGRO_VERTEX_DECLptr; CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_destroy_vertex_decl (decl: ALLEGRO_VERTEX_DECLptr); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;

(* Custom primitives *)
  PROCEDURE al_draw_soft_triangle (v1, v2, v3: ALLEGRO_VERTEXptr; state: AL_UINTPTR_T; init: ALLEGRO_INIT_TRIANGLE_PROC; first: ALLEGRO_FIRST_TRIANGLE_PROC; step: ALLEGRO_STEP_PROC; draw: ALLEGRO_DRAW_TRIANGLE_PROC); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_soft_line (v1, v2: ALLEGRO_VERTEXptr; state: AL_UINTPTR_T; first: ALLEGRO_FIRST_LINE_PROC; step: ALLEGRO_STEP_PROC; draw: ALLEGRO_DRAW_LINE_PROC); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;

(* High level primitives *)
  PROCEDURE al_draw_line (x1, y1, x2, y2: AL_FLOAT; color: ALLEGRO_COLOR; thickness: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_triangle (x1, y1, x2, y2, x3, y3: AL_FLOAT; color: ALLEGRO_COLOR; thickness: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_rectangle (x1, y1, x2, y2: AL_FLOAT; color: ALLEGRO_COLOR; thickness: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_rounded_rectangle (x1, y1, x2, y2, rx, ry: AL_FLOAT; color: ALLEGRO_COLOR; thickness: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;

  PROCEDURE al_calculate_arc (dest: AL_FLOATptr; stride: AL_INT; cx, cy, rx, ry, start_theta, delta_theta, thickness: AL_FLOAT; num_segments: AL_INT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_circle (cx, cy, r: AL_FLOAT; color: ALLEGRO_COLOR; thickness: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_ellipse (cx, cy, rx, ry: AL_FLOAT; color: ALLEGRO_COLOR; thickness: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_arc (cx, cy, r, start_theta, delta_theta: AL_FLOAT; color: ALLEGRO_COLOR; thickness: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_elliptical_arc (cx, cy, rx, ry, start_theta, delta_theta: AL_FLOAT; color: ALLEGRO_COLOR; thickness: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;

  PROCEDURE al_calculate_spline (dest: AL_FLOATptr; stride: AL_INT; points: ALLEGRO_SPLINE_CONTROL_POINTS; thickness: AL_FLOAT; num_segments: AL_INT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_spline (points: ALLEGRO_SPLINE_CONTROL_POINTS; color: ALLEGRO_COLOR; thickness: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;

  PROCEDURE al_calculate_ribbon (dest: AL_FLOATptr; dest_stride: AL_INT; CONST points: ARRAY OF AL_FLOAT; points_stride: AL_INT; thickness: AL_FLOAT; num_segments: AL_INT);
  PROCEDURE al_draw_ribbon (CONST points: ARRAY OF AL_FLOAT; points_stride: AL_INT; color: ALLEGRO_COLOR; thickness: AL_FLOAT; num_segments: AL_INT);

  PROCEDURE al_draw_filled_triangle (x1, y1, x2, y2, x3, y3: AL_FLOAT; color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_filled_rectangle (x1, y1, x2, y2: AL_FLOAT; color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_filled_ellipse (cx, cy, rx, ry: AL_FLOAT; color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_filled_circle (cx, cy, r: AL_FLOAT; color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;
  PROCEDURE al_draw_filled_rounded_rectangle (x1, y1, x2, y2, rx, ry: AL_FLOAT; color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME;

IMPLEMENTATION

  USES
  { Next unit isn't needed but it must be added to prevent a "/usr/lib/nvidia-96/libGLcore.so.1: undefined reference to `__moddi3'" error. (?) }
{$IFDEF FPC}
    GL;
{$ELSE}
    OpenGL;
{$ENDIF}



  FUNCTION _al_draw_indexed_prim_ (CONST vtxs: AL_VOIDptr; CONST decl: ALLEGRO_VERTEX_DECLptr; texture: ALLEGRO_BITMAPptr; CONST indices: AL_INTptr; num_vtx: AL_INT; _type: ALLEGRO_PRIM_TYPE): AL_INT; CDECL;
  EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME NAME 'al_draw_indexed_prim';

  FUNCTION al_draw_indexed_prim (CONST vtxs: AL_VOIDptr; CONST decl: ALLEGRO_VERTEX_DECLptr; texture: ALLEGRO_BITMAPptr; CONST indices: ARRAY OF AL_INT; num_vtx: AL_INT; _type: ALLEGRO_PRIM_TYPE): AL_INT;
  BEGIN
    al_draw_indexed_prim := _al_draw_indexed_prim_ (vtxs, decl, texture, @indices[0], num_vtx, _type);
  END;

  PROCEDURE _al_calculate_ribbon_ (dest: AL_FLOATptr; dest_stride: AL_INT; CONST points: AL_FLOATptr; points_stride: AL_INT; thickness: AL_FLOAT; num_segments: AL_INT); CDECL;
  EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME NAME 'al_calculate_ribbon';

  PROCEDURE al_calculate_ribbon (dest: AL_FLOATptr; dest_stride: AL_INT; CONST points: ARRAY OF AL_FLOAT; points_stride: AL_INT; thickness: AL_FLOAT; num_segments: AL_INT);
  BEGIN
    _al_calculate_ribbon_ (dest, dest_stride, @points[0], points_stride, thickness, num_segments);
  END;

  PROCEDURE _al_draw_ribbon_ (CONST points: AL_FLOATptr; points_stride: AL_INT; color: ALLEGRO_COLOR; thickness: AL_FLOAT; num_segments: AL_INT); CDECL;
  EXTERNAL ALLEGRO_PRIMITIVES_LIB_NAME NAME 'al_draw_ribbon';

  PROCEDURE al_draw_ribbon (CONST points: ARRAY OF AL_FLOAT; points_stride: AL_INT; color: ALLEGRO_COLOR; thickness: AL_FLOAT; num_segments: AL_INT);
  BEGIN
    _al_draw_ribbon_ (@points[0], points_stride, color, thickness, num_segments);
  END;

END.
