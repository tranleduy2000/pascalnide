uses crt;
var
    i, j: Integer;
    s: string;
    r: real;
begin
    i := 1;
    j := 3;
    writeln(i / j : 3 : 2); {0.33}

    for i := 1 to 5 do write(i:3);
    writeln;
    for i := 1 to 5 do write(i:3);
    WriteLn;

    s := 'pascal';
    r := 12 / 11;
    writeln(s:7, i:3, r:5:3);
    readln;
end.