Program Graphika12;
Uses Graph;

Var
  graphicsDriver, graphicsMode : integer;
Begin
  graphicsDriver := Detect;
  InitGraph(graphicsDriver, graphicsMode, '');
  setcolor (5);
  setlinestyle(2, 0, 3);
  setfillstyle (6, 7);
  bar3d (100, 100, 300, 500, 50, topoff);
  readln;
  closegraph;
end.


