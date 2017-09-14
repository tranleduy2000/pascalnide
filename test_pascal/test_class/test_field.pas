{$mode objfpc}
type
  cl = class
    l : longint;
  end;
var
  cl1, cl2 : cl;
begin
  cl1 := cl.create;
  cl2 := cl.create;
  cl1.l := 2;
  writeln(cl1.l);
  writeln(cl2.l);
end.