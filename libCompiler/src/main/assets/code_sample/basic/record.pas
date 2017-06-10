program length_of_line;
type
    point = record
        startX, startY: Real;
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
    Result := sqrt(Sqr(L.p1.startX + L.p2.startX) + Sqr(L.p1.startY + L.p2.startY));
    getLength := Result;
end;

begin
    mPoint1.startX := 1;
    mPoint1.startY := 2;
    mPoint2.startX := 3;
    mPoint2.startY := 5.2;

    mLine.p1 := mPoint1;
    mLine.p2 := mPoint2;

    WriteLn(getLength(mLine));

    readln;
end.
