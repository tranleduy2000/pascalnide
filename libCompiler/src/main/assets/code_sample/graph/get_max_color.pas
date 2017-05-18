program draw_circle;
uses graph;
var
    gd, gm: integer;
    maxColor, color, i: integer;
begin
    gd := Detect;
    InitGraph(gd, gm, ' ');
    if GraphResult <> grOk then  Halt(1);

    maxColor := getMaxColor;

    for i := 0 to maxColor do
    begin
        color := random(maxColor) + 1;
        putPixel(random(startX),random(startY), color);
    end;

end.