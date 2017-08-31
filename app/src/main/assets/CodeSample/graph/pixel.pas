program draw_pixel;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode,
     color, maxColor, startX, startY: Integer;
Begin
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');

    startX := getMaxX;
    startY := getMaxY;
    maxColor := getMaxColor;

    randomize;
    While (not keypressed) do
    Begin
        color := random(maxColor) + 1;
        putPixel(random(startX),random(startY), color);
    end;
    Closegraph;
End.