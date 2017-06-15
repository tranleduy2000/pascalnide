program Breakout;
uses Allegro5, Al5primitives;
Const
  FPS = 60;
  length_paddle = 100;
type
  Colors    = (RED, WHITE, BLUE, YELLOW, GREEN, NONE);
  Ball = record
    BallX, BallY, BallS, BallD : integer;
  end;
  
  Paddle = record
    Posx : integer;
    Ldir, Rdir  : boolean;
  end;
  
  Blocks = array [1..5, 1..10] of Colors;
Var
  Display : ALLEGRO_DISPLAYptr;
  PBall   : Ball;
  PPaddle : Paddle;
  PBlocks : Blocks;
  PColors : Colors;
procedure init;
var
  i, j : byte;
begin
  al_init();
  al_init_primitives_addon();
  al_install_keyboard();
  PPaddle.Posx := 300;
  PBall.BallX := 350;
  PBall.BallY := 645;
  PBall.BallD := 290;
  PBall.BallS := 10;
  {
    for i:=1 to 5 do
      for j:=1 to 10 do 
        PBlocks[i, j] := WHITE;
  }
  PBlocks[1, 1] := RED; PBlocks[1, 2] := YELLOW; PBlocks[1, 3] := RED; PBlocks[1, 4] := RED; PBlocks[1, 5] := RED; PBlocks[1, 6] := RED; PBlocks[1, 7] := RED; PBlocks[1, 8] := RED; PBlocks[1, 9] := RED; PBlocks[1, 10] := RED;
end;

procedure GameLoop(PBall : Ball; PPaddle : Paddle);

  procedure Draw(PPaddle : Paddle; PBall : Ball);
  var
    i, j  : byte;
    Color : ALLEGRO_COLOR; 
  begin
    al_clear_to_color(al_map_rgb(0, 0, 0));
    // Draw the Paddle
    al_draw_line(PPaddle.Posx, 680, PPaddle.Posx+length_paddle, 680, al_map_rgb(255, 255, 255), 30 );
    
    // Draw ball
    al_draw_filled_rectangle(PBall.BallX, PBall.BallY, PBall.BallX+20, PBall.BallY+20, al_map_rgb(255, 255, 255));
    for i:=1 to 5 do
      for j:=0 to 9 do 
        if PBlocks[i, j+1] <> NONE then begin
          case PBlocks[i, j+1] of
            RED    : Color := al_map_rgb(220, 0, 0);
            WHITE  : Color := al_map_rgb(255, 255, 255);
            BLUE   : Color := al_map_rgb(17, 44, 214);
            YELLOW : Color := al_map_rgb(246, 246, 10);
            GREEN  : Color := al_map_rgb(139, 105, 20);
          end;
          al_draw_line(j*70+10, i*50, j*70+75, i*50, Color, 30 );
      end;    
    al_flip_display();
  end;
Var
  timer               : ALLEGRO_TIMERptr;
  redraw, Closed      : boolean;
  event_queue         : ALLEGRO_EVENT_QUEUEptr;
  ev                  : ALLEGRO_EVENT;
  i, j                : byte;
begin
  Closed := False;
  event_queue := al_create_event_queue();
  timer := al_create_timer(1.0/FPS);
  al_register_event_source(event_queue, al_get_keyboard_event_source);
  al_register_event_source(event_queue, al_get_display_event_source(Display));
  al_register_event_source(event_queue, al_get_timer_event_source(timer));
  al_start_timer(timer);
  While not(Closed) do begin
    al_wait_for_event(event_queue, ev);
    if (ev._type = ALLEGRO_EVENT_KEY_DOWN) then
      case ev.keyboard.keycode of
        ALLEGRO_KEY_LEFT  : PPaddle.Ldir := True;
        ALLEGRO_KEY_RIGHT : PPaddle.Rdir := True;
      end
    else if (ev._type = ALLEGRO_EVENT_KEY_UP) then 
      case ev.keyboard.keycode of
        ALLEGRO_KEY_LEFT : PPaddle.Ldir := False;
        ALLEGRO_KEY_RIGHT : PPaddle.Rdir := False;
      end
    else if (ev._type = ALLEGRO_EVENT_DISPLAY_CLOSE ) then Closed := True
    else if (ev._type = ALLEGRO_EVENT_TIMER) then begin
      if PPaddle.Ldir and (PPaddle.Posx-10>=10) then PPaddle.Posx -= 10
      else if PPaddle.Rdir and (PPaddle.Posx+10+length_paddle<=690) then PPaddle.Posx +=10;
      with PBall do begin
        Ballx += trunc(cos(Pi / 180 * BallD)*BallS);
        BallY += trunc(sin(Pi / 180 * BallD)*BallS) ;
      end;
      if (PBall.BallX <= 20) or (PBall.BallX >= 680) then PBall.BallD += (90 - PBall.BallD)*2;
      if (PBall.BallY <= 0) then PBall.BallD += (180 - PBall.BallD) *2;
      // Cheking Collision With the paddle
      if ((PBall.BallY >= 650) and (PBall.BallY<=660)) 
         and ((PBall.BallX+20 >= PPaddle.Posx) and (PBall.BallX+20<= PPaddle.Posx+length_paddle))  then begin
           PBall.BallD += (180 - PBall.BallD - random(4)) *2; // adding some randomness :3
           if (PBall.BallX in [PPaddle.Posx..PPaddle.Posx+40]) or (PBall.BallX in [PPaddle.Posx+60..PPaddle.Posx+length_paddle]) then PBall.BallS := 18
             else PBall.BallS := 10;
      end;
      // Check collision with blocks
      // I didn't use The power of maths but this works (it's Ugly and slow);
      for i:=1 to 5 do
        for j:=0 to 9 do
          // So the Ball Was hit
          if (((PBall.BallX >= j*70+10) and (PBall.BallX <= j*70+75)) and ((PBall.BallY >=i*50) and (PBall.BallY <=i*50+10))) and (PBlocks[i, j+1]<>NONE)then begin 
            // hit from the below or above 
            if (PBall.BallY <= i*50+10) or (PBall.BallY >= i) then 
              PBall.BallD += (180 - PBall.BallD) *2
            // hit from the sides
            else if (PBall.BallX <= j*70+10) or ((PBall.BallX <= j*70+75)) then
              PBall.BallD += (90 - PBall.BallD) *2;
            PBlocks[i, j+1] := NONE;
          end;
      redraw := True;
    end;
    if redraw and al_is_event_queue_empty(event_queue) then begin 
      Draw(PPaddle, PBall);
      redraw := False;
    end;
  end;
end;

BEGIN
  init;
  Display := al_create_display(700, 700);
  al_set_window_title(Display, 'Crazy Breakout');
  GameLoop(PBall, PPaddle);
END.
