Program draw_line;
Uses Crt,Graph;
Var
    graphicsDriver, graphicsMode,
    errCode, i, startX, startY, maxColor,
    color: Integer;
Begin
    Writeln('Initialising Graphics, please wait...');
    graphicsDriver := Detect;
    InitGraph(graphicsDriver, graphicsMode,'');
    If GraphResult <> grOK then exit;

    Randomize;
    startX := getMaxX();
    startY := getMaxY();
    maxColor := getMaxColor();

    While (not keypressed) do
    Begin
        delay(50);
        color := random(maxColor) + 1;
        setColor(color);
        mLineNumber(random(startX), random(startY), random(startX), random(startY));
    end;

    ReadLn;
    CloseGraph;
End.
