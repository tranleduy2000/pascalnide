PROGRAM ex_gldepth;
(*<An example program showing how to set and use a depth buffer with an OpenGL
 * display.
 *
 * Use arrow keys to rotate, PgUp/PgDown to move closer/farther away.
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

  USES
    Common,
    Allegro5, al5image, al5gl, al5font,
  {$IFDEF FPC}
    GL,
  {$ELSE}
    OpenGL,
  {$ENDIF}
    sysutils;

  TYPE
  (* Stores camera information. *)
    TCamera = RECORD
    (* Camera angle. *)
      xAngle, yAngle, zAngle: DOUBLE;
    (* Camera distance. *)
      Dist: DOUBLE;
    END;



  VAR
  (* Camera. *)
    Camera: TCamera = (
      xAngle:  0.0;
      yAngle:  0.0;
      zAngle:  0.0;
      Dist  : 20.0;
    );

  CONST
  (* Speed animation. *)
    AngleSpeed = 5.0;
    DistSpeed = 1.0;

  VAR
  (* Texture. *)
    Texture: GLuint;
    Bitmap: ALLEGRO_BITMAPptr;
  (* Stores if key is pressed. *)
    KeyStatus: ARRAY [0..ALLEGRO_KEY_MAX] OF BOOLEAN;

(* Sets camera position. *)
  PROCEDURE SetCameraPosition;
  BEGIN
    glMatrixMode (GL_PROJECTION);
    glLoadIdentity;
    glFrustum (-1.0, 1.0, -1.0, 1.0, 1.0, 40.0);
    glTranslatef (0, 0, -Camera.Dist);
    glRotatef (Camera.xAngle, 1, 0, 0);
    glRotatef (Camera.yAngle, 0, 1, 0);
    glRotatef (Camera.zAngle, 0, 0, 1);
    glMatrixMode (GL_MODELVIEW);
  END;



(* Process keyboard input. *)
  PROCEDURE Keyboard;
  BEGIN
    IF KeyStatus[ALLEGRO_KEY_LEFT] THEN
      Camera.yAngle := Camera.yAngle + AngleSpeed;
    IF KeyStatus[ALLEGRO_KEY_RIGHT] THEN
      Camera.yAngle := Camera.yAngle - AngleSpeed;

    IF KeyStatus[ALLEGRO_KEY_UP] THEN
      Camera.xAngle := Camera.xAngle + AngleSpeed;
    IF KeyStatus[ALLEGRO_KEY_DOWN] THEN
      Camera.xAngle := Camera.xAngle - AngleSpeed;

    IF KeyStatus[ALLEGRO_KEY_PGUP] THEN
      Camera.Dist := Camera.Dist - DistSpeed;
    IF KeyStatus[ALLEGRO_KEY_PGDN] THEN
      Camera.Dist := Camera.Dist + DistSpeed;
  END;



(* Draws the scene. *)
  PROCEDURE DrawScene;
  BEGIN
  { Clear the RGB buffer and the depth buffer. }
    glClear (GL_COLOR_BUFFER_BIT OR GL_DEPTH_BUFFER_BIT);

  { Set the modelview matrix to be the identity matrix. }
    glLoadIdentity;
  { Translate and rotate the object. }
    glTranslatef (-2.5, 0.0, 0.0);
    glRotatef (-30, 1.0, 0.0, 0.0);
    glRotatef (30, 0.0, 1.0, 0.0);
    glRotatef (30, 0.0, 0.0, 1.0);

    glColor3f (1.0, 0.0, 1.0);

  { Draw the sides of the three-sided pyramid. }
    glEnable (GL_TEXTURE_2D);
    glBindTexture (GL_TEXTURE_2D, Texture);
    glBegin (GL_TRIANGLE_FAN);
      glTexCoord2f (0, 0); glVertex3d ( 0,  4,  0);
      glTexCoord2f (1, 0); glVertex3d ( 0, -4, -4);
      glTexCoord2f (1, 1); glVertex3d (-4, -4,  4);
      glTexCoord2f (0, 1); glVertex3d ( 4, -4,  4);
      glTexCoord2f (1, 0); glVertex3d ( 0, -4, -4);
    glEnd;

    glColor3f (0.0, 1.0, 1.0);

  { Draw the base of the pyramid. }
    glBegin (GL_TRIANGLES);
      glTexCoord2f (1, 0); glVertex3d ( 0, -4, -4);
      glTexCoord2f (0, 1); glVertex3d ( 4, -4,  4);
      glTexCoord2f (1, 1); glVertex3d (-4, -4,  4);
    glEnd;


    glLoadIdentity;
    glTranslatef (2.5, 0.0, 0.0);
    glRotatef (45, 1.0, 0.0, 0.0);
    glRotatef (45, 0.0, 1.0, 0.0);
    glRotatef (45, 0.0, 0.0, 1.0);

    glColor3f (0.0, 1.0, 0.0);

    glDisable (GL_TEXTURE_2D);
  { Draw the sides of the cube. }
    glBegin (GL_QUAD_STRIP);
      glVertex3d ( 3,  3, -3);
      glVertex3d ( 3, -3, -3);
      glVertex3d (-3,  3, -3);
      glVertex3d (-3, -3, -3);
      glVertex3d (-3,  3,  3);
      glVertex3d (-3, -3,  3);
      glVertex3d ( 3,  3,  3);
      glVertex3d ( 3, -3,  3);
      glVertex3d ( 3,  3, -3);
      glVertex3d ( 3, -3, -3);
    glEnd;

    glColor3f (0.0, 0.0, 1.0);

  { Draw the top of the cube. }
    glBegin (GL_QUADS);
      glVertex3d (-3, -3, -3);
      glVertex3d ( 3, -3, -3);
      glVertex3d ( 3, -3,  3);
      glVertex3d (-3, -3,  3);
    glEnd;

  { Bottom is texture-mapped. }
    glEnable (GL_TEXTURE_2D);
    glBindTexture (GL_TEXTURE_2D, Texture);
    glBegin (GL_QUADS);
      glTexCoord2f (0, 0); glVertex3d (-3,  3, -3);
      glTexCoord2f (1, 0); glVertex3d (-3,  3,  3);
      glTexCoord2f (1, 1); glVertex3d ( 3,  3,  3);
      glTexCoord2f (0, 1); glVertex3d ( 3,  3, -3);
    glEnd;
  END;



(* Loads and adds the textures. *)
  FUNCTION SetupTextures (Display: ALLEGRO_DISPLAYptr): BOOLEAN;
  VAR
    tmpBmp: ALLEGRO_BITMAPptr;
    aFont: ALLEGRO_FONTptr;
    w, h, Depth: INTEGER;
    TextColor: ALLEGRO_COLOR;
  BEGIN
    aFont := al_load_font ('data/fixed_font.tga', 0, 0);
    IF aFont = NIL THEN
      AbortExample ('Error loading `data/fixed_font.tga');

    tmpBmp := al_load_bitmap ('data/mysha.pcx');
    IF tmpBmp = NIL THEN
      AbortExample ('Error loading ''data/mysha.pcx''');
    SetupTextures := TRUE;
    w := 128;
    h := 128;
    Bitmap := al_create_bitmap (w, h);
    al_set_target_bitmap (Bitmap);
    al_draw_scaled_bitmap (tmpBmp,
      0, 0, al_get_bitmap_width (Bitmap), al_get_bitmap_height (Bitmap),
      0, 0, w, h, 0);

    TextColor := al_map_rgb (255, 0, 0);
    Depth := al_get_display_option (Display, ALLEGRO_DEPTH_SIZE);
    IF Depth = 0 THEN
      al_draw_text (aFont, TextColor, 0, 5, 0, 'No Z-buffer!')
    ELSE
      al_draw_text (aFont, TextColor, 0, 5, 0, PCHAR ('Z-buffer: '+IntToStr (Depth)+'bits'));
    al_set_target_backbuffer (Display);
    al_destroy_bitmap (tmpBmp);
    al_destroy_font (aFont);

    glEnable (GL_TEXTURE_2D);
    glTexEnvi (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);

    Texture := al_get_opengl_texture (Bitmap);
  END;



  VAR
    Display: ALLEGRO_DISPLAYptr;
    Queue: ALLEGRO_EVENT_QUEUEptr;
    Timer: ALLEGRO_TIMERptr;
    Event: ALLEGRO_EVENT;
    DoLoop: BOOLEAN;
BEGIN
{ Inits Allegro. }
  IF NOT al_init THEN
    AbortExample ('Error initialising Allegro.');

  al_init_image_addon;
  al_init_font_addon;
  al_install_keyboard;

  al_set_new_display_flags (ALLEGRO_OPENGL);
  al_set_new_display_option (ALLEGRO_DEPTH_SIZE, 16, ALLEGRO_SUGGEST);
  Display := al_create_display (640, 480);
  IF Display = NIL THEN
    AbortExample ('Could not create display.');

  Timer := al_create_timer (1 / 60);

  Queue := al_create_event_queue;
  al_register_event_source (Queue, al_get_keyboard_event_source);
  al_register_event_source (Queue, al_get_display_event_source (Display));
  al_register_event_source (Queue, al_get_timer_event_source (Timer));

  glEnable (GL_DEPTH_TEST);
  glDisable (GL_CULL_FACE);

  IF NOT SetupTextures (Display) THEN
    EXIT;
  al_start_timer (Timer);

  DoLoop := TRUE;
  WHILE DoLoop DO
  BEGIN
    al_wait_for_event (Queue, Event);
    CASE Event._type OF
    ALLEGRO_EVENT_DISPLAY_CLOSE:
      DoLoop := FALSE;
    ALLEGRO_EVENT_KEY_DOWN:
      BEGIN
	IF Event.keyboard.keycode = ALLEGRO_KEY_ESCAPE THEN
	  DoLoop := FALSE;
	KeyStatus[Event.keyboard.keycode] := true;
      END;
    ALLEGRO_EVENT_KEY_UP:
      KeyStatus[Event.keyboard.keycode] := false;
    ALLEGRO_EVENT_TIMER:
      BEGIN
	Keyboard;
	IF al_is_event_queue_empty (Queue) THEN
	BEGIN
	  SetCameraPosition;
	  DrawScene;
	  al_flip_display;
	END;
      END;
    END;
  END;
  al_destroy_bitmap (Bitmap);
END.
