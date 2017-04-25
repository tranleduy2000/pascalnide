uses aVibrate;
var
    i: integer;
begin
    for i := 1 to 10 do
    begin
        writeln('vibrate 200 ms');
        vibrate(200);
        delay(500);
    end;

    readln;
    {Stop}
    cancelVibrate;
end.