program new4;
uses crt, graph;

type
  ff = text;

  bn = array[1..24] of string[32];

  point = object
    A, B : integer;
    procedure start;

    procedure clear (color : integer);

    procedure pt (v, d : integer);

  end;

  qa = object (point)
    procedure move(x, y, color : integer);

    procedure d;

  end;

procedure point.start;
begin
  a := 2;
  b := 2;
end;

procedure point.pt (v, d : integer);
begin
  putpixel (v, d, 15);
end;

procedure point.clear (color : integer);
begin
  setfillstyle (1, 0);
  floodfill (a + 10, b + 10, 15);
  setcolor(0);
  rectangle (a, b, a + 20, b + 20);
  setcolor(color);
  setfillstyle (1, 15);
end;

procedure qa.move (x, y, color : integer);
begin
  setcolor(3);
  setfillstyle (1, 0);
  rectangle (a, b, a + 20, b + 20);
  floodfill (a + 5, b + 5, 3);
  setcolor(0);
  rectangle (a, b, a + 20, b + 20);
  setcolor(color);
  a := a + x;
  b := b + y;
  if a > 602 then a := 602;
  if a < 2   then a := 2;
  if b > 462 then b := 462;
  if b < 0   then b := 0;
  rectangle (a, b, a + 20, b + 20);
end;

procedure qa.d;
begin
  setcolor (10);
  rectangle (2, 2, 22, 22);
end;

var
  gd, gm : integer;
  ch : char;
  li : qa;
  dx, dy, color : integer;
  log, log2, log3 : ff;
  mas : bn;
  ab, bd, cd, t : byte;
  sc : array[1..10] of integer;

function setA (xs, ys : integer) : boolean;
var
  fr : string;
  rf : char;
begin
  fr := mas[ys];
  if fr[xs] = '1' then setA := false
  else setA := true;
end;

function setB (xs, ys : integer) : boolean;
var
  fr : string;
  rf : char;
begin
  fr := mas[ys];
  if fr[xs] = '1' then setB := false
  else setB := true;
end;

function setC (xs, ys : integer) : boolean;
var
  fr : string;
  rf : char;
begin
  fr := mas[ys];
  if fr[xs] = '1' then setC := false
  else setC := true;
end;

function setD (xs, ys : integer) : boolean;
var
  fr : string;
  rf : char;
begin
  fr := mas[ys];
  if fr[xs] = '1' then setD := false
  else setD := true;
end;

procedure MASSIV (var m : bn; var l : ff);
var
  com : string[31];
  ij : integer;
begin
  ij := 0;
  reset(l);
  while not eoln (l) do
  begin
    readln (l, com);
    ij := ij + 1;
    m[ij] := com;
  end;
  close(l);
end;

procedure RIT (var m : bn);
var
  tts, ttr : integer;
  dfg : string;
begin
  for tts:=1 to 31 do
    for ttr:=1 to 23 do
    begin
      dfg := m[ttr];
      setfillstyle (1, t + 6);
      if dfg[tts] = '1' then rectangle (tts * 20, ttr * 20, tts * 20 - 16, ttr * 20 - 16);
      if dfg[tts] = '1' then floodfill (tts * 20 - 5, ttr * 20 - 5, white);
      delay(200);
    end;
end;

procedure RIG;
var
  h : integer;
begin
  setfillstyle (1, t + 6);
  for h:=0 to 22 do
  begin
    rectangle (624, 20 * h + 4, 639, 20 * h + 20);
    floodfill (625, 20 * h + 10, white);
    delay(3000);
  end;
  for h:=0 to 30 do
  begin
    rectangle (20 * h + 4, 464, 20 * h + 20, 479);
    floodfill (20 * h + 6, 470, white);
    delay(3000);
  end;
  rectangle (624, 464, 639, 479);
  floodfill (625, 465, white);
end;

procedure WINER (var ch : char; dx, dy : integer; var ab, bd, cd : byte);
begin
  if (dx = 31) and (dy = 3)  then
  begin
    ab := 1;
    color := 13;
  end;
  if (dx = 1) and (dy = 23) then
  begin
    bd := 1;
    color := 14;
  end;
  if (dx = 31) and (dy = 23) then
  begin
    cd := 1;
    color := 9;
  end;
  if ab + bd + cd = 3 then ch := #13;
end;

procedure OCH1;
begin
  setlinestyle (0, 0, 3);
  setcolor (14);
  line (6, 444, 18, 460);
  line (18, 444, 6, 460);
  setcolor (9);
  line (606, 444, 618, 460);
  line (618, 444, 606, 460);
  setcolor (13);
  line (606, 44, 618, 60);
  line (618, 44, 606, 60);
  setlinestyle (0, 0, 1);
end;

procedure OCH2 (sc : array of integer);
var
  qwe : boolean;
begin
  clrscr;
  qwe := true;
  gotoxy (10, 7);
  textcolor (red);
  if sc[0] <> 0 then writeln ('Level 1:  ', 1 / sc[0] * 10000: 0 : 0, ' Points')
  else begin
    qwe := false;
    writeln ('Level 1:  FAILED');
  end;
  gotoxy (10, 8);
  if sc[1] <> 0 then writeln ('Level 2:  ', 1 / sc[1] * 10000: 0 : 0, ' Points')
  else begin
    qwe := false;
    writeln ('Level 2:  FAILED');
  end;
  gotoxy (10, 9);
  if sc[2] <> 0 then writeln ('Level 3:  ', 1 / sc[2] * 10000: 0 : 0, ' Points')
  else begin
    qwe := false;
    writeln ('Level 3:  FAILED');
  end;
  if qwe = true then
  begin
    gotoxy (20, 15);
    textcolor (128 + 10);
    writeln ('!!!!! SUPER WINER !!!!!');
  end;
  readln;
end;

begin
  t := 1;
  repeat
    color := 10;
    dx := 1;
    dy := 1;

    if t = 1 then
    begin
      assign(log, 'data.txt');
      massiv (mas, log);
    end;

    if t = 2 then
    begin
      assign(log2, 'data2.txt');
      massiv (mas, log2);
    end;

    if t = 3 then
    begin
      assign(log3, 'data3.txt');
      massiv (mas, log3);
    end;

    ab := 0;
    bd := 0;
    cd := 0;
    t := t + 1;

    gd := detect;
    gm := 0;
    InitGraph(gd, gm, '');
    if graphresult = grOK then
    begin
      rit(mas);
      rig;
      li.start;
      li.d;
      och1;
      repeat
        ch := readkey;
        if ch = #0 then
        begin
          ch := readkey;
          case ch of
            #72 : if setA(dx, dy - 1) then
            begin
              li.move(0, -20, color);
              if dy > 1 then
              begin
                dy := dy - 1;
                sc[t - 1] := sc[t - 1] + 1;
              end;
            end;
            #80 : if setB(dx, dy + 1) then
            begin
              li.move(0, 20, color);
              if dy < 24 then
              begin
                dy := dy + 1;
                sc[t - 1] := sc[t - 1] + 1;
              end;
            end;
            #75 : if setC(dx - 1, dy) then
            begin
              li.move(-20, 0, color);
              if dx > 1 then
              begin
                dx := dx - 1;
                sc[t - 1] := sc[t - 1] + 1;
              end;
            end;
            #77 : if setD(dx + 1, dy) then
            begin
              li.move(20, 0, color);
              if dx < 31 then
              begin
                dx := dx + 1;
                sc[t - 1] := sc[t - 1] + 1;
              end;
            end;
          end;
        end;
        winer (ch, dx, dy, ab, bd, cd);
      until ch = #13;
    end;
    closegraph;
    textcolor(129 + t);
    gotoxy(36, 10);
    if ab + bd + cd = 3 then
    begin
      write ('!!!WIN!!!');
      readln;
    end
    else sc[t - 1] := 0;
  until t = 4;
  och2(sc);
  readln;
end.