program xlc;
Uses crt;

Var
  chuoi : string;
  i, demso, demkt : integer;
  kt : char;
Begin
  Clrscr;
  write('Nhap vao mot chuoi: ');
  readln(chuoi);
  writeln('Chuoi vua nhap la: ', chuoi);
  While chuoi[1] = ' ' Do Delete(chuoi, 1, 1);
  While chuoi[Length(chuoi)] = ' ' Do
    Delete(chuoi, Length(chuoi), 1);
  While Pos('  ', chuoi) <> 0 do
    delete(chuoi, Pos('  ', chuoi), 1);
  writeln('Chuoi sau khi bo khoang trang thua: ', chuoi);
  writeln('Do dai cua chuoi vua nhap la: ', length(chuoi));
  chuoi := ' ' + chuoi;
  for i:=1 to length(chuoi) do
    if (chuoi[i] = ' ') then chuoi[i + 1] := upcase(chuoi[i + 1]);
  writeln('Chuan hoa ki tu dau: ', chuoi);
  for i:=1 to length(chuoi) do
    if chuoi[i] <> ' ' then chuoi[i] := upcase(chuoi[i]);
  writeln('Chuan hoa chuoi: ', chuoi);
  For i:=1 to length(chuoi) do
    If chuoi[i] IN ['A'..'Z'] Then chuoi[i] := CHR(ORD(chuoi[i]) + 32);
  Writeln('Chuan thuong chuoi: ', chuoi);
  For i:=1 to length(chuoi) do
    If chuoi[i] IN ['0'..'9'] Then demso := demso + 1;
  Writeln('So ki tu chu so trong chuoi: ', demso);
  write('Nhap vao ki tu de kiem tra: ');
  readln(kt);
  For i:=1 to length(chuoi) do
    if chuoi[i] = kt then demkt := demkt + 1;
  writeln('So lan ki tu ', kt, ' xuat hien trong chuoi la: ', demkt);
  Readln;
End.