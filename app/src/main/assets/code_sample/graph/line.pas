Program draw_line;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode,
        i, startX, startY, maxColor, color: Integer;
Begin
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');

    Randomize;
    startX := getMaxX();
    startY := getMaxY();
    maxColor := getMaxColor();

    While (not keypressed) do
    Begin
        delay(50);
        color := random(maxColor) + 1;
        setColor(color);
        line(random(startX), random(startY), random(startX), random(startY));
    end;

    ReadLn;
    CloseGraph;
End.
