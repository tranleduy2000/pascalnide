program  pixel;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode,
    errCode, color, maxColor, x, y: Integer;
Begin
    Writeln('Initialising Graphics, please wait...');
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');
    If GraphResult <> grOK then exit;{ <> means 'not equal to' }

    x := getMaxX();
    y := getMaxY();
    maxColor := getMaxColor();

    randomize;
    While (not keypressed) do
    Begin
        delay(50);
        color := random(maxColor) + 1;
        putPixel(random(x),random(y), color);
    end;
    Closegraph;
End.