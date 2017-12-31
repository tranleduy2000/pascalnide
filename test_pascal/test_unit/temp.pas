unit temp;

interface

{}
  procedure getabc(xa, ya, xb, yb, xc, yc : Real; var a, b, c : Real);

{}
  function exist(a, b, c : Real) : Boolean;

{}
  function perimeter(a, b, c : Real) : Real;

{}
  function square(a, b, c : Real) : Real;
{}

implementation

{}
function len(x1, y1, x2, y2 : Real) : Real;
begin
  len := Sqrt(Sqr(x1 - x2) + Sqr(y1 - y2));
end;

procedure getabc(xa, ya, xb, yb, xc, yc : Real; var a, b, c : Real);
begin
  a := len(xa, ya, xb, yb);
  b := len(xb, yb, xc, yc);
  c := len(xc, yc, xa, ya);
end;

function exist(a, b, c : Real) : Boolean;
begin
  exist := (a < b + c) and (b < a + c) and (c < a + b);
end;

function perimeter(a, b, c : Real) : Real;
begin
  perimeter := a + b + c;
end;

function square(a, b, c : Real) : Real;
var
  p : Real;
begin
  p := (a + b + c) / 2; {}
  square := Sqrt(p * (p - a) * (p - b) * (p - c));
end;

begin
end.