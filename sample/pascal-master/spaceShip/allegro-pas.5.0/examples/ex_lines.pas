PROGRAM ex_lines;
(*
 * This example exercises line drawing, and single buffer mode.
 *)
USES
  common,
  Allegro5,
  al5primitives;

(* XXX the software line drawer currently doesn't perform clipping properly *)

  CONST
     W = 640;
     H = 480;

   VAR
      Display: ALLEGRO_DISPLAYptr;
      Queue: ALLEGRO_EVENT_QUEUEptr;
      Black, White, Background: ALLEGRO_COLOR;
      dBuf: ALLEGRO_BITMAPptr;

      LastX: INTEGER = -1;
      LastY: INTEGER = -1;

   PROCEDURE Fade;
   BEGIN
      al_set_blender (ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_INVERSE_ALPHA);
      al_draw_filled_rectangle (0, 0, W, H, al_map_rgba_f (0.5, 0.5, 0.6, 0.2));
   END;



   PROCEDURE RedDot (x, y: INTEGER);
   BEGIN
      al_draw_filled_rectangle (x - 2, y - 2, x + 2, y + 2, al_map_rgb_f (1, 0, 0));
   END;



   PROCEDURE DrawClipRect;
   BEGIN
      al_draw_rectangle (100.5, 100.5, W - 100.5, H - 100.5, Black, 0);
   END;



   PROCEDURE SetClipRect;
   BEGIN
      al_set_clipping_rectangle (100, 100, W - 200, H - 200);
   END;



   PROCEDURE ResetClipRect;
   BEGIN
      al_set_clipping_rectangle (0, 0, W, H);
   END;



   PROCEDURE Flip;
   BEGIN
      al_set_target_backbuffer (Display);
      al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
      al_draw_bitmap (dBuf, 0.0, 0.0, 0);
      al_flip_display;
   END;


   PROCEDURE Plonk (CONST x, y: INTEGER; Blend: BOOLEAN);
   BEGIN
      al_set_target_bitmap (dBuf);

      Fade;
      al_set_blender (ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_ZERO);
      DrawClipRect;
      RedDot (x, y);

      IF (LastX = -1) AND (LastY = -1) THEN
      BEGIN
         LastX := x;
         LastY := y;
      END
      ELSE BEGIN
         SetClipRect;
         IF Blend THEN
            al_set_blender (ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_INVERSE_ALPHA);
         al_draw_line (LastX, LastY, x, y, white, 0);
         LastX := -1;
         LastY := -1;
         ResetClipRect;
      END;
      Flip;
   END;



   PROCEDURE Splat (CONST x, y: INTEGER; Blend: BOOLEAN);
   VAR
      Theta: SINGLE;
   BEGIN
      al_set_target_bitmap (dBuf);

      Fade;
      al_set_blender (ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_ZERO);
      DrawClipRect;
      RedDot (x, y);

      SetClipRect;
      IF Blend THEN
         al_set_blender (ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_INVERSE_ALPHA);
      Theta := 0;
      REPEAT
         al_draw_line (x, y, x + 40.0 * cos (Theta), y + 40.0 * sin (Theta), White, 0);
         Theta := Theta + ALLEGRO_PI / 16;
      UNTIL Theta >= 2 * ALLEGRO_PI;
      ResetClipRect;

      Flip;
   END;



VAR
   Event: ALLEGRO_EVENT;
   KeyboardState: ALLEGRO_KEYBOARD_STATE;
   Blend, EndLoop: BOOLEAN;
BEGIN
   IF NOT al_init THEN
      AbortExample ('Could not init Allegro');

   al_init_primitives_addon;
   al_install_keyboard;
   al_install_mouse;

   Display := al_create_display (W, H);
   IF Display = NIL THEN
      AbortExample ('Error creating display');

   Black := al_map_rgb_f (0.0, 0.0, 0.0);
   White := al_map_rgb_f (1.0, 1.0, 1.0);
   Background := al_map_rgb_f (0.5, 0.5, 0.6);

   IF (Paramcount > 0) AND (Paramstr (1) = '--memory-bitmap') THEN
      al_set_new_bitmap_flags (ALLEGRO_MEMORY_BITMAP);
   dBuf := al_create_bitmap (W, H);
   IF dBuf = NIL THEN
      AbortExample ('Error creating double buffer');

   al_set_target_bitmap (dBuf);
   al_clear_to_color (Background);
   DrawClipRect;
   Flip;

   Queue := al_create_event_queue;
   al_register_event_source (Queue, al_get_keyboard_event_source);
   al_register_event_source (Queue, al_get_mouse_event_source);

   EndLoop := FALSE;
   REPEAT
      al_wait_for_event (Queue, Event);
      IF Event._type = ALLEGRO_EVENT_MOUSE_BUTTON_DOWN THEN
      BEGIN
         al_get_keyboard_state (KeyboardState);
         Blend := al_key_down (KeyboardState, ALLEGRO_KEY_LSHIFT)
               OR al_key_down (KeyboardState, ALLEGRO_KEY_RSHIFT);
         IF Event.mouse.button = 1 THEN
            Plonk (Event.mouse.x, Event.mouse.y, Blend)
         ELSE
            Splat (Event.mouse.x, Event.mouse.y, Blend);
      END
      ELSE IF Event._type = ALLEGRO_EVENT_DISPLAY_SWITCH_OUT THEN
      BEGIN
         LastX := -1;
         LastY := -1;
      END
      ELSE IF (Event._type = ALLEGRO_EVENT_KEY_DOWN)
      AND (Event.keyboard.keycode = ALLEGRO_KEY_ESCAPE) THEN
         EndLoop := TRUE;
   UNTIL EndLoop;

   al_destroy_event_queue (Queue);
   al_destroy_bitmap (dBuf);
END.
/* vim: set sts=3 sw=3 et: */
