Uses Graph, CRT;
var
    d, r, e, k, j, x, y: Integer;
begin
{Инициируем графику}
    d := Detect;
    InitGraph(d, r, ' ') ;
    e := GraphResult;
    if e <> grOk then
        WriteLn(GraphErrorMsg(e))
       
    else
    begin

        x := GetMaxX div 6;{Положение графика}
        y := GetMaxY div 5;{на экране}
        for j := 0 to 2 do{Два ряда}
            for k := 0 to 3 do{По четыре квадрата}
            begin
                Rectangle((k + 1) * x,(j + 1) * y,(k + 2) * x,(j + 2) * y);
                SetFillStyle(BkSlashFill,blue);
                Bar((k + 1) * x + 1,(j + 1) * y + 1,(k + 2) * x - 1,(j + 2) * y - 1)
            end;
        if ReadKey = #0 then k := ord(ReadKey);
        CloseGraph
    end
end.
