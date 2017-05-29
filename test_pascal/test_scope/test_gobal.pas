program exGlobal;
var
    a, b, c: integer;
procedure display;
var
    x, y, z: integer;

begin
    (* local variables *)
    x := 10;
    y := 20;
    z := x + y;

    (*global variables *)
    a := 30;
    b := 40;
    c := a + b;

    writeln('Winthin the procedure display');
    writeln(' Displaying the global variables a, b, and c');

    writeln('value of a = ', a , ' b =  ',  b, ' and c = ', c);
    writeln('Displaying the local variables x, y, and z');

    writeln('value of x = ', x , ' y =  ',  y, ' and z = ', z);
end;

begin
    a := 100;
    b := 200;
    c := 300;

    writeln('Winthin the program exlocal');
    writeln('value of a = ', a , ' b =  ',  b, ' and c = ', c);

    display();
end.