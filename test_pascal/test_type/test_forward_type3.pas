type
  s = ^sss;

  sss = record
    x : string;
    c : s;
  end;
var
  v : s;
  v2 : s;
begin
  New(v);
  New(v2);
  v2^.x := '3';
  v^.x := '2';
  v^.c := v2;

  writeln(v^.x);
  writeln(v^.c^.x);
end.
