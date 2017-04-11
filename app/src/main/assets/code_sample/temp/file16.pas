Uses Graph, CRT;
var
    d, returnsValue, e, k, j, x, y: Integer;
begin
    d := Detect;
    InitGraph(d, returnsValue, ' ') ;
    e := GraphResult;
    if e <> grOk then Halt();

    x := GetMaxX div 6;
    y := GetMaxY div 5;
    for j := 0 to 2 do
        for k := 0 to 3 do
        begin
            Rectangle((k + 1) * x,(j + 1) * y,(k + 2) * x,(j + 2) * y);
            SetFillStyle(k + j * 4,j + 1);
            Bar((k + 1) * x + 1,(j + 1) * y + 1,(k + 2) * x - 1,(j + 2) * y - 1)
        end;

    readln;
    CloseGraph;
end.
{http://codingrus.ru/readarticle.php?article_id=2555}