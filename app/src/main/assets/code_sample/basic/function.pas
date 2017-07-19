function get: integer;
begin
    randomize;
    WriteLn('Test function');
    get := random(1000);
end;

begin
    writeln(get);
    readln;
end.