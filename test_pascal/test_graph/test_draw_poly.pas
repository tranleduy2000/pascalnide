uses graph;

const
  Sao : array[1..10] of PointType =
    ((x : 439; y : 201), (x : 352; y : 196), (x : 320; y : 115), (x : 288; y : 196),
    (x : 201; y : 201), (x : 268; y : 257), (x : 247; y : 341), (x : 320; y : 295),
    (x : 393; y : 341), (x : 372; y : 257));
var
  gd, gm : integer;
begin
  gd := Detect;
  InitGraph(gd, gm, '');
  SetBkColor(LightRed);
  SetColor(Yellow);
  SetFillStyle(1, Yellow);
  FillPoly(10, Sao);
  readln;
  closegraph;
end.