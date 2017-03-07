program test_type;
type
    point = record
        x, y: integer;
    end;
var p: point;
begin
    p.x := 1;
    p.y := 2;
    writeln(p.x, ' ', p.y);
end.