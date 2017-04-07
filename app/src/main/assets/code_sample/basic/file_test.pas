{
1. Create new file with name: "input.in" and put it in the directory of application
    sdcard/PascalCompiler/
2. Write to file "input.in" some content:
    this is string
    1
    3.1415
    char
    4
    1 2 3 4
3. Save it
4. Run program
}
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

    {read string in file}
    readlnf(f1, s);
    writeln('read line string: ', s);
    writelnf(f2, s);

    {read integer value and store it to variable i}
    ReadLnf(f1, i);
    writeln('read line integer: ', i);
    writelnf(f2, s);

    {read real value and store it to variable d}
    ReadLnf(f1, d);
    writeln('read line real: ', d);
    writelnf(f2, d);

    {read char value and store it to variable c}
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
{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}