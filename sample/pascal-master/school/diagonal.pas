program diagonal;
type
  mat = array [1..10, 1..10] of byte;
var
  m         : mat;
  d1, d2, n : byte;

procedure saisie(var n : byte; var m : mat);
var 
  i, j  : byte;
begin
  repeat
    write('N -> ');
    readln(n);
  until n in [2..10];
  randomize;
  for i:=1 to n do 
    for j:=1 to n do
      m[i, j] := random(10);
end;

procedure diago(var d1, d2 : byte; m : mat; n : byte);
var 
  i, j : byte;
begin
  d1 := 0;
  d2 := 0;
  for i:=1 to n do
    for j:=1 to n do 
      if j=i then d1 := d1 + m[i, j]
      else if j=n-i+1 then  d2 := d2+m[i, j];
end;

procedure affichage(m : mat; n : byte);
var 
  i, j : byte;
begin
  for i:=1 to n do begin
    for j:=1 to n do
      write(m[i, j] ,' ');
    writeln;
  end;
  
  for i:=1 to n do begin
    for j:=1 to n do begin
      if (j=i) or (j=n-i+1) then write(m[i, j]);
      write(' ');
    end;
    writeln;
  end;
end;

BEGIN
  saisie(n, m);
  affichage(m, n);
  diago(d1, d2, m, n);
  writeln('Diagonal 1 -> ', d1);
  writeln('Diagonal 2 -> ', d2);
END.
