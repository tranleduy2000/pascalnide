{
  * An Atari pong clone in Old Pascal
  * I called it Crazy Pong because of the weired bugs :3
  * This is what I call an ugly code but I commented it well
  * Made by Mohamed Aziz Knani
  * medazizknani[at]gmail.com
  * mohamedazizknani.wordpress.com
}
program pong;
uses Allegro5, Al5primitives, Al5font, Al5ttf, SysUtils;
const
  max_points = 10;
  length_paddle = 100;
  FPS = 60; // frame per second
Var
  Display                 : ALLEGRO_DISPLAYptr;
  scoreP1, scoreP2        : integer; // Player's score
  event_queue             : ALLEGRO_EVENT_QUEUEptr;
  ev                      : ALLEGRO_EVENT;
  Closed                  : boolean;
  posP1, posP2            : integer; // represents the Y (vertical) position of each player
  BallPx, BallPy, BallD, BallS   : integer; // Ball position, direction and speed
  p1UP, p1Do, p2UP, p2Do  : boolean; // Player is Up or Down
  timer                   : ALLEGRO_TIMERptr;
  redraw                  : boolean;
  i                       : byte;
  chX, chY                : integer;
  Font1                   : ALLEGRO_FONTptr;

BEGIN
  // init
  al_init();
  al_init_primitives_addon();
  al_init_font_addon();
  al_init_ttf_addon();
  al_install_keyboard();
  scorep1 := 0; scorep2 := 0;
  posP1 := 50;
  posP2 := 120;
  BallPx := 350;
  BallPy := 250;
  Closed := False;
  BallD := 45;
  BallS := 10;
  
  // Loading the font
  Font1 := al_load_font('alterebro-pixel-font.ttf', 100, 0);
  
  // Setting Display
  Display := al_create_display(700, 500);
  al_set_window_title(Display, 'Crazy Pong');
  
  // Setting the event Queue and timer
  event_queue := al_create_event_queue();
  timer := al_create_timer(1.0/FPS);
  al_register_event_source(event_queue, al_get_keyboard_event_source);
  al_register_event_source(event_queue, al_get_display_event_source(Display));
  al_register_event_source(event_queue, al_get_timer_event_source(timer));
  
  // The Game loop
  randomize;
  al_start_timer(timer);
  While not(Closed) or ((scorep1 = max_points) or (scorep2 = max_points)) do begin
    al_wait_for_event(event_queue, ev); // wait for an event
    if (ev._type = ALLEGRO_EVENT_KEY_DOWN) then
      case ev.keyboard.keycode of
        ALLEGRO_KEY_UP : p2UP := True;
        ALLEGRO_KEY_DOWN : p2Do := True; 
        ALLEGRO_KEY_A : p1UP := True;
        ALLEGRO_KEY_Q : p1Do := True;
      end
    else if (ev._type = ALLEGRO_EVENT_KEY_UP) then 
      case ev.keyboard.keycode of
        ALLEGRO_KEY_UP   : p2UP := False;
        ALLEGRO_KEY_DOWN : p2Do := False;
        ALLEGRO_KEY_A   : p1UP := False;
        ALLEGRO_KEY_Q : p1Do := False;
      end
    else if (ev._type = ALLEGRO_EVENT_DISPLAY_CLOSE ) then Closed := True
    else if (ev._type = ALLEGRO_EVENT_TIMER) then begin
      // Moving paddle 
      if p2UP and (posP2-10>=10) then posP2 -= 10
      else if P2Do and (posP2+10+length_paddle<=490) then posP2 += 10;
      if p1UP and (posP1-10>=10) then posP1 -= 10
      else if P1Do  and (posP1+10+length_paddle<=490) then posP1 += 10;
      
      // Moving with angles
      chX := trunc(cos(Pi / 180 * BallD)*BallS);
      chy := trunc(sin(Pi / 180 * BallD)*BallS);
      BallPx += chX;
      BallPY += chY;
      
      // Checking Collision with walls
      //if (BallPX <= 20) or (BallPX >= 680-15) then BallD += (90 - BallD) *2;
      if (BallPY <= 10) or (BallPY >= 470) then BallD += (180 - BallD) *2;
      
      // Cheking Collision With paddles 
      if ((BallPX >= 640) and (BallPX<=650)) 
         and ((BallPY >= posP2) and (BallPY<= posP2+length_paddle))  then begin
           BallD += (90 - BallD - random(4)) *2; // adding some randomness :3
           // if the ball were hit in the sides accelerate the ball 
           if (BallPY in [posP2..posP2+40]) or (BallPY in [posP2+60..posP2+length_paddle]) then BallS := 18
           else BallS := 10;
        end
      else if ((BallPX<=40) and (BallPy>=30)) 
      and ((BallPY >= posP1) and (BallPY<= posP1+length_paddle)) then begin
           BallD += (90 - BallD - random(4)) *2; // adding some randomness :3
           // if the ball were hit in the sides accelerate the ball 
           if (BallPY in [posP1..posP1+40]) or (BallPY in [posP1+60..posP1+length_paddle]) then BallS := 18
           else BallS := 10;
      end;
      if (BallPX>700) then begin
        BallPx := 350;
        BallPy := 250;
        BallD := random(100)+140;
        BallS := 7;
        Inc(scorep1);
      end
      else if (BallPX<0) then begin
        BallPx := 350;
        BallPy := 250;
        BallD := random(100)+330;
        BallS := 7;
        Inc(scoreP2);
      end; 
      Writeln(BallPX);
      redraw := True;
    end;
    if redraw and al_is_event_queue_empty(event_queue) then begin 
      // Draw board
      al_clear_to_color(al_map_rgb(0, 0, 0));
      al_draw_line(10, 0, 690, 0, al_map_rgb(255, 255, 255), 20);
      al_draw_line(10, 500, 690, 500, al_map_rgb(255, 255, 255), 20);
      
      // Draw Players
      al_draw_line(30, PosP1, 30, PosP1+100, al_map_rgb(255, 255, 255), 30 ); // draw 1st player
      al_draw_line(670, PosP2, 670, PosP2+100, al_map_rgb(255, 255, 255), 30 ); // draw 2nd player
      al_draw_filled_rectangle(BallPx, BallPy, BallPx+20, BallPy+20, al_map_rgb(255, 255, 255));
      
      // Draw the middle of the pitch
      for i:=1 to 20 do 
        al_draw_line(350, i*24, 350, i*24+10, al_map_rgb(255, 255, 255), 10 ); 
      
      // Draw Scores
      al_draw_text(Font1, al_map_rgb(255, 255, 255), 280, 30, ALLEGRO_ALIGN_CENTER, IntToStr(scorep1));
      al_draw_text(Font1, al_map_rgb(255, 255, 255), 420, 30, ALLEGRO_ALIGN_CENTER, IntToStr(scorep2));
      
      al_flip_display();
      redraw := False;
    end;
  end;
  al_destroy_event_queue(event_queue);
  al_destroy_timer(timer);
  al_destroy_display(Display);
END.
