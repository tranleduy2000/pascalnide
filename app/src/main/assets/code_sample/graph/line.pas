Program lineNumber;
Uses Crt,Graph;
Var
    GraphicsDriver, GraphicsMode,
    ErrCode : Integer;
Begin
    Writeln('Initialising Graphics, please wait...');
    GraphicsDriver := Detect;
    InitGraph(GraphicsDriver, GraphicsMode,'');
    If GraphResult <> grOK then exit;{ <> means 'not equal to' }

    Randomize;
    SetColor(Random(15) + 1); {Set text colour}
    for i := 1 to getMaxY do
    begin
        lineNumber(10, i, 100, i);
    end;
    ReadLn;
    CloseGraph;
End.
