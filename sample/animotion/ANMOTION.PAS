program anmotion;
uses crt;

const
  bwseg = $b800;
  bwofs = $0000;
var
  i, j, k : integer;
begin
  gotoxy(2, 2);
  writeln('ransCo');
  for i:=0 to 8 do
  begin
    textbackground(i);
    clrscr;
    delay(50);
  end;
  for i := 1 to 34 do
  begin
    delay(20);
    gotoxy(i, 7);
    write('  anim');
  end;
  for i:=1 to 28 do
  begin
    gotoxy(68 - i, 7);
    write('otion ');
  end;
  for k:=0 to 8 do
  begin
    textbackground(k);
    for i := 12 to 18 do
    begin
      gotoxy(40, 23 - i);
      write(' ');
      gotoxy(40, i);
      write(' ');
      for j := 1 to 17 do
      begin
        gotoxy(40 + j, 23 - i);
        write(' ');
        gotoxy(40 - j, 23 - i);
        write(' ');
        gotoxy(40 + j, i);
        write(' ');
        gotoxy(40 - j, i);
        write(' ');
      end;
    end;
  end;
  gotoxy(36, 7);
  writeln('animotion');
  gotoxy(1, 1);
  writeln('ransCo');
  for i:=1 to 33 do
  begin
    gotoxy(i, 9);
    write(' M');
    delay(30);
  end;
  for i:=1 to 9 do
  begin
    gotoxy(35, i - 1);
    write(' ');
    gotoxy(35, i);
    write('i');
    delay(30);
  end;
  for i:=1 to 34 do
  begin
    gotoxy(70 - i + 1, 9);
    write(' ');
    gotoxy(70 - i, 9);
    write('k');
    delay(30);
  end;
  for i:=1 to 11 do
  begin
    gotoxy(37, 20 - i + 1);
    write(' ');
    gotoxy(37, 20 - i);
    write('e');
    delay(30);
  end;
  for i:=1 to 9 do
  begin
    gotoxy(5, i);
    write(' ');
    gotoxy(5, 1 + i);
    write('C');
    delay(30);
  end;
  for i:=1 to 34 do
  begin
    gotoxy(4 + i, 10);
    write(' ');
    gotoxy(5 + i, 10);
    write('C');
    delay(30);
  end;
  gotoxy(39, 10);
  write(' ');
  gotoxy(39, 9);
  write('C');
  for i:=1 to 9 do
  begin
    gotoxy(2, i);
    write(' ');
    gotoxy(2, 1 + i);
    write('a');
    delay(30);
  end;
  for i:=1 to 38 do
  begin
    gotoxy(1 + i, 10);
    write(' ');
    gotoxy(2 + i, 10);
    write('a');
    delay(30);
  end;
  gotoxy(40, 10);
  write(' ');
  gotoxy(40, 9);
  write('a');
  for i:=1 to 9 do
  begin
    gotoxy(1, i);
    write(' ');
    gotoxy(1, 1 + i);
    write('r');
    delay(30);
  end;
  for i:=1 to 40 do
  begin
    gotoxy(i, 10);
    write(' ');
    gotoxy(1 + i, 10);
    write('r');
    delay(30);
  end;
  gotoxy(41, 10);
  write(' ');
  gotoxy(41, 9);
  write('r');
  for i:=1 to 9 do
  begin
    gotoxy(4, i);
    write(' ');
    gotoxy(4, 1 + i);
    write('s');
    delay(30);
  end;
  for i:=1 to 38 do
  begin
    gotoxy(3 + i, 10);
    write(' ');
    gotoxy(4 + i, 10);
    write('s');
    delay(30);
  end;
  gotoxy(42, 10);
  write(' ');
  gotoxy(42, 9);
  write('s');
  for i:=1 to 9 do
  begin
    gotoxy(6, i);
    write(' ');
    gotoxy(6, 1 + i);
    write('o');
    delay(30);
  end;
  for i:=1 to 37 do
  begin
    gotoxy(5 + i, 10);
    write(' ');
    gotoxy(6 + i, 10);
    write('o');
    delay(30);
  end;
  gotoxy(43, 10);
  write(' ');
  gotoxy(43, 9);
  write('o');
  for i:=1 to 9 do
  begin
    gotoxy(3, i);
    write(' ');
    gotoxy(3, 1 + i);
    write('n');
    delay(30);
  end;
  for i:=1 to 41 do
  begin
    gotoxy(2 + i, 10);
    write(' ');
    gotoxy(3 + i, 10);
    write('n');
    delay(30);
  end;
  gotoxy(44, 10);
  write(' ');
  gotoxy(44, 9);
  write('n');
end.
{John does not use good structured programming!}