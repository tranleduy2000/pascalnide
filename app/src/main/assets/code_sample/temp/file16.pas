Uses Graph, CRT;
var
    d, returnsValue, e, k, j, startX, startY: Integer;
begin
    d := Detect;
    InitGraph(d, returnsValue, ' ') ;
    e := GraphResult;
    if e <> grOk then Halt();

    startX := GetMaxX div 6;
    startY := GetMaxY div 5;
    for j := 0 to 2 do
        for k := 0 to 3 do
        begin
            Rectangle((k + 1) * startX,(j + 1) * startY,(k + 2) * startX,(j + 2) * startY);
            SetFillStyle(k + j * 4,j + 1);
            Bar((k + 1) * startX + 1,(j + 1) * startY + 1,(k + 2) * startX - 1,(j + 2) * startY - 1)
        end;

    readln;
    CloseGraph;
end.
{http://codingrus.ru/readarticle.php?article_id=2555}