Program draw_fill_ellipse;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode,
    errCode: Integer;
Begin
    Writeln('Initialising Graphics, please wait...');
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');
    If GraphResult <> grOK then exit;{ <> means 'not equal to' }

    Randomize;
    SetColor(Random(15) + 1); {Set paint color}

    fillEllipse(200, 200, 150, 50);

    ReadLn;
    CloseGraph;
End.