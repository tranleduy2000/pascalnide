function get(i: integer): integer ;
var
    res: integer;
begin
    writeln(i);
    delay(1);
    res := get(i + 1);
end;

begin
    writeln(get(1));
end.