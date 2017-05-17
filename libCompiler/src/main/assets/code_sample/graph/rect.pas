Program rect;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode,
    errCode, i: Integer;
Begin
    Writeln('Initialising Graphics, please wait...');
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');
    If GraphResult <> grOK then exit;{ <> means 'not equal to' }

    Randomize;
    SetColor(Random(15) + 1); {Set text color}

    rectangle(10, 10, 200, 200);

    ReadLn;
    CloseGraph;
End.
