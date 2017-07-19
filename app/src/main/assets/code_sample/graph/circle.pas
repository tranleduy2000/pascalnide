program draw_circle;
uses graph;

Var
  graphicsDriver, graphicsMode : integer;
  Radius : Integer;
Begin
  graphicsDriver := Detect;
  InitGraph(graphicsDriver, graphicsMode, '');
  setlinestyle(0, 0, 3);

  for Radius:=1 to 23 do Circle(320, 240, Radius * 10);

  readln;
  ClearDevice; {clear screen}

  SetColor(LightMagenta); {set paint color}

  for Radius:=1 to 23 do Arc(320, 240, 0, 280, Radius * 10);

  readln;
  closegraph;

end.