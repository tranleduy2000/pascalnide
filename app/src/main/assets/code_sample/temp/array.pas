program moving_of_line;

uses crt, graph;

const
    coeff: array[1..2, 1..12] of integer =
    ((0, 1, 2, 2, 2, 1, 0, -1, -2, -2, -2, -1),(2, 2, 1, 0, -1, -2, -2, -2, -1, 0, 1, 2));
    delays: array[1..12] of byte = (50, 56, 56, 50, 56, 56, 50, 56, 56, 50, 56, 56);
    lm = 200; {максимальная длина змейки}
    k = 4; {радиус тела змейки}
    snake_color = 11; {цвет тела змейки}
    apple_color = 14; {цвет яблока}
    liter_color = 2; {цвет букв}

var
    i, x, y, d, m: integer;
    kl: char;
    j, n, sp: byte; {n - счетчик тел змейки}
    snake: array[1..2, 1..lm] of integer; {массив координат тел змейки}
    appl: boolean; {наличие яблока на поле}
    apple: array[1..2] of integer;
    points: word; {текущее число очков}
    spoints: string; {перевод числа очков в строку}

procedure nach;
forward;

procedure ending;
begin
    sound(400);
    delay(800);
    NoSound;
    setcolor(liter_color);
    OutTextXY(100, 10, 'Игра окончена! Число очков: ' + spoints);
    OutTextXY(100, 24, 'Сыграть ещё? (Y / N)');
{kl := readkey;}readln(kl);
    closegraph;
    if kl = 'Y' then nach
    else halt;
end;

procedure krug(a, b: integer; c: shortint); {c - цвет линий}
begin
    setcolor(c);
    circle(a, b, k);
end;

function color_pt: boolean; {для яблока}
var
    i, j: integer;
begin
    color_pt := true;
    for i := 1 to k + 1 do
        for j := 1 to k + 1 do
            if getpixel(apple[1] - 3 + i, apple[2] - 3 + j) <> 0 then
                color_pt := false;
end;

procedure scanner; {сканирование первого тела на собственный цвет}
var
    pascalPlugin: integer;
begin
    for pascalPlugin := -k + 2 to k - 2 do
    begin
        if getpixel(x + pascalPlugin, y) = snake_color then ending;
        if getpixel(x, y + pascalPlugin) = snake_color then ending;
    end;
    if (x <= 10 + k) or (x >= 630 - k) or {если голова вышла за пределы поля}
    (y <= 40 + k) or (y >= 470 - k) then ending;
end;

procedure point; {изменение кол-ва очков и вывод на экран}
begin
    inc(points, 5 * sp);
    sp := (points div 100) + 1;
    str(points, spoints);
    setcolor(red);
    rectangle(1, 1, 100, 35);
    setfillstyle(0, black);
    floodfill(2, 2, red);
    setcolor(black);
    rectangle(1, 1, 100, 35);
    setcolor(liter_color);
    OutTextXY(10, 10, 'Очки: ' + spoints);
end;

procedure appling; {помещение яблока на поле}
begin
    repeat
        apple[2] := 55 + random(400);
        apple[1] := 25 + random(590)
    until color_pt = true;
    krug(apple[1], apple[2], apple_color);
    appl := true;
end;

procedure moving;
begin
    while not keypressed do
    begin
        inc(j);
        if j = lm + 1 then j := 1;
        snake[1, j] := x;
        snake[2, j] := y;
        krug(x, y, snake_color);
        if j <= n then krug(snake[1, lm - n + j], snake[2, lm - n + j], 0)
                {удаление последнего тела}
        else krug(snake[1, j - n], snake[2, j - n], 0); {с целью имитации движения}

        delay(trunc(2 * delays[i] / sqrt(sp)));

        x := x + k * coeff[1, i];
        y := y + k * coeff[2, i];
        if appl = false then appling;
        if (abs(x - apple[1]) < 2 * k) and (abs(y - apple[2]) < 2 * k) then
        begin
            krug(apple[1], apple[2], 0);
            inc(n);
            point;
            appl := false;
        end;
        scanner;
    end;
    kl := readkey;
{клавиша q - примитивная пауза}
    if kl = 'q' then while not keypressed do delay(100)
    else case kl of
    'a': inc(i);
    'd': dec(i);
    end;
    if i = 13 then i := 1;
    if i = 0 then i := 12;
end;

procedure nach;
begin
    d := detect;
    initgraph(d, m, '');
    x := 320;
    y := 240;
    points := 0;
    sp := 1;
    appl := false;
    i := 1; {переменная направления (в часах поворота)}
    n := 2; {начальная длина змейки}
    setlinestyle(0, 0, ThickWidth);
    rectangle(10, 40, 630, 470);
    setlinestyle(0, 0, NormWidth);
    settextstyle(2, 0, 5);
    setcolor(liter_color);
    outtextxy(10, 10, 'Очки: 0');
    randomize;
    repeat moving until j = 5;
    repeat moving until false;
end;

begin
    nach;
end.