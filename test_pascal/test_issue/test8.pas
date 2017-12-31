program Nguyento;
uses crt;

Var
  n, i, t, j : integer;
begin
  clrscr;
  Write('Nhap vao n: ');
  ReadLn(n);
  Writeln(n);
  Write('Cac so nguyen to nho hon n la: ');
  for i:=1 to n do
  begin
    t := 2;
    while  i mod t <> 0 do inc(t);
    if t = i then write(i, '  ');
  end;
  readln
end.
