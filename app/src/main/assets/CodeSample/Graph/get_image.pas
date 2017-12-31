uses Graph;

var
  P : Pointer;
  Size : Word;

Var
  graphicsDriver, graphicsMode : integer;
Begin
  graphicsDriver := Detect;
  InitGraph(graphicsDriver, graphicsMode, '');

  SetFillStyle(XHatchFill, Cyan);
  Bar(0, 0, GetMaxX, GetMaxY);
  Size := ImageSize(10, 20, 30, 40);
  GetMem(P, Size);
  GetImage(10, 20, 30, 40, P^);
  ReadLn;
  ClearDevice;
  PutImage(100, 100, P^, NormalPut);
  ReadLn;
  CloseGraph;
end.
{http://pascal.net.ru/GetImage}