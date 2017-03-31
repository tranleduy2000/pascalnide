PROGRAM VeDothi;

Uses

    Crt,Graph;

Var

    mh, mode: integer;
    chon: char;
    chugiai: string;

Procedure Chonham;

begin

    writeln('Các đồ thị có thể:');

    writeln('1---->Ðồ thị hình Sin(x)');

    writeln('2---->Ðồ thị hình Cos(x)');

    writeln('3---->Ðồ thị hình ArcTan(x)');

    write('Chọn đồ thị nào ?');
    readln(chon);

    Case chon of

    '1':  chugiai := 'ÐỒ THỊ HÀM SIN';

    '2': chugiai := 'ÐỒ THỊ HÀM COS';

    '3': chugiai := 'ÐỒ THỊ HÀM ARCTAN';

    end;

end;

Function   F(chon: char; x: real): real;

begin

    Case chon of

    '1':  F := Sin(x);

    '2':  F := Cos(x);

    '3':  F := Arctan(x);

    end;

end;

Procedure Dothi(a, b: real; x1, y1, x2, y2: integer; mn, md: integer);

var
    fx, k, h, r, c, d: real;

    x, y, n, m, i: integer;

begin

    c := f(chon,a);
    d := f(chon,a);

    r := a;
    h := (b - a) / 1000;

    while r <= b do

    begin

        fx := f(chon,r);

        if c > fx then c := fx;

        if d < fx then d := fx;

        r := r + h;

    end;

    Setcolor(md);
    Setbkcolor(mn);

    n := x2 - x1;

    h := (b - a) / n;

    m := y2 - y1;

    k := (d - c) / m;

    for i:=0 to n do

    begin

        x := x1 + i;

        fx := f(chon,a + i * h);

        y := round((fx - c) / k) + y1;

        y := y2 - y + y1;

        if i = 0 then moveto(x,y)

        else lineto(x,y);

    end;

end;

Begin
    (* Chương trình chính *)

    Clrscr;

    Chonham;

    mh := detect;

    Initgraph(mh,mode,'u:\bgi');

    Setviewport(GetmaxX DIV 2,GetmaxY DIV 2,GetmaxX,GetmaxY,ClipOff);

    Line(-(GetmaxX DIV 2),0,Getmaxfrraph in X DIV 2,0);

    Line(0,-(GetmaxY DIV 2),0,GetmaxY DIV 2);

    SetTextJustify(CenterText,CenterText);

    OutTextXY(-GetmaxX DIV 4,-GetmaxX DIV 4,chugiai);

    SetColor(Red);

    OutTextXY(GetmaxX DIV 2 - 32,2,'Truc x >');

    OutTextXY(27,-(GetmaxY DIV 2 - 5),'^ Truc y');

    OutTextXY(0,0,'0,0');

    Dothi(-4 * pi,4 * pi,-(getmaxx div 2) + 100,-(getmaxy div 2) + 100,getmaxx div 2 - 100,
        Getmaxy div 2 - 100,magenta,yellow);

    Readln;

    Closegraph;

End.