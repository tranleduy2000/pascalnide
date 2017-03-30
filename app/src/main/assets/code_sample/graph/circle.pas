uses graph;

var
    gd, gm: integer; Radius: integer;

begin
    gd := Detect;
    InitGraph(gd, gm, ' ');
    if GraphResult <> grOk then  Halt(1);
    setlinestyle(0,0,3);

    for Radius:=1 to 23 do Circle(320,240,Radius * 10);

    readln;
    ClearDevice;
    SetColor(LightMagenta);

    for Radius:=1 to 23 do Arc(320,240,0,280,Radius * 10);

    readln;
    closegraph;

end.