program animateGraph;

{ --------
  Text animation on graphics
  by: Mr Bee -- @pak_lebah
}

uses CRT, Graph;

const
   // global value setup
   THE_TEXT = 'PASCAL ON ANDROID';
   V_MOTION = 20;  // motion speed
   D_MOTION = 1;   // motion delay

type
   // letter structure
   TLetter = record
      l  : string;  // character
      x,y: integer; // position
      w,h: integer; // dimension
      c  : integer; // color
   end;
   TLetters = array of TLetter;

var
   goSpread: boolean; // motion direction
   goStraight: boolean; // animation mode
   doneX,doneY,done: boolean; // anim state

// generate random color
function randomColor: integer;
var
   cs: android_graphics_Color;
begin
   randomColor :=
                  cs.argb(255,random(192)+64,
                            random(192)+64,random(192)+64);
end;

// spread letters all over the screen
procedure spread(var l: TLetters);
var
   i: integer;
begin
   for i := 0 to high(l) do
   begin
      // random all over the screen
      l[i].x := random(getMaxX-l[i].w)+1;
      l[i].y := random(getMaxY-l[i].h)+1;
      l[i].c := randomColor;
   end;
end;

// gather all letters into a text
procedure gather(var l: TLetters);
var
   tw,th: integer; // text dimension
   tx,ty: integer; // text position
   i: integer;
   s: string;
begin
   // compute text dimension
   s := ''; tw := 0;
   for i := 0 to high(l) do s += l[i].l;
   for i := 0 to high(l) do tw += l[i].w;
   th := textHeight(s);
   tx := random(getMaxX-tw)+1;
   ty := random(getMaxY-th)+1;
   // align text onto new position
   tw := 0;
   for i := 0 to high(l) do
   begin
      l[i].x := tx + tw;
      l[i].y := ty;  // horz aligned
      l[i].c := randomColor;
      tw += l[i].w;
   end;
end;

// animate on x position
procedure animX(var f,t: TLetters);
var
   i,v: integer;
begin
   v := V_MOTION;
   doneX := true;
   for i := 0 to high(f) do
   begin
      // move left
      if f[i].x > t[i].x then
      begin
         f[i].x := f[i].x - v;
         if f[i].x < t[i].x then
            f[i].x := t[i].x;
      end
         // move right
      else if f[i].x < t[i].x then
      begin
         f[i].x := f[i].x + v;
         if f[i].x > t[i].x then
            f[i].x := t[i].x
      end
      else
         f[i].x := t[i].x;
      // save state
      doneX := doneX and (f[i].x = t[i].x);
   end;
end;

// animate on y position
procedure animY(var f,t: TLetters);
var
   i,v: integer;
begin
   v := V_MOTION;
   doneY := true;
   for i := 0 to high(f) do
   begin
      // move up
      if f[i].y > t[i].y then
      begin
         f[i].y := f[i].y - v;
         if f[i].y < t[i].y then
            f[i].y := t[i].y;
      end
         // move down
      else if f[i].y < t[i].y then
      begin
         f[i].y := f[i].y + v;
         if f[i].y > t[i].y then
            f[i].y := t[i].y
      end
      else
         f[i].y := t[i].y;
      // save state
      doneY := doneY and (f[i].y = t[i].y);
   end;
end;

// animate text
procedure animate(var f,t: TLetters);
begin
   if goStraight then
   begin
      if not doneX then animX(f,t);
      if not doneY then animY(f,t);
   end else
   begin
      if goSpread then
      begin
         // go vertical first
         if not doneY then
            animY(f,t)
         else begin
            if not doneX then
               animX(f,t);
         end;
      end else
      begin
         // go horizontal first
         if not doneX then
            animX(f,t)
         else begin
            if not doneY then
               animY(f,t);
         end;
      end;
   end;
   // animation state
   done := doneX and doneY;
end;

// print letters on the screen
procedure print(l: TLetters);
var
   i: integer;
begin
   for i := 0 to high(l) do
   begin
      setColor(l[i].c);
      outTextXY(l[i].x,l[i].y,l[i].l);
   end;
end;

// convert string to animated letters
function toLetters(s: string): TLetters;
var
   i,w: integer;
   l: TLetters;
begin
   w := 1;
   setLength(l,length(s));
   for i := 0 to length(s)-1 do
   begin
      l[i].l := s[i+1];
      l[i].w := textWidth(l[i].l)+3;
      l[i].h := textHeight(l[i].l)+2;
      l[i].x := w;
      l[i].y := l[i].h;
      l[i].c := randomColor;
      // space doesn't have width
      if l[i].l = ' ' then
         l[i].w := textWidth('H')+2;
      // i is too thin
      if upCase(l[i].l) = 'I' then
         l[i].w := l[i].w+2;
      w += l[i].w;
   end;
   toLetters := l;
end;

// remove text footprint
procedure clear(l: TLetters);
var
   cs: android_graphics_Color;
   i: integer;
begin
   clearBuffer;
   for i := 0 to high(l) do
   begin
      setColor(cs.rgb(32,32,32));
      outTextXY(l[i].x,l[i].y,l[i].l);
   end;
end;

// copy letter colors
procedure copyColor(var f,t: TLetters);
var
   i: integer;
begin
   for i := 0 to high(f) do
      t[i].c := f[i].c;
end;

// screen setup
procedure openScreen;
var
   gd,gm: integer;
begin
   randomize;
   // setup graph mode
   gd := detect;
   setBufferEnable(true);
   initGraph(gd,gm,'');
   setTextJustify(leftText,bottomText);
   setTextStyle(gothicFont,horizDir,3);
end;

// screen closing
procedure closeScreen;
begin
   closeGraph;
end;

{+++ MAIN PROGRAM +++}

var
   f,t: TLetters;
begin
   // setup
   openScreen;
   goSpread := true;
   goStraight := true;
   f := toLetters(THE_TEXT);
   t := toLetters(THE_TEXT);
   gather(f); // from (origin)
   spread(t); // to (target)
   
   // animate
   repeat
      // draw
      clear(t);
      print(f);
      drawBuffer;
      delay(D_MOTION);
      
      // check done
      if done then
      begin
         // switch destination
         copyColor(t,f);
         goSpread := not goSpread;
         if goSpread then spread(t)
         else gather(t);
         
         // freeze for 1 second
         delay(1000);
         // reset state
         doneX := false;
         doneY := false;
         done  := false;
         
         // switch mode
         if random(2) = 1 then
            goStraight := not goStraight;
      end;
      
      // motion
      animate(f,t);
   until keyPressed;
   
   // close down
   closeScreen;
end.