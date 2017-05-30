{Пример программы для процедуры Ellipse}

Uses Graph;

Var Gd, Gm : Integer;

Begin
 Gd:=Detect;
 InitGraph(Gd, Gm, '');
 If GraphResult <> grOk Then Halt(1);
 Ellipse(100, 100, 0, 360, 30, 50);
 Ellipse(100, 100, 0, 180, 50, 30);
 ReadLn;
 CloseGraph;
End.
