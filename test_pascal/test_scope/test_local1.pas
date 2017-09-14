program test_local;
var
  a, b, c : integer;
procedure display;

var
  a, b, c : integer;
begin
  (* local variables *)
  a := 10;
  b := 20;
  c := a + b;

  writeln('Winthin the procedure display');
  writeln('value of a = ', a, ' b =  ', b, ' and c = ', c);
end;

begin
  a := 100;
  b := 200;
  c := a + b;

  writeln('Winthin the program exlocal');
  writeln('value of a = ', a, ' b =  ', b, ' and c = ', c);
  display();
end.