program moving_of_line;
uses graph, crt;

const
  lm = 200;
  k = 4;
  snake_color = 11;
  apple_color = 14;
  liter_color = 2;

coeff : array[1..2, 1..12] of -2..2 = ((0, 1, 2, 2, 2, 1, 0, -1, -2, -2, -2, -1), (2, 2, 1, 0, -1, -2, -2, -2, -1, 0, 1, 2));
delays : array[1..12] of byte = (50, 56, 56, 50, 56, 56, 50, 56, 56, 50, 56, 56);

var
  i, x, y, d, m : integer;
  kl : char;
  j, n, sp : byte;

  snake : array[1..2, 1..lm] of integer;

  appl : boolean;

  apple : array[1..2] of integer;
  points : word;

  spoints : string;

procedure nach; forward;

procedure ending;
begin
  sound(400);
  delay(800);
  NoSound;
  setcolor(liter_color);
  OutTextXY(100, 10, 'Игра окончена! Число очков: ' + spoints);
  OutTextXY(100, 24, 'Сыграть ещё? (Y / N)');

  closegraph;
  if kl = 'Y' then nach
  else halt;
end;

procedure krug(a, b : integer; c : shortint);
begin
  setcolor(c);
  circle(a, b, k);
end;

function color_pt : boolean;
var
  i, j : integer;
begin
  color_pt := true;
  for i := 1 to k + 1 do
    for j := 1 to k + 1 do
      if getpixel(apple[1] - 3 + i, apple[2] - 3 + j) <> 0 then
        color_pt := false;
end;

procedure scanner;
var
  t : integer;
begin
  for t := -k + 2 to k - 2 do
  begin
    if getpixel(x + t, y) = snake_color then ending;
    if getpixel(x, y + t) = snake_color then ending;
  end;

  if (x <= 10 + k) or (x >= 630 - k) or
  (y <= 40 + k) or (y >= 470 - k) then ending;
end;

procedure point;
begin
  inc(points, 5 * sp);
  sp := (points div 100) + 1;
  str(points, spoints);
  setcolor(red);
  rectangle(1, 1, 100, 35);
  setfillstyle(0, black);
  floodfill(2, 2, red);
  setcolor(black);
  rectangle(1, 1, 100, 35);
  setcolor(liter_color);
  OutTextXY(10, 10, 'Очки: ' + spoints);
end;

procedure appling;
begin
  repeat
    apple[2] := 55 + random(400);
    apple[1] := 25 + random(590)
  until color_pt = true;
  krug(apple[1], apple[2], apple_color);
  appl := true;
end;

procedure moving;
begin
  while not keypressed do
  begin
    inc(j);
    if j = lm + 1 then j := 1;
    snake[1, j] := x;
    snake[2, j] := y;
    krug(x, y, snake_color);
    if j <= n then krug(snake[1, lm - n + j], snake[2, lm - n + j], 0)
    else krug(snake[1, j - n], snake[2, j - n], 0);

    delay(trunc(2 * delays[i] / sqrt(sp)));
    x := x + k * coeff[1, i];
    y := y + k * coeff[2, i];
    if appl = false then appling;
    if (abs(x - apple[1]) < 2 * k) and (abs(y - apple[2]) < 2 * k) then
    begin
      krug(apple[1], apple[2], 0);
      inc(n);
      point;
      appl := false;
    end;
    scanner;
  end;
  kl := readkey;

  if kl = 'q' then while not keypressed do delay(100)
  else case kl of
         'a' : inc(i);
         'd' : dec(i);
  end;
  if i = 13 then i := 1;
  if i = 0 then i := 12;
end;

procedure nach;
begin
  d := detect;
  initgraph(d, m, '');
  x := 320;
  y := 240;
  points := 0;
  sp := 1;
  appl := false;
  i := 1;
  n := 2;
  setlinestyle(0, 0, ThickWidth);
  rectangle(10, 40, 630, 470);
  setlinestyle(0, 0, NormWidth);
  settextstyle(2, 0, 5);
  setcolor(liter_color);
  outtextxy(10, 10, 'Очки: 0');
  randomize;
  repeat moving until j = 5;
  repeat moving until false;
end;

begin
  nach;
end.