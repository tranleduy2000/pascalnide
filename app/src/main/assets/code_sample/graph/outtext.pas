Program outtext_sample;
Uses Crt,Graph;
Var
    GraphicsDriver, GraphicsMode,
    ErrCode : Integer;
        {two var's are needed for initialisation}
Begin
    Writeln('Initialising Graphics, please wait...');
    GraphicsDriver := Detect;
    InitGraph(GraphicsDriver, GraphicsMode,'');
    {IMPORTANT, read the following or
     otherwise graphics will not work!! ;)}
    ErrCode := GraphResult;
    If GraphResult <> grOK then exit;{ <> means 'not equal to' }

    Randomize;
    SetColor(Random(15) + 1); {Set text colour}
    {Output text at 20 pixels from the top of the screen,
     and 20 other from the left side of the screen.}
    OutTextXY(20,20,'Welcome to the new generation of Pascal Programming:');
    OutTextXY(20,30,'Pascal Graphics!!');
    OutTextXY(25,70,'You will learn more graphics procedures and');
    OutTextXY(25,80,'functions, later in this lesson :-)');

    Readln;
    CloseGraph;
End.
