program string_test;
var
    name: string;
    tmp: char;
    i: Integer;
begin
    {print to console}
    writeln('enter your full name (lower case): ');

    {Input data into variable name}
    readln(name);

    {print upper case}
    for i := 1 to length(name) do
    begin
        tmp := upcase(name[i]);
        Write(tmp);
    end;

    writeln;
    {tran le duy -> Tran Le Duy}
    name := ' ' + name;
    for i := 1 to Length(name) do
    begin
        if name[i] = ' ' then name[i + 1] := upcase(name[i + 1]);
    end;

    {print to console}
    writeln(name);
    readln;
end.
{Input: tran le duy
Output:
    TRAN LE DUY
    Tran Le Duy}
{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}