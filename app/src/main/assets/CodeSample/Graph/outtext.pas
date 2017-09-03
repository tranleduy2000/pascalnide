Program outtext_sample;
Uses Crt, Graph;

Var
  graphicsDriver, graphicsMode : integer;
Begin
  graphicsDriver := Detect;
  InitGraph(graphicsDriver, graphicsMode, '');

  Randomize;
  SetColor(Random(15) + 1); {Set text color}

  OutTextXY(20, 20, 'Welcome to the new generation of Pascal Programming:');
  OutTextXY(20, 30, 'Pascal Graphics!!');
  OutTextXY(25, 70, 'You will learn more graphics procedures and');
  OutTextXY(25, 80, 'functions, later in this lesson :-)');

  Readln;
  CloseGraph;
End.
