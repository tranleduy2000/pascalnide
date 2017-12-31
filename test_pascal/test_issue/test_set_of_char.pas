const
  gis = ['a', 'b', 'c'];
type
  buk = set of char;
var
  i, j, t : Byte;
  stroka : string;
  twogls : buk;
begin
  WriteLn('');
  ReadLn(stroka);
  t := 1;
  twogls := [];
  twogls := twogls + ['1', '2'];
  WriteLn(twogls);
  twogls := [];
  WriteLn(twogls);
end.