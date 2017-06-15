PROGRAM ex_prim;
(*         ______   ___    ___
 *        /\  _  \ /\_ \  /\_ \
 *        \ \ \L\ \\//\ \ \//\ \      __     __   _ __   ___
 *         \ \  __ \ \ \ \  \ \ \   /'__`\ /'_ `\/\`'__\/ __`\
 *          \ \ \/\ \ \_\ \_ \_\ \_/\  __//\ \L\ \ \ \//\ \L\ \
 *           \ \_\ \_\/\____\/\____\ \____\ \____ \ \_\\ \____/
 *            \/_/\/_/\/____/\/____/\/____/\/___L\ \/_/ \/___/
 *                                           /\____/
 *                                           \_/__/
 *
 *      A sampler of the primitive addon.
 *      All primitives are rendered using the additive blender, so overdraw will manifest itself as overly bright pixels.
 *
 *
 *      By Pavel Sountsov.
 *      Translated to Pascal by Ñuño Martínez.
 *
 *      See readme.txt for copyright information.
 *)

{$LONGSTRINGS ON}

  USES
    common,
    Allegro5, al5font, al5image, al5primitives,
    sysutils;


  TYPE

    TMODE = (
      INIT,
      LOGIC,
      DRAW
    );



    ScreenProcedure = PROCEDURE (Mode: TMODE);



    CUSTOM_VERTEX = RECORD
      u, v, x, y: SMALLINT;
      Color: ALLEGRO_COLOR;
      Junk: ARRAY [1..6] OF INTEGER;
    END;



  CONST
    ScreenW = 800;
    ScreenH = 600;
    NUM_SCREENS = 10;
    ROTATE_SPEED = 0.0001;

  VAR
    Screens: ARRAY [1..NUM_SCREENS] OF ScreenProcedure;
    ScreenName: ARRAY [1..NUM_SCREENS] OF STRING;
    Font: ALLEGRO_FONTptr;
    Identity: ALLEGRO_TRANSFORM;
    Buffer, Texture: ALLEGRO_BITMAPptr;
    SolidWhite: ALLEGRO_COLOR;

    Soft: BOOLEAN = FALSE;
    Blend: BOOLEAN = TRUE;
    Speed: SINGLE = ROTATE_SPEED;
    Theta: SINGLE;
    Background: BOOLEAN = TRUE;
    Thickness: SINGLE = 0;
    MainTrans: ALLEGRO_TRANSFORM;



  PROCEDURE CustomVertexFormatPrimitives (Mode: TMODE);
  VAR
    vtx: ARRAY [1..4] OF CUSTOM_VERTEX;
    Decl: ALLEGRO_VERTEX_DECLptr;
    Ndx, x, y: INTEGER;
  CONST
     Elems: ARRAY [1..4] OF ALLEGRO_VERTEX_ELEMENT = (
     { NOTE: There's no way to get the field offset at runtime,
             so we assume 16bit for SMALLINT. }
       (attribute: ALLEGRO_PRIM_POSITION; storage: ALLEGRO_PRIM_SHORT_2; offset: 4),
       (attribute: ALLEGRO_PRIM_TEX_COORD_PIXEL; storage: ALLEGRO_PRIM_SHORT_2; offset: 0),
       (attribute: ALLEGRO_PRIM_COLOR_ATTR; storage: ALLEGRO_PRIM_STORAGE_NONE; offset: 8),
       (attribute: ALLEGRO_PRIM_ATTR_NONE; storage: ALLEGRO_PRIM_STORAGE_NONE; offset: 0)
     );
  BEGIN
    CASE Mode OF
    INIT:
      BEGIN
	Decl := al_create_vertex_decl (@Elems[LOW (Elems)], sizeof (CUSTOM_VERTEX));
	FOR Ndx := LOW (vtx) TO HIGH (vtx) DO
	BEGIN
	  x := TRUNC (200 * cos (Ndx DIV 4 * 2 * ALLEGRO_PI));
	  y := TRUNC (200 * sin (Ndx DIV 4 * 2 * ALLEGRO_PI));
	  vtx[Ndx].x := x; vtx[Ndx].y := y;
	  vtx[Ndx].u := 64 * x DIV 100; vtx[Ndx].v := 64 * y DIV 100;
	  vtx[Ndx].Color := al_map_rgba_f (1, 1, 1, 1);
	END;
      END;
    LOGIC:
      BEGIN
	Theta := Theta + Speed;
	al_build_transform (MainTrans, ScreenW DIV 2, ScreenH DIV 2, 1, 1, Theta);
      END;
    DRAW:
      BEGIN
	IF Blend THEN
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE)
	ELSE
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
	al_use_transform (MainTrans);
	al_draw_prim (@vtx[LOW (vtx)], Decl, Texture, 0, 4, ALLEGRO_PRIM_TRIANGLE_FAN);
	al_use_transform (Identity);
      END;
    END;
  END;



  PROCEDURE TexturePrimitives (Mode: TMODE);
  VAR
    vtx, vtx2: ARRAY [1..13] OF ALLEGRO_VERTEX;
    Ndx, x, y: INTEGER;
    Color: ALLEGRO_COLOR;
  BEGIN
    CASE Mode OF
    INIT:
      FOR Ndx := LOW (vtx) TO HIGH (vtx) DO
      BEGIN
	x := TRUNC (200 * cos (Ndx DIV 13 * 2 * ALLEGRO_PI));
	y := TRUNC (200 * sin (Ndx DIV 13 * 2 * ALLEGRO_PI));
	Color := al_map_rgb ((Ndx + 1) MOD 3 * 64, (Ndx + 2) MOD 3 * 64, Ndx MOD 3 * 64);
	vtx[Ndx].x := x; vtx[Ndx].y := y; vtx[Ndx].z := 0;
	vtx2[Ndx].x := 0.1 * x; vtx2[Ndx].y := 0.1 * y;
	vtx[Ndx].u := 64 * x DIV 100; vtx[Ndx].v := 64 * y DIV 100;
	vtx2[Ndx].u := 64 * x DIV 100; vtx2[Ndx].v := 64 * y DIV 100;
	IF Ndx < 10 THEN
	  vtx[Ndx].Color := al_map_rgba_f (1, 1, 1, 1)
	ELSE
	  vtx[Ndx].Color := Color;
	vtx2[Ndx].Color := vtx[Ndx].Color;
      END;
    LOGIC:
      BEGIN
	Theta := Theta + Speed;
	al_build_transform (MainTrans, ScreenW DIV 2, ScreenH DIV 2, 1, 1, Theta);
      END;
    DRAW:
      BEGIN
	IF Blend THEN
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE)
	ELSE
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
	al_use_transform (MainTrans);
	al_draw_prim (@vtx[LOW (vtx)], NIL, Texture, 0, 4, ALLEGRO_PRIM_LINE_LIST);
	al_draw_prim (@vtx[LOW (vtx)], NIL, Texture, 4, 9, ALLEGRO_PRIM_LINE_STRIP);
	al_draw_prim (@vtx[LOW (vtx)], NIL, Texture, 9, 13, ALLEGRO_PRIM_LINE_LOOP);
	al_draw_prim (@vtx2[LOW (vtx2)], NIL, Texture, 0, 13, ALLEGRO_PRIM_POINT_LIST);
	al_use_transform (Identity);
      END;
    END;
  END;



  PROCEDURE FilledTexturePrimitives (Mode: TMODE);
  VAR
    vtx: ARRAY [1..21] OF ALLEGRO_VERTEX;
    Ndx, x, y: INTEGER;
    Color: ALLEGRO_COLOR;
  BEGIN
    CASE Mode OF
    INIT:
      FOR Ndx := LOW (vtx) TO HIGH (vtx) DO
      BEGIN
	IF Ndx MOD 2 = 0 THEN
	BEGIN
	  x := TRUNC (150 * cos (Ndx DIV 20 * 2 * ALLEGRO_PI));
	  y := TRUNC (150 * sin (Ndx DIV 20 * 2 * ALLEGRO_PI));
	 END
	 ELSE BEGIN
	  x := TRUNC (200 * cos (Ndx DIV 20 * 2 * ALLEGRO_PI));
	  y := TRUNC (200 * sin (Ndx DIV 20 * 2 * ALLEGRO_PI));
	END;
	IF Ndx = 0 THEN
	BEGIN
	  x := 0; y := 0;
	END;
	Color := al_map_rgb ((7 * Ndx + 1) MOD 3 * 64, (2 * Ndx + 2) MOD 3 * 64, Ndx MOD 3 * 64);
	vtx[Ndx].x := x; vtx[Ndx].y := y; vtx[Ndx].z := 0;
	vtx[Ndx].u := 64 * x DIV 100; vtx[Ndx].v := 64 * y DIV 100;
	IF Ndx < 10 THEN
	  vtx[Ndx].Color := al_map_rgba_f (1, 1, 1, 1)
	ELSE
	  vtx[Ndx].Color := Color;
      END;
    LOGIC:
      BEGIN
	Theta := Theta + Speed;
	al_build_transform (MainTrans, ScreenW DIV 2, ScreenH DIV 2, 1, 1, Theta);
      END;
    DRAW:
      BEGIN
	IF Blend THEN
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE)
	ELSE
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
	al_use_transform (MainTrans);
	al_draw_prim (@vtx[LOW (vtx)], NIL, Texture, 0, 6, ALLEGRO_PRIM_TRIANGLE_FAN);
	al_draw_prim (@vtx[LOW (vtx)], NIL, Texture, 7, 13, ALLEGRO_PRIM_TRIANGLE_LIST);
	al_draw_prim (@vtx[LOW (vtx)], NIL, Texture, 14, 20, ALLEGRO_PRIM_TRIANGLE_STRIP);
	al_use_transform (Identity);
      END;
    END;
  END;



  PROCEDURE FilledPrimitives (Mode: TMODE);
  VAR
    vtx: ARRAY [1..21] OF ALLEGRO_VERTEX;
    Ndx, x, y: INTEGER;
    Color: ALLEGRO_COLOR;
  BEGIN
    CASE Mode OF
    INIT:
      FOR Ndx := LOW (vtx) TO HIGH (vtx) DO
      BEGIN
	IF Ndx MOD 2 = 0 THEN
	BEGIN
	  x := TRUNC (150 * cos (Ndx DIV 20 * 2 * ALLEGRO_PI));
	  y := TRUNC (150 * sin (Ndx DIV 20 * 2 * ALLEGRO_PI));
	 END
	 ELSE BEGIN
	  x := TRUNC (200 * cos (Ndx DIV 20 * 2 * ALLEGRO_PI));
	  y := TRUNC (200 * sin (Ndx DIV 20 * 2 * ALLEGRO_PI));
	END;
	IF Ndx = 0 THEN
	BEGIN
	  x := 0; y := 0;
	END;
	Color := al_map_rgb ((7 * Ndx + 1) MOD 3 * 64, (2 * Ndx + 2) MOD 3 * 64, Ndx MOD 3 * 64);
	vtx[Ndx].x := x; vtx[Ndx].y := y; vtx[Ndx].z := 0;
	vtx[Ndx].u := 64 * x DIV 100; vtx[Ndx].v := 64 * y DIV 100;
	IF Ndx < 10 THEN
	  vtx[Ndx].Color := al_map_rgba_f (1, 1, 1, 1)
	ELSE
	  vtx[Ndx].Color := Color;
      END;
    LOGIC:
      BEGIN
	Theta := Theta + Speed;
	al_build_transform (MainTrans, ScreenW DIV 2, ScreenH DIV 2, 1, 1, Theta);
      END;
    DRAW:
      BEGIN
	IF Blend THEN
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE)
	ELSE
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
	al_use_transform (MainTrans);
	al_draw_prim (@vtx[LOW (vtx)], NIL, NIL, 0, 6, ALLEGRO_PRIM_TRIANGLE_FAN);
	al_draw_prim (@vtx[LOW (vtx)], NIL, NIL, 7, 13, ALLEGRO_PRIM_TRIANGLE_LIST);
	al_draw_prim (@vtx[LOW (vtx)], NIL, NIL, 14, 20, ALLEGRO_PRIM_TRIANGLE_STRIP);
	al_use_transform (Identity);
      END;
    END;
  END;

(* 
static void IndexedFilledPrimitives(Mode: TMODE)
{
   static ALLEGRO_VERTEX vtx[21];
   static int indices1[] = {12, 13, 14, 16, 17, 18};
   static int indices2[] = {6, 7, 8, 9, 10, 11};
   static int indices3[] = {0, 1, 2, 3, 4, 5};
   if (mode == INIT) {
      int Ndx = 0;
      for (Ndx = 0; Ndx < 21; Ndx++) {
         float x, y;
         ALLEGRO_COLOR color;
         if (Ndx % 2 == 0) {
            x = 150 * cosf((float)Ndx / 20 * 2 * ALLEGRO_PI);
            y = 150 * sinf((float)Ndx / 20 * 2 * ALLEGRO_PI);
         } else {
            x = 200 * cosf((float)Ndx / 20 * 2 * ALLEGRO_PI);
            y = 200 * sinf((float)Ndx / 20 * 2 * ALLEGRO_PI);
         }
         
         if (Ndx == 0) {
            x = y = 0;
         }
         
         color = al_map_rgb((7 * Ndx + 1) % 3 * 64, (2 * Ndx + 2) % 3 * 64, (Ndx) % 3 * 64);
         
         vtx[Ndx].x = x; vtx[Ndx].y = y; vtx[Ndx].z = 0;
         vtx[Ndx].color = color;
      }
   } else if (mode == LOGIC) {
      int Ndx;
      Theta += Speed;
      for (Ndx = 0; Ndx < 6; Ndx++) {
         indices1[Ndx] = ((int)al_get_time() + Ndx) % 20 + 1;
         indices2[Ndx] = ((int)al_get_time() + Ndx + 6) % 20 + 1;
         if (Ndx > 0)
            indices3[Ndx] = ((int)al_get_time() + Ndx + 12) % 20 + 1;
      }
      
      al_build_transform(&MainTrans, ScreenW / 2, ScreenH / 2, 1, 1, Theta);
   } else if (mode == DRAW) {
      if (Blend)
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE);
      else
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
      
      al_use_transform(&MainTrans);
      
      al_draw_indexed_prim(vtx, 0, 0, indices1, 6, ALLEGRO_PRIM_TRIANGLE_LIST);
      al_draw_indexed_prim(vtx, 0, 0, indices2, 6, ALLEGRO_PRIM_TRIANGLE_STRIP);
      al_draw_indexed_prim(vtx, 0, 0, indices3, 6, ALLEGRO_PRIM_TRIANGLE_FAN);
      
      al_use_transform(&Identity);
   }
}

static void HighPrimitives(Mode: TMODE)
{
   if (mode == INIT) {
   
   } else if (mode == LOGIC) {
      Theta += Speed;
      al_build_transform(&MainTrans, ScreenW / 2, ScreenH / 2, 1, 1, Theta);
   } else if (mode == DRAW) {
      float points[8] = {
         -300, -200,
         700, 200,
         -700, 200,
         300, -200
      };

      if (Blend)
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE);
      else
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
      
      al_use_transform(&MainTrans);
      
      al_draw_line(-300, -200, 300, 200, al_map_rgba_f(0, 0.5, 0.5, 1), Thickness);
      al_draw_triangle(-150, -250, 0, 250, 150, -250, al_map_rgba_f(0.5, 0, 0.5, 1), Thickness);
      al_draw_rectangle(-300, -200, 300, 200, al_map_rgba_f(0.5, 0, 0, 1), Thickness);
      al_draw_rounded_rectangle(-200, -125, 200, 125, 50, 100, al_map_rgba_f(0.2, 0.2, 0, 1), Thickness);

      al_draw_ellipse(0, 0, 300, 150, al_map_rgba_f(0, 0.5, 0.5, 1), Thickness);
      al_draw_arc(0, 0, 200, -ALLEGRO_PI / 2, ALLEGRO_PI, al_map_rgba_f(0.5, 0.25, 0, 1), Thickness);
      al_draw_spline(points, al_map_rgba_f(0.1, 0.2, 0.5, 1), Thickness);
      
      al_use_transform(&Identity);
   }
}

static void HighFilledPrimitives(Mode: TMODE)
{
   if (mode == INIT) {
   
   } else if (mode == LOGIC) {
      Theta += Speed;
      al_build_transform(&MainTrans, ScreenW / 2, ScreenH / 2, 1, 1, Theta);
   } else if (mode == DRAW) {
      if (Blend)
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE);
      else
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
      
      al_use_transform(&MainTrans);
      
      al_draw_filled_triangle(-100, -100, -150, 200, 100, 200, al_map_rgb_f(0.5, 0.7, 0.3));
      al_draw_filled_rectangle(20, -50, 200, 50, al_map_rgb_f(0.3, 0.2, 0.6));
      al_draw_filled_ellipse(-250, 0, 100, 150, al_map_rgb_f(0.3, 0.3, 0.3));
      al_draw_filled_rounded_rectangle(50, -250, 350, -75, 50, 70, al_map_rgba_f(0.2, 0.2, 0, 1));
      
      al_use_transform(&Identity);
   }
}

static void TransformationsPrimitives(Mode: TMODE)
{
   float t = al_get_time();
   if (mode == INIT) {
   
   } else if (mode == LOGIC) {
      Theta += Speed;
      al_build_transform(&MainTrans, ScreenW / 2, ScreenH / 2, sinf(t / 5), cosf(t / 5), Theta);
   } else if (mode == DRAW) {
      float points[8] = {
         -300, -200,
         700, 200,
         -700, 200,
         300, -200
      };
      
      if (Blend)
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE);
      else
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
      
      al_use_transform(&MainTrans);
      
      al_draw_line(-300, -200, 300, 200, al_map_rgba_f(0, 0.5, 0.5, 1), Thickness);
      al_draw_triangle(-150, -250, 0, 250, 150, -250, al_map_rgba_f(0.5, 0, 0.5, 1), Thickness);
      al_draw_rectangle(-300, -200, 300, 200, al_map_rgba_f(0.5, 0, 0, 1), Thickness);
      al_draw_rounded_rectangle(-200, -125, 200, 125, 50, 100, al_map_rgba_f(0.2, 0.2, 0, 1), Thickness);
      
      al_draw_ellipse(0, 0, 300, 150, al_map_rgba_f(0, 0.5, 0.5, 1), Thickness);
      al_draw_arc(0, 0, 200, -ALLEGRO_PI / 2, ALLEGRO_PI, al_map_rgba_f(0.5, 0.25, 0, 1), Thickness);
      al_draw_spline(points, al_map_rgba_f(0.1, 0.2, 0.5, 1), Thickness);
      
      al_use_transform(&Identity);
   }
}

static void LowPrimitives(Mode: TMODE)
{
   static ALLEGRO_VERTEX vtx[13];
   static ALLEGRO_VERTEX vtx2[13];
   if (mode == INIT) {
      int Ndx = 0;
      ALLEGRO_COLOR color;
      for (Ndx = 0; Ndx < 13; Ndx++) {
         float x, y;
         x = 200 * cosf((float)Ndx / 13.0f * 2 * ALLEGRO_PI);
         y = 200 * sinf((float)Ndx / 13.0f * 2 * ALLEGRO_PI);
         
         color = al_map_rgb((Ndx + 1) % 3 * 64, (Ndx + 2) % 3 * 64, (Ndx) % 3 * 64);
         
         vtx[Ndx].x = x; vtx[Ndx].y = y; vtx[Ndx].z = 0;
         vtx2[Ndx].x = 0.1 * x; vtx2[Ndx].y = 0.1 * y;
         vtx[Ndx].color = color;
         vtx2[Ndx].color = color;
      }
   } else if (mode == LOGIC) {
      Theta += Speed;
      al_build_transform(&MainTrans, ScreenW / 2, ScreenH / 2, 1, 1, Theta);
   } else if (mode == DRAW) {
      if (Blend)
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE);
      else
         al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
      
      al_use_transform(&MainTrans);
      
      al_draw_prim(vtx, 0, 0, 0, 4, ALLEGRO_PRIM_LINE_LIST);
      al_draw_prim(vtx, 0, 0, 4, 9, ALLEGRO_PRIM_LINE_STRIP);
      al_draw_prim(vtx, 0, 0, 9, 13, ALLEGRO_PRIM_LINE_LOOP);
      al_draw_prim(vtx2, 0, 0, 0, 13, ALLEGRO_PRIM_POINT_LIST);
      
      al_use_transform(&Identity);
   }
}

      *)


  PROCEDURE IndexedPrimitives (Mode: TMODE);
  VAR
    Indices1: ARRAY [0..3] OF LONGINT = (0, 1, 3, 4);
    Indices2: ARRAY [0..3] OF LONGINT = (5, 6, 7, 8);
    Indices3: ARRAY [0..3] OF LONGINT = (9, 10, 11, 12);
    vtx, vtx2 : ARRAY [0..12] OF ALLEGRO_VERTEX;
    Ndx, x, y: INTEGER;
    Color: ALLEGRO_COLOR;
  BEGIN
    CASE Mode OF
    INIT:
      FOR Ndx := LOW (vtx) TO HIGH (vtx) DO
      BEGIN
	x := TRUNC (200 * cos (Ndx / 13 * 2 * ALLEGRO_PI));
	y := TRUNC (200 * sin (Ndx / 13 * 2 * ALLEGRO_PI));
	Color := al_map_rgb ((Ndx + 1) MOD 3 * 64, (2 * Ndx + 2) MOD 3 * 64, Ndx MOD 3 * 64);
	vtx[Ndx].x := x; vtx[Ndx].y := y; vtx[Ndx].z := 0;
	vtx2[Ndx].x := 0.1 * x; vtx2[Ndx].y := 0.1 * y;
	vtx[Ndx].color := Color;
	vtx2[Ndx].color := Color;
      END;
    LOGIC:
      BEGIN
	Theta := Theta + Speed;
	FOR Ndx := LOW (Indices1) TO HIGH (Indices1) DO
	BEGIN
	  indices1[Ndx] := TRUNC (al_get_time + Ndx) MOD 13;
	  indices2[Ndx] := TRUNC (al_get_time + Ndx + 4) MOD 13;
	  indices3[Ndx] := TRUNC (al_get_time + Ndx + 8) MOD 13;
	END;
	al_build_transform (MainTrans, ScreenW / 2, ScreenH / 2, 1, 1, Theta);
      END;
    DRAW:
      BEGIN
	IF Blend THEN
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ONE)
	ELSE
	  al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
	al_use_transform (MainTrans);
	al_draw_indexed_prim (@vtx[0], NIL, NIL, Indices1, 4, ALLEGRO_PRIM_LINE_LIST);
	al_draw_indexed_prim (@vtx[0], NIL, NIL, Indices2, 4, ALLEGRO_PRIM_LINE_STRIP);
	al_draw_indexed_prim (@vtx[0], NIL, NIL, Indices3, 4, ALLEGRO_PRIM_LINE_LOOP);
	al_draw_indexed_prim (@vtx2[0], NIL, NIL, Indices3, 4, ALLEGRO_PRIM_POINT_LIST);
	al_use_transform (Identity);
      END;
    END;
  END;




VAR
  Display: ALLEGRO_DISPLAYptr;
  Bkg: ALLEGRO_BITMAPptr;
  White, Black: ALLEGRO_COLOR;
  Queue, TimerQueue: ALLEGRO_EVENT_QUEUEptr;
  RefreshRate, FramesDone: INTEGER;
  TimeDiff, FixedTimestep, RealTime, GameTime: SINGLE;
  Ndx, CurScreen: INTEGER;
  Done, Clip: BOOLEAN;
  Old: INTEGER;
  Timer: ALLEGRO_TIMERptr;
  FrameDuration, StartTime: SINGLE;
  KeyEvent: ALLEGRO_EVENT;



  PROCEDURE NextScreen;
  BEGIN
    REPEAT
      INC (CurScreen);
      IF CurScreen > HIGH (Screens) THEN
	CurScreen := LOW (Screens);
    UNTIL Screens[CurScreen] <> NIL;
  END;

  PROCEDURE PrevScreen;
  BEGIN
    REPEAT
      DEC (CurScreen);
      IF CurScreen < LOW (Screens) THEN
	CurScreen := HIGH (Screens);
    UNTIL Screens[CurScreen] <> NIL;
  END;

BEGIN
{ Initialize Allegro 5 and addons }
  IF NOT al_init THEN
    AbortExample ('Could not init Allegro.');
  al_init_image_addon;
  al_init_font_addon;
  al_init_primitives_addon;
{ Create a window to display things on: 640x480 pixels }
  Display := al_create_display (ScreenW, ScreenH);
  IF Display = NIL THEN
      AbortExample ('Error creating display.');
{ Install input handler }
  IF NOT al_install_keyboard THEN
    AbortExample ('Error installing keyboard.');
  IF NOT al_install_mouse THEN
    AbortExample ('Error installing mouse.');
{ Load resources }
  Font := al_load_font ('data/fixed_font.tga', 0, 0);
  IF Font = NIL THEN
    AbortExample ('Error loading "data/fixed_font.tga"');
  SolidWhite := al_map_rgba_f (1, 1, 1, 1);
  Bkg := al_load_bitmap ('data/bkg.png');
  Texture := al_load_bitmap ('data/texture.tga');
  IF Texture = NIL THEN
    AbortExample ('Error loading "data/texture.tga"');
{ Make and set some color to draw with }
  White := al_map_rgba_f (1.0, 1.0, 1.0, 1.0);
  Black := al_map_rgba_f (0.0, 0.0, 0.0, 1.0);
{ Start the event queue to handle input }
  Queue := al_create_event_queue;
  al_register_event_source (Queue, al_get_keyboard_event_source);
  al_register_event_source (Queue, al_get_display_event_source (Display));
  al_register_event_source (Queue, al_get_mouse_event_source);
  al_set_window_title (Display, 'Primitives Example');

  RefreshRate := 60;
  FramesDone := 0;
  TimeDiff := al_get_time;
  FixedTimestep := 1 / RefreshRate;
  RealTime := al_get_time;
  GameTime := al_get_time;
  CurScreen := 2; { LOW (Screens); }
  Done := FALSE;
  Clip := FALSE;

  timer := al_create_timer (ALLEGRO_BPS_TO_SECS (RefreshRate));
  al_start_timer (Timer);
  TimerQueue := al_create_event_queue;
  al_register_event_source (TimerQueue, al_get_timer_event_source (Timer));

  Old := al_get_new_bitmap_flags;
  al_set_new_bitmap_flags (ALLEGRO_MEMORY_BITMAP);
  Buffer := al_create_bitmap (ScreenW, ScreenH);
  al_set_new_bitmap_flags (Old);

  al_identity_transform (Identity);

  Screens[1] := NIL; { @LowPrimitives; }
  Screens[2] := @IndexedPrimitives;
  Screens[3] := NIL; { @HighPrimitives; }
  Screens[4] := NIL; { @TransformationsPrimitives; }
  Screens[5] := @FilledPrimitives;
  Screens[6] := NIL; { @IndexedFilledPrimitives; }
  Screens[7] := NIL; { @HighFilledPrimitives; }
  Screens[8] := @TexturePrimitives;
  Screens[9] := @FilledTexturePrimitives;
  Screens[10] := @CustomVertexFormatPrimitives;

  ScreenName[1] := 'Low Level Primitives';
  ScreenName[2] := 'Indexed Primitives';
  ScreenName[3] := 'High Level Primitives';
  ScreenName[4] := 'Transformations';
  ScreenName[5] := 'Low Level Filled Primitives';
  ScreenName[6] := 'Indexed Filled Primitives';
  ScreenName[7] := 'High Level Filled Primitives';
  ScreenName[8] := 'Textured Primitives';
  ScreenName[9] := 'Filled Textured Primitives';
  ScreenName[10] := 'Custom Vertex Format';

  FOR Ndx := LOW (Screens) TO HIGH (Screens) DO
    IF Screens[Ndx] <> NIL THEN
      Screens[Ndx] (INIT);
  REPEAT
    FrameDuration := al_get_time - RealTime;
    al_rest (FixedTimestep - FrameDuration); { rest at least fixed_dt }
    FrameDuration := al_get_time - RealTime;
    RealTime := al_get_time;
    IF RealTime - GameTime > FrameDuration THEN { eliminate excess overflow }
    BEGIN
      GameTime := GameTime + FixedTimestep * TRUNC ((RealTime - GameTime) / FixedTimestep);
      WHILE RealTime - GameTime >= 0 DO
      BEGIN
	StartTime := al_get_time;
	GameTime := GameTime + FixedTimestep;
	Screens[CurScreen](LOGIC);
	WHILE al_get_next_event (Queue, KeyEvent) DO
	BEGIN
	  CASE (KeyEvent._type) OF
	  ALLEGRO_EVENT_MOUSE_BUTTON_DOWN:
	    NextScreen;
	  ALLEGRO_EVENT_DISPLAY_CLOSE:
	    Done := TRUE;
	  ALLEGRO_EVENT_KEY_CHAR:
	    CASE KeyEvent.keyboard.keycode OF
	    ALLEGRO_KEY_ESCAPE:
	      Done := TRUE;
	    ALLEGRO_KEY_S:
	      BEGIN
		Soft := NOT Soft;
		TimeDiff := al_get_time;
		FramesDone := 0;
	      END;
	    ALLEGRO_KEY_C:
	      BEGIN
		Clip := NOT Clip;
		TimeDiff := al_get_time;
		FramesDone := 0;
	      END;
	    ALLEGRO_KEY_L:
	      BEGIN
		Blend := NOT Blend;
		TimeDiff := al_get_time;
		FramesDone := 0;
	      END;
	    ALLEGRO_KEY_B:
	      BEGIN
		Background := NOT Background;
		TimeDiff := al_get_time;
		FramesDone := 0;
	      END;
	    ALLEGRO_KEY_LEFT:
	      Speed := Speed - ROTATE_SPEED;
	    ALLEGRO_KEY_RIGHT:
	      Speed := Speed + ROTATE_SPEED;
	    ALLEGRO_KEY_PGUP:
	      BEGIN
		Thickness := Thickness + 0.5;
		IF Thickness < 1 THEN
		  Thickness := 1;
	      END;
	    ALLEGRO_KEY_PGDN:
	      BEGIN
		Thickness := Thickness - 0.5;
		IF Thickness < 1 THEN
		  Thickness := 0;
	      END;
	    ALLEGRO_KEY_UP:
	      NextScreen;
	    ALLEGRO_KEY_SPACE:
	      Speed := 0;
	    ALLEGRO_KEY_DOWN:
	      PrevScreen;
	    END;
	  END;
	END;
	IF al_get_time - StartTime > FixedTimestep THEN { break if we start taking too long }
	  RealTime := GameTime;
      END;
      al_clear_to_color (Black);
      IF Soft THEN
      BEGIN
	al_set_target_bitmap (Buffer);
	al_clear_to_color (Black);
      END;
      IF Background AND (Bkg <> NIL) THEN
      BEGIN
	al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
	al_draw_scaled_bitmap (Bkg, 0, 0, al_get_bitmap_width (Bkg), al_get_bitmap_height (Bkg), 0, 0, ScreenW, ScreenH, 0);
      END;
      IF Clip THEN
	al_set_clipping_rectangle (ScreenW DIV 2, ScreenH DIV 2, ScreenW DIV 2, ScreenH DIV 2);
      Screens[CurScreen] (DRAW);
      al_set_clipping_rectangle (0, 0, ScreenW, ScreenH);
      IF Soft THEN
      BEGIN
	al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
	al_set_target_backbuffer (Display);
	al_draw_bitmap (Buffer, 0, 0, 0);
      END;
      al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_INVERSE_ALPHA);
      al_draw_text (Font, SolidWhite, ScreenW DIV 2, ScreenH - 20, ALLEGRO_ALIGN_CENTRE, '('+IntToStr (CurScreen)+') '+ScreenName[CurScreen]);
      al_draw_text (Font, SolidWhite, 0, 0, 0, 'FPS: '+ FloatToStr (FramesDone / (al_get_time - TimeDiff)));
      al_draw_text (Font, SolidWhite, 0, 20, 0, 'Change Screen (Up/Down). Esc to Quit.');
      al_draw_text (Font, SolidWhite, 0, 40, 0, 'Rotation (Left/Right/Space): '+ FloatToStr (Speed));
      al_draw_text (Font, SolidWhite, 0, 60, 0, 'Thickness (PgUp/PgDown): '+ FloatToStr (Thickness));
      al_draw_text (Font, SolidWhite, 0, 80, 0, 'Software (S): '+ BoolToStr (Soft));
      al_draw_text (Font, SolidWhite, 0, 100, 0, 'Blending (L): '+ BoolToStr (Blend));
      al_draw_text (Font, SolidWhite, 0, 120, 0, 'Background (B): '+ BoolToStr (Background));
      al_draw_text (Font, SolidWhite, 0, 140, 0, 'Clip (C): '+ BoolToStr (Clip));

      al_flip_display;
      INC (FramesDone);
    END;
  UNTIL Done;
END.
