PROGRAM ex_audio_simple;
(*    Example program for the Allegro library.
 *
 *    Demonstrate 'simple' audio interface.
 *)
USES
   common,
   Allegro5, al5audio, al5acodec;
CONST
   RESERVED_SAMPLES = 16;
   MAX_SAMPLE_DATA  = 10;
VAR
   SampleData: ARRAY [1..MAX_SAMPLE_DATA] OF ALLEGRO_SAMPLEptr;
   Display: ALLEGRO_DISPLAYptr;
   EventQueue: ALLEGRO_EVENT_QUEUEptr;
   Event: ALLEGRO_EVENT;
   Ndx: INTEGER;
   FileName: STRING;
   EndLoop: BOOLEAN;
BEGIN
   IF Paramcount < 1 THEN
   BEGIN
      WriteLn ('Usage:');
      WriteLn ('   ', ParamStr (0), ' {audio_files}');
      HALT (1);
   END;

   IF NOT al_init THEN
      AbortExample ('Could not init Allegro.');

   al_install_keyboard;

   Display := al_create_display (640, 480);
   IF Display = NIL THEN
      AbortExample ('Could not create display');

   EventQueue := al_create_event_queue;
   al_register_event_source (EventQueue, al_get_keyboard_event_source);
   al_register_event_source (EventQueue, al_get_display_event_source (Display));

   al_init_acodec_addon;
   IF NOT al_install_audio THEN
   BEGIN
      WriteLn ('Could not init sound!');
      HALT (1);
   END;
   IF NOT al_reserve_samples (RESERVED_SAMPLES) THEN
   BEGIN
      WriteLn ('Could not set up voice and mixer.');
      HALT (1);
   END;

   FOR Ndx := LOW (SampleData) TO HIGH (SampleData) DO
   BEGIN
      IF Ndx <= Paramcount THEN
      BEGIN
         FileName := ParamStr (Ndx);
      { Load the entire sound file from disk. }
         SampleData[Ndx] := al_load_sample (FileName);
         IF SampleData[Ndx] = NIL THEN
            WriteLn ('Could not load sample from "', FileName, '"!');
      END
      ELSE
         SampleData[Ndx] := NIL;
   END;

   WriteLn ('Press digits to play sounds, space to stop sounds, Escape to quit.');
   REPEAT
      al_wait_for_event (EventQueue, Event);
      CASE Event._type OF
      ALLEGRO_EVENT_KEY_CHAR:
         BEGIN
            IF Event.keyboard.keycode = ALLEGRO_KEY_ESCAPE THEN
               EndLoop := TRUE;
            IF Event.keyboard.unichar = ORD (' ') THEN
               al_stop_samples
            ELSE IF (ORD ('0') <= Event.keyboard.unichar)
            AND (Event.keyboard.unichar <= ORD ('9')) THEN
            BEGIN
               IF Event.keyboard.unichar = ORD ('0') THEN
                  Ndx := 10
               ELSE
                  Ndx := (Event.keyboard.unichar - ORD ('0') + 10) MOD 10;
               IF SampleData[Ndx] <> NIL THEN
               BEGIN
                  WriteLn ('Playing ', Ndx);
                  IF NOT al_play_sample (SampleData[Ndx], 1, 0.5, 1, ALLEGRO_PLAYMODE_LOOP, NIL) THEN
                     WriteLn ('al_play_sample_data failed, perhaps too many sounds');
               END;
            END;
         END;
         ALLEGRO_EVENT_DISPLAY_CLOSE:
            EndLoop := TRUE;
      END;
   UNTIL EndLoop;
{ Sample data and other objects will be automatically freed. }
   al_uninstall_audio;
END.

/* vim: set sts=3 sw=3 et: */
