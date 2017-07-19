{This is a propram used to test "break" command
Used in loops, include:
    for..to...do..
    for..downto..do..
    while...do...
    repeat...until...
}
program break_command;
var
    i: Integer;
begin
    WriteLn('Test break in for..to..do statement: ');
    for i := 1 to 10 do
    begin
        Write(i, ' ');
        if i = 5 then
        begin
            WriteLn('Breaked');
            break;
        end;
    end;
    WriteLn();
    WriteLn('Test break in while..do statement: ');
    i := 1;
    while i < 10 do
    begin
        Write(i, ' ');
        i := i + 1;
        if i = 5 then
        begin
            WriteLn('Breaked');
            break;
        end;
    end;
    WriteLn();
    WriteLn('Test break in repeat...until statement: ');
    i := 1;
    repeat
        Write(i, ' ');
        if i = 5 then
        begin
            WriteLn('Breaked');
            break;
        end;
        inc(i);
    until i = 10;
    {pause screen}
    WriteLn('End of test');
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}
