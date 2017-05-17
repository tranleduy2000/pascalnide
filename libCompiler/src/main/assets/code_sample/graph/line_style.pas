Program line_style;
uses Graph;

var
    Gd, Gm: Integer;
    X1, Y1, X2, Y2: Integer;
begin
    Gd := Detect;
    InitGraph(Gd, Gm, ' ');
    if GraphResult <> grOk then
        Halt(1);

    SetColor(blue);

    //    draws a solid line.
    SetLineStyle(SolidLn, 0, ThickWidth);
    rectangle(10, 10, 300, 110);

    //    Draws a dotted line.
    SetLineStyle(DottedLn, 0, ThickWidth);
    rectangle(10, 120, 300, 220);

    //    draws a non-broken centered line.
    SetLineStyle(CenterLn, 0, ThickWidth);
    rectangle(10, 230, 300, 330);

    //    draws a dashed line.
    SetLineStyle(DashedLn, 0, ThickWidth);
    rectangle(10, 340, 300, 440);

    ReadLn;
    CloseGraph;
End.
