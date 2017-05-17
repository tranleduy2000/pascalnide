program fibonacci;

function fibonacci(n: integer) : integer;
var
    tmp: integer;

function calculate(i: integer): integer;
begin
    if (i = 1) or (i = 2) then
        calculate := 1
    else
        calculate := calculate(i - 1) + calculate(i - 2);
end;

begin
    fibonacci := calculate(n);
end;

begin
    writeln('fibonacci 4th = ', fibonacci(4));
    readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}