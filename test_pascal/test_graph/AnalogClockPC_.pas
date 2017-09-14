program clock;
{ --------
  Analog clock demo for Pascal N-IDE
  by: Mr Bee -- @pak_lebah
}
uses
  CRT, Graph, SysUtils, DOS, Math;

type
    // screen orientation

  TOrientation = (oPortrait, oLandscape);

    // clock structure

  TPoint = record
    x, y : integer;
    // x,y coordinat
  end;

  TAngle = record
    a : float;
    // angle
    r : integer;
    // radius
  end;

var
  o : TOrientation;
  c : TPoint;
  a : TAngle;

// color selection shortcut
function color(r, g, b : byte) : integer;
//var
    //  cs: android_graphics_Color;
    //c: integer;
begin
  //c := cs.rgb(r,g,b);
  color := random(0); ;
end;

// angle (degree) point inside a circle
function point(c : TPoint; a : TAngle) : TPoint;
var
  r : TPoint;
begin
  r.x := round(c.x + a.r * sin(degToRad(a.a)));
  r.y := round(c.y + a.r * cos(degToRad(a.a)));
  point := r;
end;

// clock-wise point inside a circle
function hand(c : TPoint; a : TAngle) : TPoint;
var
  p : TPoint;
begin
  a.a := -a.a - 180; // adjust to screen
  p := point(c, a);
  hand := p;
end;

procedure drawText;
var
  t : string = 'Analog Clock';
  h, n, s, z, w : Word;
  j, m, d, e : string;
  yr, mo, dy, wd : Word;
begin
  setTextStyle(defaultFont, horizDir, 3);
  setTextJustify(leftText, topText);
  setColor(15);
  w := textWidth(t) div 2;
  if o = oPortrait then
    outTextXY(c.x - w, c.y - a.r - 50, t)
  else
    outTextXY(c.x - a.r - w * 2 - 50, c.y, t);
  // show digital time
  getTime(h, n, s, z);
  j := intToStr(h);
  if length(j) = 1 then j := '0' + j;
  m := intToStr(n);
  if length(m) = 1 then m := '0' + m;
  d := intToStr(s);
  if length(d) = 1 then d := '0' + d;
  e := intToStr(z);
  if length(e) = 1 then e := '0' + e;
  t := j + ':' + m + ':' + d + '.' + e;
  w := textWidth(t) div 2;
  if o = oPortrait then
    outTextXY(c.x - w, c.y + a.r + 80, t)
  else
    outTextXY(c.x + a.r + 50, c.y, t);
  getDate(yr, mo, dy, wd);
  // FORMAT DOES NOT WORK!!!
  //t := format('%-2.2d-%-2.2d-%-2.2d',[dy,mo,yr]);
  //outTextXY(c.x-w,c.y+a.r+140,t)
end;

procedure handHour(h, n : integer);
var
  p : TPoint;
  r : TAngle;
begin
  if h > 11 then h := h - 12;
  r := a;
  r.a := (h + n / 60) * 30;
  r.r := r.r div 2 - 12;
  p := hand(c, r);
  // hand
  setColor(lightBlue);
  setLineStyle(solidLn, 0, 24);
  setFillStyle(solidFill, lightBlue);
  line(c.x, c.y, p.x, p.y);
  // rounded tip
  setLineStyle(solidLn, 0, 1);
  //pieSlice(p.x,p.y,0,360,11);
end;

procedure handMinute(n, s : integer);
var
  p : TPoint;
  r : TAngle;
begin
  r := a;
  r.a := (n + (s / 60)) * 6;
  r.r := r.r - 50;
  p := hand(c, r);
  // hand
  setColor(lightGreen);
  setLineStyle(solidLn, 0, 12);
  setFillStyle(solidFill, lightGreen);
  line(c.x, c.y, p.x, p.y);
  // rounded tip
  setLineStyle(solidLn, 0, 1);
  //pieSlice(p.x,p.y,0,360,5);
end;

procedure handSecond(s, z : integer);
var
  p : TPoint;
  r : TAngle;
begin
  r := a;
  r.a := (s + (z / 100)) * 6;
  setColor(red);
  setFillStyle(solidFill, red);
  // hand
  r.r := a.r - 20;
  p := hand(c, r);
  setLineStyle(solidLn, 0, 4);
  line(c.x, c.y, p.x, p.y);
  // circle tip
  {r.r := a.r-30;
  p := hand(c,r);
  circle(p.x,p.y,10);}
  // calculate tail
  r.a := r.a - 180;
  r.r := 60;
  p := hand(c, r);
  // tail
  setLineStyle(solidLn, 0, 4);
  line(c.x, c.y, p.x, p.y);
  // center
  pieSlice(c.x, c.y, 0, 360, 20);
end;

procedure drawFace;
var
  i : integer;
  p : TPoint;
  r : TAngle;
begin
  // style
  setTextJustify(centerText, centerText);
  setTextStyle(defaultFont, horizDir, 3);
  setLineStyle(solidLn, 0, 2);
  setFillStyle(solidFill, color(24, 24, 24));
  setColor(7);
  r := a;
  // border
  //circle(c.x,c.y,a.r);
  pieSlice(c.x, c.y, 0, 360, a.r);
  // center
  //circle(c.x,c.y,10);
  // tick marks
  i := 0;
  r.r := r.r - 30;
  repeat
    i := i + 1;
    r.a := i * 6; // second degree
    p := hand(c, r);
      // hour ticks
    if i * 6 mod 30 = 0 then
      fillEllipse(p.x, p.y, 1, 1)
    else
      putPixel(p.x, p.y, 7);
  until i > 60;
  // hour marks
  i := 0;
  r.r := r.r - 30;
  repeat
    i := i + 1;
    r.a := i * 30; // hour degree
    p := hand(c, r);
    outTextXY(p.x, p.y, intToStr(i));
  until i > = 12;
end;

procedure getOrientation;
begin
  c.x := getMaxX div 2;
  c.y := getMaxY div 2;
  if c.x > c.y then o := oLandscape
  else o := oPortrait;
  if o = oLandscape then
    a.r := c.y - 10 else a.r := c.x - 10;
end;

procedure openScreen;
var
  gd, gm : integer;
begin
  gd := detect;
  //setBufferEnable(true);
  initGraph(gd, gm, '');
end;

procedure closeScreen;
begin
  closeGraph;
end;

var
  h, n, s, z : word;
begin
  openScreen;
  getOrientation;

  repeat
      //  clearBuffer;
    drawText;
    drawFace;
    getTime(h, n, s, z);

    handHour(h, n);
    handMinute(n, s);
    handSecond(s, z);
      // drawBuffer;
  until keyPressed;

  closeScreen;
end.
