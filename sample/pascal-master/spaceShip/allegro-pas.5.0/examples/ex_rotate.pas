PROGRAM ex_rotate;
(*
 *    Example program for the Allegro library, by Peter Wang.
 *)
USES
   common,
   Allegro5,
   al5image;

  CONST
     DisplayW = 640;
     DisplayH = 480;

  VAR
     Display: ALLEGRO_DISPLAYptr;
     Buf, Bmp, MemBmp, SrcBmp: ALLEGRO_BITMAPptr;
     BmpW, BmpH: INTEGER;
     Queue: ALLEGRO_EVENT_QUEUEptr;
     Event: ALLEGRO_EVENT;
     Theta, K: SINGLE;
     Mode, Flags: INTEGER;
     WideMode, MemSrcMode, TransMode, ClipMode: BOOLEAN;
     Trans: ALLEGRO_COLOR;
     EndLoop: BOOLEAN;

BEGIN
   Theta := 0;
   K := 1;
   Mode := 0;
   WideMode := FALSE;
   MemSrcMode := FALSE;
   TransMode := FALSE;
   Flags := 0;
   ClipMode := FALSE;

   IF NOT al_init THEN
      AbortExample ('Could not init Allegro');

   al_install_keyboard;
   al_init_image_addon;

   OpenLog;
   LogWriteLn ('Press ''w'' to toggle wide mode.');
   LogWriteLn ('Press ''s'' to toggle memory source bitmap.');
   LogWriteLn ('Press space to toggle drawing to backbuffer or off-screen bitmap.');
   LogWriteLn ('Press ''t'' to toggle translucency.');
   LogWriteLn ('Press ''h'' to toggle horizontal flipping.');
   LogWriteLn ('Press ''v'' to toggle vertical flipping.');
   LogWriteLn ('Press ''c'' to toggle clipping.');
   LogWriteLn ('');

   Display := al_create_display (DisplayW, DisplayH);
   IF Display = NIL THEN
      AbortExample ('Error creating display');

   Buf := al_create_bitmap (DisplayW, DisplayH);
   IF Buf = NIL THEN
      AbortExample ('Unable to create buffer');

   Bmp := al_load_bitmap ('data/mysha.pcx');
   IF Bmp = NIL THEN
      AbortExample ('Unable to load image');

   al_set_new_bitmap_flags (ALLEGRO_MEMORY_BITMAP);
   MemBmp := al_load_bitmap ('data/mysha.pcx');
   IF MemBmp = NIL THEN
      AbortExample ('Unable to load image');

   BmpW := al_get_bitmap_width (Bmp);
   BmpH := al_get_bitmap_height (Bmp);

   Queue := al_create_event_queue;
   al_register_event_source (Queue, al_get_keyboard_event_source);

   EndLoop := FALSE;
   REPEAT
      IF al_get_next_event (Queue, Event) THEN
      BEGIN
         IF Event._type = ALLEGRO_EVENT_KEY_CHAR THEN
         BEGIN
            IF Event.keyboard.keycode = ALLEGRO_KEY_ESCAPE THEN
               EndLoop := TRUE;
            IF Event.keyboard.unichar = ORD (' ') THEN
            BEGIN
               mode := 1 - mode;
               IF Mode = 0 THEN
                  LogWriteLn ('Drawing to off-screen buffer')
               ELSE
                  LogWriteLn ('Drawing to display backbuffer');
            END;
            IF Event.keyboard.unichar = ORD ('w') THEN
               WideMode := NOT WideMode;
            IF Event.keyboard.unichar = ORD ('s') THEN
            BEGIN
               MemSrcMode := NOT MemSrcMode;
               IF MemSrcMode THEN
                  LogWriteLn ('Source is memory bitmap')
               ELSE
                  LogWriteLn ('Source is display bitmap');
            END;
            IF Event.keyboard.unichar = ORD ('t') THEN
               TransMode := NOT transmode;
            IF Event.keyboard.unichar = ORD ('h') THEN
               Flags := Flags XOR ALLEGRO_FLIP_HORIZONTAL;
            IF Event.keyboard.unichar = ORD ('v') THEN
               Flags := Flags XOR ALLEGRO_FLIP_VERTICAL;
            IF Event.keyboard.unichar = ORD ('c') THEN
               ClipMode := NOT ClipMode;
         END;
      END;
      (*
       * mode 0 = draw scaled to off-screen buffer before
       *          blitting to display backbuffer
       * mode 1 = draw scaled to display backbuffer
       *)

      IF Mode = 0 THEN
         al_set_target_bitmap (Buf)
      ELSE
         al_set_target_backbuffer (Display);

      IF MemSrcMode THEN
         SrcBmp := MemBmp
      ELSE
         SrcBmp := Bmp;
      IF WideMode THEN
         K := 2
      ELSE
         K := 1;

      al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
      Trans := al_map_rgba_f (1, 1, 1, 1);
      IF Mode = 0 THEN
         al_clear_to_color (al_map_rgba_f (1, 0, 0, 1))
      ELSE
         al_clear_to_color (al_map_rgba_f (0, 0, 1, 1));

      IF TransMode THEN
      BEGIN
         al_set_blender (ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_INVERSE_ALPHA);
         Trans := al_map_rgba_f (1, 1, 1, 0.5);
      END;

      IF ClipMode THEN
         al_set_clipping_rectangle (50, 50, DisplayW - 100, DisplayH - 100)
      ELSE
         al_set_clipping_rectangle (0, 0, DisplayW, DisplayH);

      al_draw_tinted_scaled_rotated_bitmap (SrcBmp,
         Trans,
         50, 50, DisplayW DIV 2, DisplayH DIV 2,
         K, K, Theta,
         Flags);

      IF Mode = 0 THEN
      BEGIN
         al_set_target_backbuffer (Display);
         al_set_clipping_rectangle (0, 0, DisplayW, DisplayH);
         al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_ZERO);
         al_draw_bitmap (Buf, 0, 0, 0);
      END;

      al_flip_display;
      al_rest (0.01);
      Theta := Theta - 0.01;
   UNTIL EndLoop;
   al_destroy_bitmap (Bmp);
   al_destroy_bitmap (MemBmp);
   al_destroy_bitmap (Buf);

   CloseLog (FALSE);
END.

/* vim: set sts=3 sw=3 et: */
