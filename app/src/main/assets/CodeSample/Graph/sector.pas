{Sample code for the Sector procedure.}
uses Graph;

const
  R = 50;
var
  Driver, Mode : Integer;
  Xasp, Yasp : Word;
begin
  Driver := Detect; { Put in graphics mode }
  InitGraph(Driver, Mode, ' ');

  Sector(GetMaxX div 2, GetMaxY div 2, 0, 234, R, R);

  Readln;
  CloseGraph;
end.
{http://putka.upm.si/langref/turboPascal/04D0.html}