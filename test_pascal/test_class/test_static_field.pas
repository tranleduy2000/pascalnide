{$mode objfpc}
type
   cl = class
      l : longint;
      static;
   end;
var
   cl1, cl2 : cl;
begin
   cl1 := cl.create;
   cl2 := cl.create;
   cl1.l := 2;
   writeln(cl2.l);
   cl2.l := 3;
   writeln(cl1.l);
   Writeln(cl.l);
end.