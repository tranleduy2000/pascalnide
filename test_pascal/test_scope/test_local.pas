program test_local;
var
    a, b, c: integer;

begin
    (* actual initialization *)
    a := 10;
    b := 20;
    c := a + b;

    writeln('value of a = ', a , ' b =  ',  b, ' and c = ', c);
end.