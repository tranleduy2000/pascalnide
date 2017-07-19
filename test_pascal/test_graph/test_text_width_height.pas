uses Graph;

var
  Gd, Gm : Integer;
  Row : Integer;
  Title : String;
  Size : Integer;

begin
  Gd := Detect;
  InitGraph(Gd, Gm, 'X:\BP');
  if GraphResult <> grOk then
    Halt(1);
  Row := 0;
  Title := 'Turbo Graphics';
  Size := 1;
  while TextWidth(Title) < GetMaxX do
  begin
    OutTextXY(0, Row, Title);
    Inc(Row, TextHeight('M'));
    Inc(Size);
    SetTextStyle(DefaultFont, HorizDir, Size);
  end;
  ReadLn;
  CloseGraph;
end.

{http://pascal.net.ru/TextWidth}