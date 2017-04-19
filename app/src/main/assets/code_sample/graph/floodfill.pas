uses graph;

var
    gd, gm: integer;

begin
    gd := Detect;
    InitGraph(gd,gm,'');

    if GraphResult <> grOk then Halt(1);

    setFillStyle(3,Blue);
    Bar3D(150,130,440,470,100,topon);

    FloodFill(300,125,Cyan);
    readln;
    closegraph;
end.
