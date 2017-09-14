program exGlobal;
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
  writeln(' Displaying the global variables a, b, and c');

  writeln('value of a = ', a, ' b =  ', b, ' and c = ', c);
  writeln('Displaying the local variables a, b, and c');

  writeln('value of a = ', a, ' b =  ', b, ' and c = ', c);
end;

begin
  a := 100;
  b := 200;
  c := 300;

  writeln('Winthin the program exlocal');
  writeln('value of a = ', a, ' b =  ', b, ' and c = ', c);

  display();
end.