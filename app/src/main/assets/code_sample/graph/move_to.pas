program moveToExample;
Uses Graph;
Var
    Gd, Gm : Integer;
Begin
    Gd := Detect;
    InitGraph(Gd, Gm, '');

    MoveTo(0, 0);
    LineTo(GetMaxX, GetMaxY);

    ReadLn;
    CloseGraph;
End.
