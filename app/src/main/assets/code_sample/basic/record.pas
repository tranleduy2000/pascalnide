program length_of_line;
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

    mLine.p1 := mPoint1;
    mLine.p2 := mPoint2;

    WriteLn(getLength(mLine));

    readln;
end.
