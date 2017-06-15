{
  * A 2048 clone in Old pascal using Allegro.pas
  * Made by Mohamed Aziz Knani
  * medazizknani[at]gmail.com  
  * http://mohamedazizknani.wordpress.com
}
program Game2048;
{$H+}
uses Allegro5, Al5primitives, Al5font, Al5ttf;
type
  direction = (L, R, U, D, N); {
    Left, Right, Up, Down and None
  }
  Game = record
    Board       : array [1..4, 1..4] of LongInt;
    full, next  : boolean;
    dir         : direction;
    score       : LongInt;
  end;
var 
  Display         : ALLEGRO_DISPLAYptr;
  box_x, box_y    : integer;
  PGame           : Game; 
  event_queue     : ALLEGRO_EVENT_QUEUEptr;
  ev              : ALLEGRO_EVENT;
  Closed          : boolean;
{Just to use it as one liner instead of using the Ugly pascal procedure}
function IntToStr(int : Longint) : string;
Var
  st : string;
begin
  Str(int, st);
  IntToStr := st;
end;

procedure Draw(PGame : game);
var 
  Font1, Font2    : ALLEGRO_FONTptr;
  j, k, i         : byte;
  Color           : ALLEGRO_COLOR;
  
begin
  {Drawing the cells}
  al_clear_to_color(al_map_rgb(187, 173, 160));
  Font1 := al_load_font('Consolas.ttf', 32, 0);
  box_y := -100;
  box_x := 0;
  j:=0;
  k:=0;
  for i:=0 to 15 do begin
    box_x := box_x + 110;
    Inc(k);
    if (i mod 4 = 0) then begin
      box_x := 30;
      box_y := box_y + 110;
      Inc(j);
      k:=1;
    end;
    case PGAME.Board[j, k] of 
      0             : Color := al_map_rgb(204,192,179);
      2, 4          : Color := al_map_rgb(238,228,218);
      8, 16, 32, 64 : Color := al_map_rgb(242,177,121);
      128, 256, 512 : Color := al_map_rgb(220,155,27);
      else Color := al_map_rgb(0,0,0);
    end;
    al_draw_filled_rounded_rectangle(box_x, box_y, box_x+100, box_y+100, 10, 10, Color);
    if (PGame.board[j, k] <>0) then
      al_draw_text(Font1, al_map_rgb(119, 110, 110), box_x+50, box_y+40, ALLEGRO_ALIGN_CENTRE, IntToStr(PGame.board[j, k]));
  end;
  {Drawing score}
  Font2 := al_load_font('Consolas.ttf', 32, 0);
  al_draw_text(Font2, al_map_rgb(104, 104, 104), 10, 460, ALLEGRO_ALIGN_LEFT,  concat('Score : ', IntToStr(PGame.Score)));
  al_flip_display();
end; 

procedure spawnTile(var PGame : Game);
  function rand(x, y : byte) : byte;
  Var 
    r : byte;
  begin
    randomize();
    r := random(2);
    if r=0 then rand := x
    else rand:=y;
  end;
Var 
  x, y : byte;
begin
  randomize();
  repeat
    x:=random(4)+1;
    y:=random(4)+1;
  until PGame.board[x, y] = 0;
  PGame.board[x, y] := rand(2, 4);
end;

procedure Pfull(var PGame : Game);
var
  bool : boolean;
  i, j : byte;
begin
  bool := True;
  for i:=1 to 4 do
    for j:=1 to 4 do 
      if PGame.board[i, j] = 0 then bool := False; 
  PGame.full := bool;
end;

procedure Pnext(var PGame : Game);
Var
  bool : boolean;
  i, j : byte;
begin
  bool := False;
  for i:=1 to 4 do
    for j:=1 to 3 do
      if (PGame.board[j, i]<>0) and (PGame.board[j, i]=PGame.board[j+1, i]) then bool := True
      else if (PGame.board[i, j]<>0) and (PGame.board[i, j]=PGame.board[i, j+1]) then bool:=True;
  for i:=1 to 4 do
    for j:=4 Downto 2 do
      if (PGame.board[j, i]<>0) and (PGame.board[j, i]=PGame.board[j-1, i]) then bool := True
      else if (PGame.board[i, j]<>0) and (PGame.board[i, j]=PGame.board[i, j+1]) then bool := True;
  
  PGame.next :=bool;     
end;

procedure mainGame(var PGame : Game);

  procedure movingUp(var PGame : Game);
  var 
    i, j, k : byte;
  begin
    for i:=1 to 4 do
      for j:=1 to 4 do begin
        if (PGame.board[j, i] = 0) then 
          for k:=j+1 to 4 do
            if (PGame.board[k, i]<>0) then begin
              PGame.board[j, i] := PGame.board[k, i];
              PGame.board[k, i] := 0;
              break;
            end;
      end;
  end;
  
  procedure movingDown(var PGame : Game);
  var 
    i, j, k : byte;
  begin
    for i:=1 to 4 do
      for j:=4 Downto 1 do begin
        if (PGame.board[j, i] = 0) then 
          for k:=j-1 Downto 1 do
            if (PGame.board[k, i]<>0) then begin
              PGame.board[j, i] := PGame.board[k, i];
              PGame.board[k, i] := 0;
              break;
            end;
      end;
  end;
  
  procedure movingLeft(var PGame : Game);
  var 
    i, j, k : byte;
  begin
    for i:=1 to 4 do
      for j:=1 to 4 do begin
        if (PGame.board[i, j] = 0) then 
          for k:=j+1 to 4 do
            if (PGame.board[i, k]<>0) then begin
              PGame.board[i, j] := PGame.board[i, k];
              PGame.board[i, k] := 0;
              break;
            end;
      end;
  end;
  
  procedure movingRight(var PGame : Game);
  var 
    i, j, k : byte;
  begin
    for i:=1 to 4 do
      for j:=4 Downto 1 do begin
        if (PGame.board[i, j] = 0) then 
          for k:=j-1 Downto 1 do
            if (PGame.board[i, k]<>0) then begin
              PGame.board[i, j] := PGame.board[i, k];
              PGame.board[i, k] := 0;
              break;
            end;
      end;
  end;  

  procedure updateUp(var PGame : Game);
  var 
    i, j : byte;
  begin
    for i:=1 to 4 do
      for j:=1 to 3 do begin
        if (PGame.board[j, i]<>0) and (PGame.board[j, i]=PGame.board[j+1, i]) then begin
          PGame.score+=PGame.board[j+1, i]*2;
          PGame.board[j, i]+=PGame.board[j+1, i];
          PGame.board[j+1][i]:=0;
          break;
        end;
      end;
  end;
  
  procedure updateDown(var PGame : Game);
  var 
    i, j : byte;
  begin
    for i:=1 to 4 do
      for j:=4 Downto 2 do begin
        if (PGame.board[j, i]<>0) and (PGame.board[j, i]=PGame.board[j-1, i]) then begin
          PGame.score+=PGame.board[j-1, i]*2;
          PGame.board[j, i]+=PGame.board[j-1, i];
          PGame.board[j-1][i]:=0;
          break;
        end;
      end;
  end;
  
  procedure updateRight(var PGame : Game);
  var 
    i, j : byte;
  begin
    for i:=1 to 4 do
      for j:=4 Downto 2 do begin
        if (PGame.board[i, j]<>0) and (PGame.board[i, j]=PGame.board[i, j-1]) then begin
          PGame.score+=PGame.board[i, j-1]*2;
          PGame.board[i, j]+=PGame.board[i, j-1];
          PGame.board[i, j-1]:=0;
          break;
        end;
      end;
  end;
  
  procedure updateLeft(var PGame : Game);
  var 
    i, j : byte;
  begin
    for i:=1 to 4 do
      for j:=1 to 3 do begin
        if (PGame.board[i, j]<>0) and (PGame.board[i, j]=PGame.board[i, j+1]) then begin
          PGame.score+=PGame.board[i, j+1]*2;
          PGame.board[i, j]+=PGame.board[i, j+1];
          PGame.board[i, j+1]:=0;
          break;
        end;
      end;
  end;
  
begin
  {The moving and updating part}
  // this is chandruscm's algorithm simple and powerful (Written in C++) !  
  case PGame.dir of 
    U : begin
          movingUp(PGame);
          updateUp(PGame);
        end;
    D : begin
          movingDown(PGame);
          updateDown(PGame);
        end;
    L : begin
          movingLeft(PGame);
          updateLeft(PGame);
        end;
    R : begin
          movingRight(PGame);
          updateRight(PGame);
        end;
  end;
end;
  
BEGIN
  { init the PGame record}
  with PGame do begin
    Score       := 0;
    full        := False;
    next        := True;
    spawnTile(PGame);
    spawnTile(PGame);
  end;
  al_init();
  al_init_font_addon();
  al_init_primitives_addon();
  al_init_ttf_addon();
  al_install_keyboard();
  Display := al_create_display(500, 500);
  al_set_window_title(Display, '2048');
  event_queue := al_create_event_queue();
  al_register_event_source(event_queue, al_get_keyboard_event_source);
  al_register_event_source(event_queue, al_get_display_event_source(Display));
  Draw(PGame);
  Closed:=False;
  while not(Closed) or not(PGame.next) do begin
    al_wait_for_event(event_queue, ev);
    if (ev._type = ALLEGRO_EVENT_KEY_DOWN) then 
      case ev.keyboard.keycode of 
        ALLEGRO_KEY_UP : PGame.dir := U;
        ALLEGRO_KEY_DOWN : PGame.dir := D;
        ALLEGRO_KEY_LEFT : PGame.dir := L;
        ALLEGRO_KEY_RIGHT : PGame.dir := R;
      else PGame.dir := N;
      end
    else if (ev._type = ALLEGRO_EVENT_DISPLAY_CLOSE) then Closed := True;
    if (ev._type = ALLEGRO_EVENT_KEY_DOWN) then begin
      if PGame.dir <> N then begin
        mainGame(PGame);
        if not(PGame.full) then
          spawnTile(PGame);
      end;
      Pfull(PGame);
      Draw(PGame); 
      Pnext(PGame);
    end;
  end;
  if not(PGame.next) then begin
    al_draw_filled_rounded_rectangle(50, 50, 450, 450, 10, 10, al_map_rgb(255, 223, 223));
    al_draw_text(al_load_font('Consolas.ttf', 52, 0), al_map_rgb(119, 110, 110), al_get_display_width(display)/2, al_get_display_height(display)/2, ALLEGRO_ALIGN_CENTRE, 'Game Over!');
    al_flip_display();
  end; 
  al_destroy_display(Display);
END.
