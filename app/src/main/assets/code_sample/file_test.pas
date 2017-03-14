program file_reader;
var
    f1, f2: text;
    s: string;
    i, tmp, n: integer;
    d: real;
    c: char;

begin
    Assign(f1, 'input.in');
    Reset(f1);

    Assign(f2, 'output.out');
    Rewrite(f2);

    readlnf(f1, s);
    writeln('read line string: ', s);
    writelnf(f2, s);

    ReadLnf(f1, i);
    writeln('read line integer: ', i);
    writelnf(f2, s);

    ReadLnf(f1, d);
    writeln('read line real: ', d);
    writelnf(f2, d);

    readlnf(f1, c);
    writeln('read char: ', c);
    writelnf(f2, c);

    readlnf(f1, n);
    for i := 1 to n do
    begin
        readf(f1, tmp);
        writeln(tmp);
        writef(f2, tmp, ' ');
    end;

    close(f1);
    Close(f2);
end.