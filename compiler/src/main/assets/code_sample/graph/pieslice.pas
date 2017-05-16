{Sample code for the PieSlice procedure.}
uses Graph;
const
    Radius = 30;
var
    Gd, Gm: Integer;
begin
    Gd := Detect;
    InitGraph(Gd, Gm, ' ');
    if GraphResult <> grOk then
        Halt(1);
    PieSlice(100, 100, 0, 270, Radius);
    Readln;
    CloseGraph;
end.
{http://putka.upm.si/langref/turboPascal/04B7.html}