uses graph;

Var
  graphicsDriver, graphicsMode : integer;
Begin
  graphicsDriver := Detect;
  InitGraph(graphicsDriver, graphicsMode, '');

  setFillStyle(3, Blue);
  Bar3D(150, 130, 440, 470, 100, topon);

  FloodFill(300, 125, Cyan);
  readln;
  closegraph;
end.
