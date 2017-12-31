program Ball;
uses  CRT, GRAPH;

    { Mini-Game | Мини-Игра }

VAR
  X, Y, F : INTEGER;
  G, S : CHAR;

procedure MoveLeft;
begin
  dec(x, 30);
  clearDevice;
  for F:=0 to 20 do
  begin
    circle(x, y, F);
    circle(x, y, F);
    circle(x, y, F);
  end;
  if x < 0 then x := getMaxX;
end;

procedure MoveRight;
begin
  inc(x, 30);
  clearDevice;
  for F:=0 to 20 do
  begin
    circle(x, y, F);
    circle(x, y, F);
    circle(x, y, F);
  end;
  if x > getMaxX then x := 0;
end;

procedure MoveUp;
begin
  dec(y, 30);
  clearDevice;
  for F:=0 to 20 do
  begin
    circle(x, y, F);
    circle(x, y, F);
    circle(x, y, F);
  end;
  if y < 0 then y := getMaxY;
end;

procedure MoveDown;
begin
  inc(y, 30);
  clearDevice;
  for F:=0 to 20 do
  begin
    circle(x, y, F);
    circle(x, y, F);
    circle(x, y, F);
  end;
  if y > getMaxY then y := 0;
end;

BEGIN
  randomize;
  if getMaxX > getMaxY then exit;
  writeln('Русский | English   (1 | 2)');
  s := readkey;
  if getMaxX > getMaxY then exit;
  if s = '2' then
  begin
    x := getMaxX div 2;
    y := getMaxY div 2;
    initGRAPH(detect, detect, '');
    CursorOFF;
    clrSCR;
    ClearDevice;
    setCOLOR(-6656);
    setTextStyle(0, 0, 5);
    outTextXY(125, 75, 'g');
    outTextXY(50, 177, 'c');
    outTextXY(125, 180, 'v');
    outTextXY(200, 165, 'b');
    setCOLOR(-16743292);
    outTextXY(125, 270, '+');
    setCOLOR(-1);
    for F:=0 to 20 do
    begin
      circle(x, y, F);
      circle(x, y, F);
      circle(x, y, F);
    end;
    setCOLOR(-16758009);
    While True Do
    begin
      if getMaxX > getMaxY then exit;
      g := ReadKEY;
      if getMaxX > getMaxY then exit;
      if g = 'g' then
      begin
        MoveUp;
        MoveUp;
      end else
        if g = 'v' then
        begin
          MoveDown;
          MoveDown;
        end else
          if g = 'c' then
          begin
            MoveLeft;
            MoveLeft;
          end else
            if g = '+' then setCOLOR(random(getMaxColor)) else
              if g = 'b' then
              begin
                MoveRight;
                MoveRight;
              end else write;
    end;
  end
  else if s = '1' then
  begin
    x := round(getMaxX / 2);
    y := round(getMaxY / 2);
    initGRAPH(detect, detect, '');
    CursorOFF;
    clrSCR;
    ClearDevice;
    setCOLOR(-6656);
    setTextStyle(0, 0, 5);
    outTextXY(125, 75, 'н');
    outTextXY(60, 150, 'п');
    outTextXY(125, 230, 'и');
    outTextXY(190, 151, 'о');
    setCOLOR(-16743292);
    outTextXY(125, 320, '+');
    setCOLOR(-1);
    for F:=0 to 20 do
    begin
      circle(x, y, F);
      circle(x, y, F);
      circle(x, y, F);
    end;
    setCOLOR(-16758009);
    While True Do
    begin
      if getMaxX > getMaxY then exit;
      g := ReadKEY;
      if getMaxX > getMaxY then exit;
      if g = 'н' then MoveUp else
        if g = 'и' then MoveDown else
          if g = 'п' then MoveLeft else
            if g = '+' then setCOLOR(random(getMaxColor)) else
              if g = 'о' then MoveRight else write;
    end;
  end else exit;
END.
