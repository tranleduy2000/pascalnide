type
    egde = record
        x, y: LongInt;
    end;
var
    a: array[0..10] of integer;
    b: array[0..10] of LongInt;
    c: array[0..10] of Real;
    d: array[2..10, 3..10] of Integer;
    e: array[1..100] of egde;
    f: array[0..10, 1..10] of egde;
    i: Integer;
    j: Integer;
begin
    for i := 1 to 10 do
    begin
        a[i] := i * 10;
        Write(a[i], ' ');
    end;
    writeln;
    for i := 1 to 10 do
    begin
        b[i] := 100000 * i;
        WriteLn(b[i], ' ');
    end;
    writeln;
    for i := 1 to 10 do
    begin
        c[i] := i / 10.5;
        WriteLn(c[i] : 2 : 3);
    end;
    writeln;
    for i := 2 to 10 do
    begin
        for j := 3 to 10 do
        begin
            a[i] := i * j;
            Write(a[i], ' ');
        end;
        writeln;
    end;
    for i := 1 to 10 do
    begin
        e[i].x := i;
        e[i].y := i * i;
        writeln('(x, y) ', e[i].x, ' ', e[i].y);
    end;
    for i := 1 to 10 do
    begin
        for j := 1 to 10 do
        begin
            f[i, j].x := i;
            f[i, j].y := j;
            writeln('(x, y) ', f[i, j].x, ' ', f[i,j].y);
        end;
        writeln;
    end;
    readln;
end.