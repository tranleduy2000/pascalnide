{This is a propram used to test "break" command
Used in loops, include:
    for..to...do..
    for..downto..do..
    while...do...
    repeat...until...
}
program test_break;
var
    i: Integer;
begin
    WriteLn('Test break in for..to..do statement: ');
    for i := 1 to 10 do
    begin
        Write(i, ' '); {write to console}
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
        inc(i);
        if i = 5 then
        begin
            WriteLn('Breaked');
            break;
        end;
    until i = 10;
    {pause screen}
    WriteLn('End of test');
    ReadLn();
end
.
