
function div_par_11(a : LongInt):Boolean;
var
  x : Integer;
begin
  repeat
    x := a mod 10;
    a := (a div 10) - x; 
  until (a<=0);
  div_par_11 := (a = 0); 
end;



BEGIN
  Writeln(div_par_11(12345));
END.
