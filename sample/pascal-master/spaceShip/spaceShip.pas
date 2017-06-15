program theSpaceShip;

{
  Coded by Mohamed Aziz Knani,
  medazizknani@gmail.com 
  mohamedazizknani.wordpress.com 
  Art is not made by me i just
  borrowed it from the World Wide Web
  Code is not commented but it's written well 
  and it's indented
  Notice : you can use this code in your projects 
  or develop this game but you must notice the real 
  author :v 
  2015
}
  
uses Allegro5, Al5image,
     Al5acodec, Al5audio,
     Sysutils, Al5font,
     Al5ttf, Math,
     Al5primitives;


Const
  FPS = 60;
  frame_delay = 10;
  screen_h = 800;
  screen_w = 600;
type 
  projectiles = (Bullet, Rocket);
  bu = array [1..10] of record 
                         ID : projectiles;
                         live : Boolean;
                         x, y  : real;
                         rad, speed : real;
                         Img   : ALLEGRO_BITMAPptr;
                         
                       end;
  Ship      = record
                s, x, y, rad : real;
                deg, Health  : integer;
                live         : boolean;
              end;

  asteroids = array of record 
                                Image : ALLEGRO_BITMAPptr;
                                x, y  : real;
                                rad   : real;
                                live  : boolean;
                                speed : real;
                                _size : byte;  
                              end; 
  spriteZ   = record
                   curr_frame, frame_count,
                   frame_delay, max_frames,
                   frame_height, frame_width : byte;
                    _size               : integer; 
                   images                    : array of ALLEGRO_BITMAPptr;
                   live                      : Boolean;
                   x, y : real;
                 end;

  evil     = array [1..5]  of record
               x, y, rad : real;
               deg       : integer;
               live      : boolean;
               health    : integer;
             end;

Var
  Display       : ALLEGRO_DISPLAYptr;
  redfighter,
  SpaceWall     : ALLEGRO_BITMAPptr;  
  cond          : boolean; 
  timer         : ALLEGRO_TIMERptr;
  event_queue   : ALLEGRO_EVENT_QUEUEptr;
  ev            : ALLEGRO_EVENT;
  i             : byte;
  Up, Down, 
  redraw, Turbo : Boolean;
  Bullets       : bu; 
  TShip         : Ship;
  Mis           : boolean;
  song, 
  bulletSound   : ALLEGRO_SAMPLEptr;
  songInstance  : ALLEGRO_SAMPLE_INSTANCEptr;
  Tasteroids    : asteroids;
  asterNum      : byte; 
  rotatingStar  : spriteZ;
  score         : LongInt; 
  Tevil         : evil;
  Tevimg        : ALLEGRO_BITMAPptr;
  maxAst        : byte;
  Font1         : ALLEGRO_FONTptr;
  gameOver      : ALLEGRO_BITMAPptr;
  Explosions    : array [1..10] of spriteZ;
  
procedure Initprojectiles(var Bullets : bu); forward;

procedure InitAst(var Tasteroids : asteroids; Start, maxAst : byte); forward;

procedure spawnStar(var rotatingStar : spriteZ); forward;


procedure alInit;
begin 
  al_init();
  al_init_image_addon();
  al_init_primitives_addon();
  al_init_acodec_addon();
  al_install_audio();
  al_install_keyboard();
  al_init_font_addon();
  al_init_ttf_addon(); 
  bulletSound := al_load_sample('bullet.ogg');
  al_reserve_samples(2);
  
  Font1 := al_load_font('alterebro-pixel-font.ttf', 72, 0);
  Display := al_create_display(screen_h, screen_w);
  timer := al_create_timer(1.0/FPS);
  event_queue := al_create_event_queue();
  gameOver := al_load_bitmap('gameover.png'); 
  al_register_event_source(event_queue, al_get_display_event_source(Display));
  al_register_event_source(event_queue, al_get_timer_event_source(timer));
  al_register_event_source(event_queue, al_get_keyboard_event_source());
  
  song := al_load_sample('music.ogg');
  songInstance := al_create_sample_instance(song);
  al_set_sample_instance_playmode(songInstance, ALLEGRO_PLAYMODE_LOOP);
  al_attach_sample_instance_to_mixer(songInstance, al_get_default_mixer());
  al_play_sample_instance(songInstance);
end;

procedure Init;
var 
  i, j : byte;
begin
  randomize;
  TShip.s := 1;
  TShip.x := 200;
  TShip.y := 100;
  TShip.x := 100;
  
  Mis := False;

  cond := True;
  score := 0; 
  TShip.Health := 10;
  maxAst := 5;
  Setlength(Tasteroids, 5);

  Initprojectiles(Bullets);
  InitAst(Tasteroids, 0, maxAst);
  
  
  redfighter := al_load_bitmap('redfighter.png');
  SpaceWall := al_load_bitmap('galaxy.jpg');
  
  { init the rotatingstar }
  setLength(rotatingStar.images, 16);
  rotatingStar.frame_delay := 2;
  rotatingStar.max_frames  := 16;
  rotatingStar.live        := False;

  for i:= 0 to rotatingStar.max_frames-1 do 
    rotatingStar.images[i] := al_load_bitmap('rotStar/a-'+InttoStr(i)+'.png');

  rotatingStar.live := True;
  rotatingStar._size := 72;
  
  for j:=1 to 10 do begin   
    Setlength(Explosions[j].images, 16);
    Explosions[j].frame_delay := 1;
    Explosions[j].max_frames := 16;
    Explosions[j].live := False;
    Explosions[j].frame_height := 200;
    Explosions[j].frame_width := 200;
    for i:=0 to Explosions[j].max_frames-1 do 
      Explosions[j].images[i] := al_load_bitmap('explosion/exp-'+IntToStr(i)+'.png'); 
  end; 
  spawnStar(rotatingStar);
    
end;

procedure Initprojectiles(var Bullets : bu);
Var
  i : byte;
begin

  for i:=1 to 10 do begin
    Bullets[i].ID := Bullet;
    Bullets[i].Img := al_load_bitmap('bullet1.png');
    Bullets[i].live := False;
    Bullets[i].speed := 10;
  end;
end;

procedure Fireprojectile(var Bullets : bu; TShip : Ship);
var 
  i : byte;
begin
  for i:=1 to 10 do 
    if not(Bullets[i].live) then begin
      Bullets[i].rad := TShip.rad + (random(3) * 0.1) ;
      Bullets[i].live := True;
      Bullets[i].x := TShip.x;
      Bullets[i].y := TShip.y;
      break;
    end;
end;

procedure Updateprojectile(var Bullets : bu) ;
var 
  i : byte;
begin
  for i:=1 to 10 do 
    if Bullets[i].live then begin
      Bullets[i].x += trunc(cos(Bullets[i].rad)*Bullets[i].speed);
      Bullets[i].y += trunc(sin(Bullets[i].rad)*Bullets[i].speed); 
      if ((screen_h+16 < Bullets[i].x) or  (-16 > Bullets[i].y)) 
      or ((screen_w+16 < Bullets[i].y) or (-16 > Bullets[i].x)) then begin
        Bullets[i].live := False;
        continue;
      end;
      al_draw_rotated_bitmap(Bullets[i].Img, 16 / 2, 16 / 2, Bullets[i].x, Bullets[i].y, Bullets[i].rad, 0); 
      
    end;
end;

procedure InitAst(var Tasteroids : asteroids; Start, maxAst : byte);
var 
  i, r : byte;
begin
  for i:=Start to maxAst-1  do begin 
    Tasteroids[i].x     := 0;
    Tasteroids[i].y     := 0; 
    Tasteroids[i].live  := False;
    r                   := random(3)+1; 
    Tasteroids[i].Image := al_load_bitmap('asteroid/asteroid'+ InttoStr(r) +'.png');
    al_convert_mask_to_alpha(Tasteroids[i].Image, al_map_rgb(255, 255, 255)) ;
    Tasteroids[i].speed := r*1.5;
    case r of 
      1 : Tasteroids[i]._size := 32;
      2 : Tasteroids[i]._size := 40;
      3 : Tasteroids[i]._size := 44;
    end; 
  end;
end;

procedure spawnAst(var Tasteroids : asteroids; maxAst : byte);
var 
  i : byte;
begin
  for i:=0 to maxAst-1  do
    if not(Tasteroids[i].live) then begin
      Tasteroids[i].live := True;
      Tasteroids[i].rad  := random(361) * pi / 180;
      Tasteroids[i].x    := random(801);
      Tasteroids[i].y    := -44;
      break;
    end;
end;

procedure UpdateAst(var Tasteroids : asteroids; maxAst : byte) ;
var 
  i : byte;
begin
  for i:=0 to maxAst-1 do 
    if Tasteroids[i].live then begin
      Tasteroids[i].x += trunc(cos(Tasteroids[i].rad)*Tasteroids[i].speed);
      Tasteroids[i].y += trunc(sin(Tasteroids[i].rad)*Tasteroids[i].speed); 
      if ((screen_h+44 < Tasteroids[i].x) or  (-44 > Tasteroids[i].y)) 
      or ((screen_w+44 < Tasteroids[i].y) or (-44 > Tasteroids[i].x)) then begin
        Tasteroids[i].live := False;
        continue;
      end;
      al_draw_rotated_bitmap(Tasteroids[i].Image, Tasteroids[i]._size / 2, Tasteroids[i]._size / 2, 
        Tasteroids[i].x, Tasteroids[i].y, Tasteroids[i].rad, 0); 
      
    end;
end;

procedure collision(var Tasteroids : asteroids; var Bullets : bu; maxAst : byte); 
var 
  i, j : byte;
begin
  for i:=1 to 10 do 
    for j:=0 to maxAst-1 do 
      if (Tasteroids[j].live) then
    	if((Bullets[i].x > (Tasteroids[j].x - Tasteroids[i]._size)) 
        and (Bullets[i].x < (Tasteroids[j].x + Tasteroids[j]._size)) 
        and ((Bullets[i].y > (Tasteroids[j].y - Tasteroids[j]._size)) 
        and (Bullets[i].y < (Tasteroids[j].y + Tasteroids[j]._size)))) then begin
          Bullets[i].live := False;
          Tasteroids[j].live := False;
        end;
end;

procedure spriteMover(var curr_frame, frame_count : byte; frame_delay, max_frames : byte);
begin
  
  if (frame_count >= frame_delay ) then begin
    curr_frame := curr_frame +1;
    if (curr_frame >= max_frames) then curr_frame := 0; 
    frame_count := 0;
  end; 
  frame_count := frame_count +1;
end;


procedure spawnStar(var rotatingStar : spriteZ);
begin
  rotatingStar.live := True;
  rotatingStar.x := random(601)+100;
  rotatingStar.y := random(401)+100;
end;

procedure starCollision(var rotatingStar : spriteZ; TShip : Ship);
begin
  if((TShip.x > (rotatingStar.x - rotatingStar._size)) 
  and (TShip.x < (rotatingStar.x + rotatingStar._size)) 
  and ((TShip.y > (rotatingStar.y - rotatingStar._size)) 
  and (TShip.y < (rotatingStar.y + rotatingStar._size)))) then 
    rotatingStar.live := False;
end;

procedure shipCollision(var TShip : Ship; var Tasteroids : asteroids; maxAst : byte);
var 
  j, i, k : byte;
begin 
  for j:= 0 to maxAst-1 do 
    if((TShip.x > (Tasteroids[j].x - Tasteroids[j]._size)) 
    and (TShip.x < (Tasteroids[j].x + Tasteroids[j]._size)) 
    and ((TShip.y > (Tasteroids[j].y - Tasteroids[j]._size)) 
    and (TShip.y < (Tasteroids[j].y + Tasteroids[j]._size)))) then begin 
      TShip.Health -= 1;
      Tasteroids[j].live := False;
      for i:=1 to 10 do 
       if not(Explosions[i].live ) then 
        with Explosions[i] do begin 
          x := TShip.x - Explosions[i].frame_width / 2;
          y := TShip.y- Explosions[i].frame_height / 2;
          live := True;
          {break;}
        end;
    end; 
end;

procedure drawHealth(TShip : Ship);
begin
  al_draw_filled_rectangle(420, 570, 760, 595 ,al_map_rgb(0, 0, 0));
  al_draw_filled_rectangle(440, 572.5, 440+(TShip.Health*30), 592.5, al_map_rgb(232, 44, 12)); 
end;
{
procedure updateEvil(var Tevil : Evil);
begin
   
end;
}
procedure destruct;
var 
  i : byte;
begin

  for i:=0 to rotatingStar.max_frames-1 do 
    al_destroy_bitmap(rotatingStar.images[i]);
  
  al_destroy_bitmap(Tevimg); 
  al_destroy_bitmap(SpaceWall);
  al_destroy_sample(bulletSound);
  al_destroy_sample(song);
  al_destroy_sample_instance(songInstance);
  al_destroy_bitmap(redfighter);
  al_destroy_event_queue(event_queue); 
  al_destroy_timer(timer);  
  al_destroy_display(Display); 
end;

BEGIN
  alInit;
  Init;
  al_start_timer(timer);
  while cond do begin
    al_wait_for_event(event_queue, ev);

    if (ev._type = ALLEGRO_EVENT_DISPLAY_CLOSE) then cond := False
    else if (ev._type = ALLEGRO_EVENT_KEY_DOWN) then 
      case ev.keyboard.keycode of 
        AlLEGRO_KEY_UP    : Up    := True;
        ALLEGRO_KEY_DOWN  : Down  := True;
        ALLEGRO_KEY_SPACE : Turbo := True;
        ALLEGRO_KEY_W     : begin
                            if Mis = False then  
                             al_play_sample(bulletSound, 1, 0, 1, ALLEGRO_PLAYMODE_ONCE, nil);
                             Mis   := True; 
                           end;
        ALLEGRO_KEY_N     : if TShip.Health <= 0 then Init; 
      end
    else if (ev._type = ALLEGRO_EVENT_KEY_UP) then 
       case ev.keyboard.keycode of 
        AlLEGRO_KEY_UP    : Up    := False;
        ALLEGRO_KEY_DOWN  : Down  := False;
        ALLEGRO_KEY_SPACE : Turbo := False;
        ALLEGRO_KEY_W     : Mis   := False;  
       end
    else if (ev._type = ALLEGRO_EVENT_TIMER) then begin
      spawnAst(Tasteroids, maxAst); 
      if (Turbo) and (TShip.s <= 7)  then TShip.s += 0.1  
      else if TShip.s > 0 then TShip.s -= 0.1;
      if Up then TShip.deg -= 3 
      else if Down then TShip.deg += 3;
      if Mis then Fireprojectile(Bullets, TShip);
      TShip.rad := TShip.deg * pi /180; 
      TShip.x += trunc(cos(TShip.rad)*TShip.s);
      TShip.y += trunc(sin(TShip.rad)*TShip.s);
      {object redirection} 
     if (screen_h+68 < TShip.x) then TShip.x := -68
     else if (-76 > TShip.y) then TShip.y := screen_w+76
     else if (screen_w+76 < TShip.y) then TShip.y := -76
     else if (-68 > TShip.x) then TShip.x := screen_h+68; 
     redraw := True;
     if redraw and al_is_event_queue_empty(event_queue) then begin
       al_draw_bitmap(SpaceWall, 0, 0, 0);
       {al_clear_to_color(al_map_rgb(0, 12, 11));}
       { Drawing the ship :3 }
       collision(Tasteroids, Bullets, maxAst);
       Updateprojectile(Bullets);
       with rotatingStar do begin
         spriteMover(curr_frame, frame_count, frame_delay, max_frames);
         al_draw_bitmap(images[curr_frame], x, y, 0);
         starCollision(rotatingStar, TShip); 
         if not(live) then begin 
           {  add score   }
           maxAst += 1;
           Setlength(Tasteroids, maxAst);
           InitAst(Tasteroids, maxAst-1, maxAst);
           score += 10;
           score += score div 10; 
           spawnStar(rotatingStar);
         end;
       end;
       shipCollision(TShip, Tasteroids, maxAst); 
        for i:=1 to 10 do begin
        if (Explosions[i].live) then begin
          with Explosions[i] do begin
            spriteMover(curr_frame, frame_count, frame_delay, max_frames);
            al_draw_bitmap(images[curr_frame], x, y, 0);
            if (curr_frame=max_frames-1) then begin
              curr_frame := 0;
              frame_count := 0;
              live := False;
            end;
          end;
          end;
        end;
       al_draw_rotated_bitmap(redfighter, 68 / 2, 76 / 2, TShip.x , TShip.y, TShip.rad  , 0);
       UpdateAst(Tasteroids, maxAst);
       { Draw score } 
       al_draw_text(Font1, al_map_rgb(232, 44, 12), 20, 550, ALLEGRO_ALIGN_LEFT, IntToStr(score));
       {  Draw Health bar }
       drawHealth(TShip);
       if TShip.Health <= 0 then begin
          al_draw_bitmap(gameOver, 0, 0, 0); 
       end;
      al_flip_display();
      redraw := False;
     end; 
    end;

  end;

  destruct;
END.


{EOC (End Of Code yes I try always to be funny :v)}
