program score;
uses crt;

var
  chuoi : string;
  i, x, code, tong : integer;
  diem, st : string;
begin
  clrscr;
  write('nhap chuoi: ');
  readln(chuoi);
  if chuoi[1] = 'c' then diem := '1' else diem := '0';
  for i:=2 to length(chuoi) do
    if chuoi[i] = 'c' then
    begin
      val(diem[i - 1], x, code);
      x := x + 1;
      str(x, st);
      diem := diem + st;
    end
    else diem := diem + '0';
  writeln(diem);
  for i:=1 to length(diem) do
  begin
    val(diem[i], x, code);
    tong := tong + x;
  end;
  writeln(tong);
end.