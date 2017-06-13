var
  a, b : Integer;
  c, d : integer;
begin
  a := 1;
  b := 2;
  a += 1;
  writeln(a);
  a += b;
  WriteLn(a);
  a -= b;
  WriteLn(a);
  a *= b;
  WriteLn(a);
  c := a;
  writeln(c: 0 : 7);
  d := 1;
  d *= c;
  WriteLn(d: 0 : 7);
  d /= c;
  writeln(d: 0 : 7);

  d += c + a + b;
  WriteLn(d: 0 : 7);
  d /= a + d - c;
  WriteLn(d: 0 : 7);

end.
{
2
4
2
4
4.0000000
4.0000000
1.0000000
11.0000000
1.0000000
}