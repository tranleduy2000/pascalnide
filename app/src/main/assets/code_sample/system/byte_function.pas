{This program will be test function "byte"
    function byte(<condition>)
    return 1 if <condition> is true, else return 0
}
program byte_function;
var
    b: Boolean;
begin
    b := false;
    WriteLn(b, ' ', byte(b));
    b := true;
    WriteLn(b, ' ', byte(b));
    {pause screen}
    readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}