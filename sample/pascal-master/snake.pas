(* Simple Snake game implemantation in Pascal
 * using ncurses; written be Mohamed Aziz Knani
 * Email : medazizknani@gmail.com
 * Sorry for the spaghetti code :v
 *)
program thePSnake;
{$H+}
uses ncurses;
type
  Apple = record
  Eaten : Boolean;
  Shape : char;
  Posi   : array [1..2] of integer;
  end;
  Snake = record
  Length   : integer;
  Shape    : char;
  Dead     : boolean;
  Dir      : char;
  HeadPos, BackPos : array [1..2] of integer;
  end;
  Board = array [2..25, 2..80] of  char;
var
  score, game    : PWindow;
  max_y, max_x   : integer; { max screen size }
  new_y, new_x   : integer; { new screen size }
  rand_y, rand_x : integer;
  PApple         : Apple;
  PSnake         : Snake;
  scr, ang, rk, i, j   : integer;
  dir              : char;
  PBoard : Board;

procedure drawBox(win : PWindow); (* it is PWindow in pascal not Window like in C and
                                       C++*)
var
  x, y, i : integer;
begin
  getmaxyx(win, y, x);
  mvwprintw(win, 0, 0, '+');
  mvwprintw(win, y - 1, 0, '+');
  mvwprintw(win, 0, x - 1, '+');
  mvwprintw(win, y - 1, x - 1, '+');
  for i:=1 to y - 2 do begin
    mvwprintw(win, i, 0, '|');
    mvwprintw(win, i, x - 1, '|');
  end;
  for i:= 1 to (x - 2) do begin
    mvwprintw(win, 0, i, '-');
    mvwprintw(win, y - 1, i, '-');
  end;
end;

procedure generateApple(max_y, max_x : integer; var rand_y, rand_x : integer);
begin
  randomize;
  rand_y := random(max_y-4)+2;
  rand_x := random(max_x-3)+2;
  mvwprintw(game, rand_y, rand_x, 'o');
end;

procedure movingSnake(var PSnake : Snake;Dir : char; var PBoard : Board);
var
  tmp_y, tmp_x : integer;
begin
  { Choice part }
  if (Dir <> PSnake.Dir) and not((((PSnake.Dir = 'L') and (Dir = 'R')) 
     or ((PSnake.Dir = 'R') and (Dir = 'L' ))) 
     or (((PSnake.Dir = 'U') and (Dir = 'D')) 
     or (PSnake.Dir = 'D') and (Dir = 'U'))) then begin
    if (Dir = 'U') then Dec(PSnake.HeadPos[1])
    else if (Dir = 'D') then Inc(PSnake.HeadPos[1])
    else if (Dir = 'R') then Inc(PSnake.HeadPos[2])
    else Dec(Psnake.HeadPos[2]);
    PSnake.Dir := Dir;
  end;
  { moving part }
  if (PSnake.Dir = 'R') then Inc(PSnake.HeadPos[2])
  else if (PSnake.Dir = 'L') then Dec(PSnake.HeadPos[2])
  else if (PSnake.Dir = 'U') then Dec(PSnake.HeadPos[1])
  else Inc(PSnake.HeadPos[1]);
  tmp_y := PSnake.HeadPos[1]; tmp_x := PSnake.HeadPos[2];
  { I Will do the printing in the Main function }
  PBoard[tmp_y][tmp_x] := 'S';
end;

procedure checkCollision(PSnake : Snake; var PApple : Apple);
begin
  if (PSnake.HeadPos[1] = PApple.Posi[1]) 
     and (PSnake.HeadPos[2] = PApple.Posi[2]) then
  PApple.Eaten := True
  else PApple.Eaten := False;
end;

function IntToStr (I : Longint) : String;
var 
  s : string;
begin
   Str(I,S);
    IntToStr:=s;
end;

BEGIN
  (* initialisations *)
  initscr();
  noecho();
  curs_set(0);
  (* let's disable this for a while *)
  nodelay(stdscr, True);
  (* let's make two windows *)
  getmaxyx(stdscr, max_y, max_x);
  game  := newwin(max_y-3, max_x, 0, 0);
  score := newwin(3, max_x, max_y-3, 0);
  (* we should refresh after we print  *)  
  drawBox(game);
  drawBox(score);
  { Objects initialisations }
  PApple.Eaten  := True;
  PApple.Shape  := 'o';
  PSnake.Shape  := 'O';
  PSnake.Length := 5;
  PSnake.Dead   := False;
  scr := -1;
  PSnake.HeadPos[1] := 5;
  PSnake.HeadPos[2] := 20;
  PSnake.Dir := 'D';
  for i:=2 to max_y-5 do
    for j:=2 to max_x do
      PBoard[i][j] := 'E'; { E means empty }
  nodelay(game, True);
  (* Game logic here *)
  while True do begin
    (* dynamic resizing *)
    getmaxyx(stdscr, new_y, new_x);
    if (new_y <> max_y) or (new_x <> max_x) then begin
      max_y:=new_y; max_x:=new_x;
      game  := newwin(max_y-3, max_x, 0, 0);
      score := newwin(3, max_x, max_y-3, 0);
      wclear(game);
      wclear(score);
      drawBox(game);
      drawBox(score);
    end;
    rk := wgetch(game);
    case rk of
      67    : Dir := 'R'; {Right}
      68    : Dir := 'L'; {Left}
      65    : Dir := 'U'; {UP}
      66    : Dir := 'D'; {Down}
    end;
    movingSnake(PSnake, Dir, PBoard);
    checkCollision(PSnake, PApple);
    if (PApple.Eaten) or (scr = -1) then begin
      {mvwdelch(game, rand_y, rand_x);}
      mvwprintw(game, rand_y, rand_x, ' ');
      PBoard[rand_y][rand_x] := 'E';
      generateApple(max_y, max_x, rand_y, rand_x);
      PBoard[rand_y][rand_x] := 'A'; { A means apple }
      Inc(scr);
      Inc(PSnake.Length);
    end;
    { Printing Part; Printing the board to the screen 
    for i:=2 to max_y-6 do
      for j:=2 to max_x do
        begin
          in this way it will clean itself no need for window clear
          if (PBoard[i][j] = 'S') then begin
            mvwprintw(game, j, i, 'O');
            PBoard[i][j] := 'E';
          end
          else if (PBoard[i][j] = 'E') then mvwprintw(game, j, i, ' ');
          //napms(20);
        end;}
    {  this cleans behind the Snake }
    { Displaying score }
      mvwprintw(game, PSnake.HeadPos[1], PSnake.HeadPos[2], ' ');
      mvwprintw(score, 1, 1, PChar(concat(IntToStr(scr), '                   '))); { the argument must be passed as a PChar }
      wrefresh(game);
      wrefresh(score);
  end;
  endwin();
END.
