type
  s = ^sss;

  sss = record
    x : string;
    c : s;
  end;
var
  v : sss;
begin
  v.x := '2';
  v.c^.x := '3';

  writeln(v.x);
  WriteLn(v.c^.x);
end.