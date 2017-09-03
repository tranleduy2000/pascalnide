Program draw_bar;
Uses Crt, Graph;

Var
  graphicsDriver, graphicsMode : integer;
Begin
  graphicsDriver := Detect;
  InitGraph(graphicsDriver, graphicsMode, '');
  Randomize;
  SetColor(Random(15) + 1); {Set paint color}


  {Draw bar}
  bar(100, 100, 300, 200);

  ReadLn;
  CloseGraph;
End.
