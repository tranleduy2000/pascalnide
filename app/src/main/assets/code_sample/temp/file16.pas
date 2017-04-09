Program program16;
{Author: Rao S Lakkaraju}
{* Basic Graphics Program *}

uses crt,graph;
var
    grmode, grdriver : integer;
    x, y : integer;

Procedure graphicpics;
Begin
{Now give instructions to do graphics}

    randomize;
    settextstyle(triplexfont,horizdir,1);
    for x := 1 to 200 do begin
        putpixel(49 + x,500,(random(15)));
    end;

    setcolor(Lightgreen);

    settextstyle(triplexfont,horizdir,3);
    OutTextXY(50,30,'Graphics With Pascal');
    settextstyle(triplexfont,horizdir,1);
    OutTextXY(50,300,'Screen Coordinates');
    OutTextXY(50,350,'Top left corner (x,y): 0,0');
    OutTextXY(50,400,'Bottom right corner (x,y): 1023,711 Pixels');
    writeln('max x = ', getmaxx,' max y = ', getmaxy);

    setfillstyle(1,red);
    bar(100,100,250,250);
    setfillstyle(2,green);
    bar(0,0,23,19);
    bar(1000,690,1023,711);

    setcolor(red);
{line(x1,y1,x2,y2)}
    line(0,0,0,23);
    line(0,0,23,0);
    for x := 1000 to 1023 do putpixel(x,711,red);
    for y := 690 to 711 do putpixel(1023,y,red);
    for x := 800 to 850 do putpixel(x,500,red);
    for y := 450 to 500 do putpixel(850,y,red);
    setcolor(cyan);
{rectangle(x1,y1,x2,y2);}
    Rectangle(50,150,80,250);

{circle(x1,y1,radius); x1,y1 center of the circle}
    circle(80,180,50);

{ellipse(x1,y1,degreesfrom,degrees to,hr,vr)}
    ellipse(150,200,0,360,30,40);

{arc(x,y,p,q,radius)}
    arc(180,250,0,90,60);

{pieslice(x,y,p,q,radius)}
    pieslice(150,150,20,95,100);
End;


begin
{First we have to initialize the graphics mode}

    clrscr;
    grdriver := DETECT;
    initgraph(grdriver,grmode, ' ' );

    graphicpics;

    Writeln('**** My Sixteenth Pascal Program ****');
    readln;
    CloseGraph;
end.
