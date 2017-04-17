program test_record_4;
type
    point = record
        x, y: Real;
    end;

    line = record
        p1: point;
        p2: point;
    end;
var
    mPoint1, mPoint2: point;
    mLine: line;

{Get length of line}
function getLength(L: line): real;
var
    result : real;
begin
    Result := sqrt(Sqr(L.p1.x + L.p2.x) + Sqr(L.p1.y + L.p2.y));
    getLength := Result;
end;

begin
    mPoint1.x := 1;
    mPoint1.y := 2;
    mPoint2.x := 3;
    mPoint2.y := 5.2;

    line.p1 = mPoint1;
    line.p2 = mPoint2;

    WriteLn(getLength(line));

    readln;
end.
