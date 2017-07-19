Program ellipse;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode: integer;
Begin
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');

    Randomize;
    SetColor(Random(15) + 1); {Set paint color}


    {Draw bar}
    ellipse(200, 200, 0, 270, 200, 100);

    ReadLn;
    CloseGraph;
End.
