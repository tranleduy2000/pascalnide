
uses Allegro5, Al5image, sysutils;

Const
  FPS = 60;
  frame_delay = 10;

Var
  Display     : ALLEGRO_DISPLAYptr;
  Sprites     : array [1..8] of ALLEGRO_BITMAPptr;
  cond        : boolean; 
  timer       : ALLEGRO_TIMERptr;
  event_queue : ALLEGRO_EVENT_QUEUEptr;
  ev          : ALLEGRO_EVENT;
  frame_count, curr_frame, i : byte;
  deg: integer;
  rad, y, x, s : real;
  Up, Down, redraw
  : Boolean;

BEGIN
  s := 3;
  curr_frame  := 1;
  frame_count := 0;
  x := 100;
  cond := True;
  al_init();
  al_init_image_addon();
  al_install_keyboard(); 
  Display := al_create_display(700, 300);
  timer := al_create_timer(1.0/FPS);
  event_queue := al_create_event_queue();
  al_register_event_source(event_queue, al_get_display_event_source(Display));
  al_register_event_source(event_queue, al_get_timer_event_source(timer));
  al_register_event_source(event_queue, al_get_keyboard_event_source());
  al_start_timer(timer);

  for i:=1 to 8 do 
    Sprites[i] := al_load_bitmap('frame-'+InttoStr(i)+'.png');

  while cond do begin
    al_wait_for_event(event_queue, ev);

    if (ev._type = ALLEGRO_EVENT_DISPLAY_CLOSE) then cond := False
    else if (ev._type = ALLEGRO_EVENT_KEY_DOWN) then 
      case ev.keyboard.keycode of 
        AlLEGRO_KEY_UP : Up := True;
        ALLEGRO_KEY_DOWN : Down := True;
      end
    else if (ev._type = ALLEGRO_EVENT_KEY_UP) then 
       case ev.keyboard.keycode of 
        AlLEGRO_KEY_UP : Up := False;
        ALLEGRO_KEY_DOWN : Down := False;
       end
    else if (ev._type = ALLEGRO_EVENT_TIMER) then begin 
      if Up then deg -= 10
      else if Down then deg += 10;
      rad := deg * pi /180;
      { works great for collision }
      if cos(rad) >= 0 then x += cos(rad)*s;
      y += sin(rad)*s; 
      if (frame_count >= frame_delay ) then begin
        curr_frame := curr_frame +1;
        if (curr_frame > 8) then curr_frame := 1; 
        frame_count := 0; 
      end; 
      frame_count := frame_count +1;
      // x := x+4;
     if (700 <= x) then x := -105;
     redraw := True;
     if redraw and al_is_event_queue_empty(event_queue) then begin
       al_clear_to_color(al_map_rgb(122, 128, 160));
       al_draw_rotated_bitmap(Sprites[curr_frame], 154 / 2, 105 / 2, x , y, rad  , 0);
     al_flip_display();
     redraw := False;
     end; 
    end;

  end;

  for i:=1 to 8 do 
    al_destroy_bitmap(Sprites[i]);
  al_destroy_event_queue(event_queue); 
  al_destroy_timer(timer);  
  al_destroy_display(Display); 
END.
