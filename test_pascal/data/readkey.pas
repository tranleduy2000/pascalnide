Program Example3;
uses Crt;

{ Program to demonstrate the ReadKey function. }

var
    ch : char;
begin
    writeln('Press Left/Right, Esc=Quit');
    repeat
        ch := ReadKey;
        writeln(#64);
    until ch = #27 {Esc}
end.
