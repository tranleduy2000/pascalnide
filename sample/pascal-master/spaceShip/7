
uses Allegro5, Al5image;

Const
  FPS = 60;
  frame_delay = 10;
  screen_h = 800;
  screen_w = 600;
type 
  projectiles = (Bullet, Rocket);
  bu = array [1..5] of record 
                         ID : projectiles;
                         live : Boolean;
                         x, y, speed : integer;
                       end;

Var
  Display       : ALLEGRO_DISPLAYptr;
  redfighter,
  SpaceWall     : ALLEGRO_BITMAPptr;  
  cond          : boolean; 
  timer         : ALLEGRO_TIMERptr;
  event_queue   : ALLEGRO_EVENT_QUEUEptr;
  ev            : ALLEGRO_EVENT;
  frame_count, 
  curr_frame, i : byte;
  deg           : integer;
  rad, y, x, s  : real;
  Up, Down, 
  redraw, Turbo  : Boolean;
  Bullets    : bu;
  pro : bu;

procedure Initprojectiles(var Bullets : bu);
begin
  for i:=1 to 5 do begin
    Bullets.ID := Bullet;
    Bullets.live := False;
    Bullets.speed := 1;
  end;
end;

procedure Fireprojectile(var )

BEGIN
  s := 1;
  x := 200;
  y := 100;
  curr_frame  := 1;
  frame_count := 0;
  x := 100;
  cond := True;
  al_init();
  al_init_image_addon();
  al_install_keyboard(); 
  Initprojectiles(Bullets);
  Display := al_create_display(screen_h, screen_w);
  timer := al_create_timer(1.0/FPS);
  event_queue := al_create_event_queue();
  al_register_event_source(event_queue, al_get_display_event_source(Display));
  al_register_event_source(event_queue, al_get_timer_event_source(timer));
  al_register_event_source(event_queue, al_get_keyboard_event_source());
  al_start_timer(timer);


  redfighter := al_load_bitmap('redfighter.png');
  SpaceWall := al_load_bitmap('galaxy.jpg');
  while cond do begin
    al_wait_for_event(event_queue, ev);

    if (ev._type = ALLEGRO_EVENT_DISPLAY_CLOSE) then cond := False
    else if (ev._type = ALLEGRO_EVENT_KEY_DOWN) then 
      case ev.keyboard.keycode of 
        AlLEGRO_KEY_UP    : Up    := True;
        ALLEGRO_KEY_DOWN  : Down  := True;
        ALLEGRO_KEY_SPACE : Turbo := True;
      end
    else if (ev._type = ALLEGRO_EVENT_KEY_UP) then 
       case ev.keyboard.keycode of 
        AlLEGRO_KEY_UP    : Up    := False;
        ALLEGRO_KEY_DOWN  : Down  := False;
        ALLEGRO_KEY_SPACE : Turbo := False; 
       end
    else if (ev._type = ALLEGRO_EVENT_TIMER) then begin 
      if Turbo then s += 0.1  
      else if s > 0 then s -= 0.1;
      if Up then deg -= 3 
      else if Down then deg += 3;
      rad := deg * pi /180;
     
      x += trunc(cos(rad)*s);
      y += trunc(sin(rad)*s);
      {object redirection} 
     if (screen_h+68 < x) then x := -68
     else if (-76 > y) then y := screen_w+76
     else if (screen_w+76 < y) then y := -76
     else if (-68 > x) then x := screen_h+68; 
     redraw := True;
     if redraw and al_is_event_queue_empty(event_queue) then begin
       al_draw_bitmap(SpaceWall, 0, 0, 0);
       {al_clear_to_color(al_map_rgb(0, 12, 11));}
       { Drawing the ship :3 }
       al_draw_rotated_bitmap(redfighter, 68 / 2, 76 / 2, x , y, rad  , 0);

     al_flip_display();
     redraw := False;
     end; 
    end;

  end;
  al_destroy_bitmap(redfighter);
  al_destroy_event_queue(event_queue); 
  al_destroy_timer(timer);  
  al_destroy_display(Display); 
END.



