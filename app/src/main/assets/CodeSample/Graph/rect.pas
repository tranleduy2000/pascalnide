Program rect;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode, i: Integer;
Begin
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');

    Randomize;
    SetColor(Random(15) + 1); {Set text color}

    rectangle(10, 10, 200, 200);

    ReadLn;
    CloseGraph;
End.
