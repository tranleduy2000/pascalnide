Program draw_arc;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode: integer;
Begin
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');

    Randomize;
    SetColor(Random(15) + 1); {Set paint color}

    {Draw arc}
    arc(200, 200, 0, 90, 100);

    ReadLn;
    CloseGraph;
End.
