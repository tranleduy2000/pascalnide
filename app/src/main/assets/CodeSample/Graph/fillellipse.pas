Program draw_fill_ellipse;
Uses Crt, Graph;

Var
  graphicsDriver, graphicsMode : integer;
Begin
  graphicsDriver := Detect;
  InitGraph(graphicsDriver, graphicsMode, '');

  Randomize;
  SetColor(Random(15) + 1); {Set paint color}

  fillEllipse(200, 200, 150, 50);

  ReadLn;
  CloseGraph;
End.