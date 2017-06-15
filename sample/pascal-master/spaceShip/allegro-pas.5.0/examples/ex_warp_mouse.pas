PROGRAM ex_warp_mouse;
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
    common,
    Allegro5, al5font, al5image, al5primitives,
    sysutils;

  CONST
    Width = 640;
    Height = 480;

  VAR
    Font: ALLEGRO_FONTptr;
    Display: ALLEGRO_DISPLAYptr;
    EventQueue: ALLEGRO_EVENT_QUEUEptr;
    Event: ALLEGRO_EVENT;
    EndLoop, RightButtonDown, Redraw: BOOLEAN;
    FakeX, FakeY, th: INTEGER;
    White, Black, Red: ALLEGRO_COLOR;

BEGIN
  RightButtonDown := FALSE;
  Redraw := TRUE;
  FakeX := 0; FakeY := 0;

  IF NOT al_init THEN
    AbortExample ('Could not init Allegro.');
  al_init_primitives_addon;
  al_init_font_addon;
  al_init_image_addon;
  al_install_mouse;
  al_install_keyboard;

  al_set_new_display_flags (ALLEGRO_WINDOWED);
  Display := al_create_display (Width, Height);
  IF Display = NIL THEN
    AbortExample ('Could not create display.');

  EventQueue := al_create_event_queue;
  al_register_event_source (EventQueue, al_get_display_event_source (Display));
  al_register_event_source (EventQueue, al_get_mouse_event_source);
  al_register_event_source (EventQueue, al_get_keyboard_event_source);

  Font := al_load_font ('data/fixed_font.tga', 0, 0);
  White := al_map_rgb_f (1, 1, 1);
  Black := al_map_rgb_f (0, 0, 0);
  Red   := al_map_rgb_f (1, 0, 0);

  EndLoop := FALSE;
  REPEAT
    al_get_next_event (EventQueue, Event);
    IF Redraw AND al_is_event_queue_empty (EventQueue) THEN
    BEGIN
      th := al_get_font_line_height (Font);

      al_clear_to_color (Black);

      IF RightButtonDown THEN
      BEGIN
	al_draw_line (Width DIV 2, Height DIV 2, FakeX, FakeY, Red, 1);
	al_draw_line (FakeX - 5, FakeY, FakeX + 5, FakeY, White, 2);
	al_draw_line (FakeX, FakeY - 5, FakeX, FakeY + 5, White, 2);
      END;

      al_draw_text (Font, White, 0, 0, 0,
	Format ('x: %d y: %d dx: %d dy %d',
	  [event.mouse.x, event.mouse.y, event.mouse.dx, event.mouse.dy]
	)
      );
      al_draw_text (Font, White, Width DIV 2, Height DIV 2 - th,
	ALLEGRO_ALIGN_CENTRE, 'Left-Click to warp pointer to the middle once.'
      );
      al_draw_text (Font, White, Width DIV 2, Height DIV 2,
	ALLEGRO_ALIGN_CENTRE,
	'Hold right mouse button to constantly move pointer to the middle.'
      );
      al_flip_display;
      Redraw := FALSE;
    END;

    al_wait_for_event (EventQueue, Event);
    CASE Event._type OF
    ALLEGRO_EVENT_DISPLAY_CLOSE:
      EndLoop := TRUE;
    ALLEGRO_EVENT_KEY_DOWN:
      IF Event.keyboard.keycode = ALLEGRO_KEY_ESCAPE THEN
	EndLoop := TRUE;
    ALLEGRO_EVENT_MOUSE_WARPED: { ??? }
    { WriteLn ('Warp') };
    ALLEGRO_EVENT_MOUSE_AXES:
      BEGIN
	IF RightButtonDown THEN
	BEGIN
	  al_set_mouse_xy (Display, Width DIV 2, Height DIV 2);
	  INC (FakeX, Event.mouse.dx);
	  INC (FakeY, Event.mouse.dy);
	END;
	Redraw := TRUE;
      END;
    ALLEGRO_EVENT_MOUSE_BUTTON_DOWN:
      BEGIN
	IF Event.mouse.button = 1 THEN
	  al_set_mouse_xy (Display, Width DIV 2, Height DIV 2);
	IF Event.mouse.button = 2 THEN
	BEGIN
	  RightButtonDown := TRUE;
	  FakeX := Width DIV 2;
	  FakeY := Height DIV 2;
	END;
	Redraw := TRUE;
      END;
    ALLEGRO_EVENT_MOUSE_BUTTON_UP:
      IF Event.mouse.button = 2 THEN
        RightButtonDown := FALSE;
    END;
  UNTIL EndLoop;

  al_destroy_font (Font);
  al_destroy_event_queue(EventQueue);
  al_destroy_display (Display);
END.
