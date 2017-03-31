PROGRAM  Vebautroi;
Uses Graph,Crt;
Var
    gd, gm, x, y: integer; maxcolor: word;
Begin
    Gd := Detect;
    Initgraph(gd,gm,'C:\TP70\BGI');
    If Graphresult <> Grok Then Halt(1);
    x := GetmaxX;
    y := GetmaxY;
    Maxcolor := Getmaxcolor;
    Randomize;
    While (not keypressed) do
    Begin
        delay(100);
        Putpixel(random(x),random(y),Random (maxcolor - 1) + 1);
    end;
    Closegraph;
End.