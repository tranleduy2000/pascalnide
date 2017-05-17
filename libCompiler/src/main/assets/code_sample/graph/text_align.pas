uses Graph;
var
    Gd, Gm : Integer;

begin
    Gd := Detect;
    InitGraph(Gd, Gm, '');
    if GraphResult <> grOk then Halt(1);

    SetTextJustify(CenterText, CenterText);
    OutTextXY(Succ(GetMaxX) div 2, Succ(GetMaxY) div 3,'CenterText');

    SetTextJustify(LeftText, CenterText);
    OutTextXY(0, Succ(GetMaxY) div 3,'LeftText');

    SetTextJustify(RightText, CenterText);
    OutTextXY(Succ(GetMaxX),Succ(GetMaxY) div 3,'RightText');
    ReadLn;
    CloseGraph;
end.
