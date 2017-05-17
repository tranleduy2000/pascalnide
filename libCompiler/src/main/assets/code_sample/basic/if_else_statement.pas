program if_else;
var
    num1, num2: integer;
begin
    writeln('Enter first number: ');
    readln(num1);
    writeln('Enter second number: ');
    readln(num2);
    if (num1 < num2) then
        writeln(num1 ,' < ', num2)
    else if (num1 > num2) then
        writeln(num1 , ' > ', num2)
    else
        write(num1 , ' = ',  num2);

    readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}