program bounceBalls;

{ --------
  Bouncing balls demo for Pascal N-IDE
  by: Mr Bee -- @pak_lebah
}

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
  nBall = 9; // number of balls

var
  Balls : array[1..nBall] of TBall;

function randomColor : integer;
var
  cs : android_graphics_Color;
begin
  randomColor := cs.rgb(random(224) + 32,
                         random(224) + 32, random(224) + 32);
end;

procedure MoveBall(var aBall : TBall);
begin
  with aBall do
  begin
    // direction
    if gl then bx := bx - dx
    else bx := bx + dx;
    if gu then by := by - dy
    else by := by + dy;
    // bouncing
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

procedure DrawBall(aBall : TBall);
begin
  with aBall do
  begin
    SetColor(bc);
    SetFillStyle(SolidFill, bc);
    PieSlice(bx, by, 0, 360, br);
  end;
end;

procedure InitBalls(w, h : integer);
var
  i : integer;
begin
  for i := 1 to nBall do
    with Balls[i] do
    begin
      bc := randomColor; // color
      br := random(34) + 23; // radius
      bx := random(w - br) + br; // start x
      by := random(h - br) + br; // start y
      dx := random(13) + 1; // speed x
      dy := random(13) + 1; // speed y
      mx := w - br - 1; // max x
      my := h - br - 1; // max y
      nx := br + 1; // min x
      ny := br + 1; // min y
      gl := random(2) = 0; // move left
      gu := random(2) = 0; // move up
    end;
end;

var
  cs : android_graphics_Color;
  i, c : integer;
  mx, my : integer;
begin
  // enable double buffered
  setBufferEnable(true);
  InitGraph(detect, 0, '');
  mx := GetMaxX;
  my := GetMaxY;

  // create balls
  Randomize;
  InitBalls(mx, my);
  SetLineStyle(SolidLn, 0, ThickWidth);

  // balls animation
  while not keyPressed do
  begin
    // re-initiate if orientation change
    {if (GetMaxX <> mx) or (GetMaxY <> my) then
    begin
      mx := getMaxX;
      my := getMaxY;
      InitBalls(mx,my);
    end;}

    // draw background box
    SetColor(15);
    c := cs.argb(100, 1, 1, 1);
    setFillStyle(solidFill, c);
    Bar(0, 0, mx, my);
    // draw background text
    SetTextStyle(boldFont, horizDir, 9);
    SetTextJustify(centerText, centerText);
    outTextXY(mx div 2, my div 2, '⚽️ Hello 😊');

    // draw balls
    for i := 1 to nBall do
      DrawBall(Balls[i]);
    // move balls
    for i := 1 to nBall do
      MoveBall(Balls[i]);

    // redraw buffer to screen
    drawBuffer;
  end;
end.