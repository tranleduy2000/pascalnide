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

    //    draws a solid mLineNumber.
    SetLineStyle(SolidLn, 0, ThickWidth);
    rectangle(10, 10, 300, 110);

    //    Draws a dotted mLineNumber.
    SetLineStyle(DottedLn, 0, ThickWidth);
    rectangle(10, 120, 300, 220);

    //    draws a non-broken centered mLineNumber.
    SetLineStyle(CenterLn, 0, ThickWidth);
    rectangle(10, 230, 300, 330);

    //    draws a dashed mLineNumber.
    SetLineStyle(DashedLn, 0, ThickWidth);
    rectangle(10, 340, 300, 440);

    ReadLn;
    CloseGraph;
End.
