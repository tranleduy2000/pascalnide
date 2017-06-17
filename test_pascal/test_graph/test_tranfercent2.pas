uses graph;
var
   gd, gm : integer;
begin
   gd := Detect;
   InitGraph(gd, gm, '');
   
   SetBkColor(LightRed);
   SetColor(Yellow);
   
   SetFillStyle(1, 1401028352);
   bar(0, 0, getMaxX div 3 * 2, getMaxY);
   SetFillStyle(1, -1540161281);
   bar(getMaxX div 3 , 0, getMaxX , getMaxY);
   readln;
   closegraph;
end.