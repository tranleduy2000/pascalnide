program bounce;

uses
  CRT, Graph;

type
  TBall = record
    bc, br : integer;
    bx, by : integer;
    dx, dy : integer;
    mx, my : integer;
    nx, ny : integer;
    gl, gu : boolean;
  end;

const
  nBall = 9;
var
  Balls : array[1..nBall] of TBall;

procedure MoveBall(var aBall : TBall);
begin
  with aBall do
  begin
    if gl then bx := bx - dx
    else bx := bx + dx;
    if gu then by := by - dy
    else by := by + dy;

    if bx < nx then
    begin
      bx := nx;
      gl := false;
    end;
    if bx > mx then
    begin
      bx := mx;
      gl := true;
    end;
    if by < ny then
    begin
      by := ny;
      gu := false;
    end;
    if by > my then
    begin
      by := my;
      gu := true;
    end;
  end;
end;

procedure DrawBall(aBall : TBall;
                      del : boolean);
var
  c : integer;
begin
  with aBall do
  begin
    if del then c := 0 else c := bc;
    SetFillStyle(SolidFill, c);
    SetColor(c);
    PieSlice(bx, by, 0, 360, br);
    //Circle(bx,by,br);
    //Bar(bx-br,by-br,bx+br,by+br);
  end;
end;

procedure InitBalls(w, h : integer);
var
  i : integer;
begin
  for i := 1 to nBall do
    with Balls[i] do
    begin
      bc := random(14) + 1;
      br := random(30) + 20;
      bx := random(w - br) + br;
      by := random(h - br) + br;
      dx := random(9) + 1;
      dy := random(9) + 1;
      mx := w - br - 1;
      my := h - br - 1;
      nx := br + 1;
      ny := br + 1;
      gl := false;
      gu := false;
    end;
end;

var
  i : integer;
  gd, gm : integer;
  mx, my : integer;
begin
  setBufferEnable(true); //default is false, this function in graph lib

  gd := detect;
  gm := 0;
  InitGraph(gd, gm, '');
  mx := GetMaxX;
  my := GetMaxY;

  Randomize;
  InitBalls(mx, my);
  SetLineStyle(SolidLn, 0, ThickWidth);

  while not KeyPressed do
  begin
    clearBuffer(); //clear buffer data, this function in graph lib

    for i := 1 to nBall do
      MoveBall(Balls[i]);
    for i := 1 to nBall do
      DrawBall(Balls[i], false);

    drawBuffer(); //draw buffer into screen, this function in crt library
  end;
end.