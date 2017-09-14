program AreaCalculation;
uses CalculateArea;

var
  l, w, r, a, b, c, area : real;

begin
  l := 5.4;
  w := 4.7;
  area := RectangleArea(l, w);
  writeln('Area of Rectangle 5.4 x 4.7 is: ', area: 7 : 3);

  r := 7.0;
  area := CircleArea(r);
  writeln('Area of Circle with radius 7.0 is: ', area: 7 : 3);

  a := 3.0;
  b := 4.0;
  c := 5.0;

  area := TriangleArea(a, b, c);
  writeln('Area of Triangle 3.0 by 4.0 by 5.0 is: ', area: 7 : 3);
end.