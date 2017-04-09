Program draw_bar;
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


    {Draw bar}
    ellipse(200, 200, 0, 180, 100, 50);

    ReadLn;
    CloseGraph;
End.
