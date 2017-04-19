program draw_pixel;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode,
    errCode, color, maxColor, startX, startY: Integer;
Begin
    Writeln('Initialising Graphics, please wait...');
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');
    If GraphResult <> grOK then exit;{ <> means 'not equal to' }

    startX := getMaxX();
    startY := getMaxY();
    maxColor := getMaxColor();

    randomize;
    While (not keypressed) do
    Begin
        delay(50);
        color := random(maxColor) + 1;
        putPixel(random(startX),random(startY), color);
    end;
    Closegraph;
End.