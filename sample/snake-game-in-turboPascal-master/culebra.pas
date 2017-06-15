program snake;
uses crt;

var
   tamanoculebra : byte;
   posculebra : array[1..50, 1..2] of byte;
   poscomida : array[1..2] of byte;
   puntos : integer;
   hit, direction : char;
   jugando : boolean;

procedure crear_mapa;
var
   row, col : byte;
begin
   for row:=2 to 24 do
   begin
      if (row = 2) or (row = 24) then
      begin
         for col:=2 to 79 do
         begin
            textbackground(blue);
            gotoxy(col, row);
            write(' ');
         end;
      end else begin
         textbackground(blue);
         gotoxy(2, row);
         write(' ');
         gotoxy(79, row);
         write(' ');
      end;
   end;
end;

procedure puntos_es;
begin
   gotoxy(1, 1);
   textcolor(red);
   textbackground(black);
   write('Puntos: ');
   textcolor(white);
   write(puntos);
end;

function velocidad : word;
var
   speed : word;
begin
   if puntos < 100 then
      speed := 150
   else if puntos < 200 then
      speed := 100
   else
      speed := 50;
   velocidad := speed;
end;

procedure crea_culebra;
var
   tculebra : byte;
begin
   tamanoculebra := 6;
   for tculebra:=1 to tamanoculebra do
   begin
      posculebra[tculebra][1] := 39 + tculebra;
      posculebra[tculebra][2] := 12;
   end;
end;

procedure crea1_culebra;
var
   tculebra : byte;
begin
   for tculebra:=1 to tamanoculebra do
   begin
      if tculebra = 1 then
         textbackground(green)
      else
         textbackground(lightgray);
      gotoxy(posculebra[tculebra][1], posculebra[tculebra][2]);
      write(' ');
   end;
end;

function en_arreglo(x, y : byte; first : boolean) : boolean;
var
   helper : boolean;
   tculebra : byte;
begin
   if first then
      tculebra := 1
   else
      tculebra := 2;
   helper := false;
   while (tculebra <= tamanoculebra) and not helper do
   begin
      if (x = posculebra[tculebra][1]) and (y = posculebra[tculebra][2]) then
         helper := true;
      tculebra := tculebra + 1;
   end;
   en_arreglo := helper;
end;

procedure comida;
begin
   randomize;
   repeat
      poscomida[1] := random(76) + 3;
      poscomida[2] := random(21) + 3;
   until not en_arreglo(poscomida[1], poscomida[2], true);
   textbackground(black);
   textcolor(red);
   gotoxy(poscomida[1], poscomida[2]);
   write('*');
end;

procedure mov_culebra(dir : char);
var
   tculebra : byte;
   last : array[1..2] of byte;
begin
   textbackground(black);
   gotoxy(posculebra[tamanoculebra][1], posculebra[tamanoculebra][2]);
   write(' ');
   last[1] := posculebra[tamanoculebra][1];
   last[2] := posculebra[tamanoculebra][2];
   for tculebra:=tamanoculebra downto 2 do
   begin
      posculebra[tculebra][1] := posculebra[tculebra - 1][1];
      posculebra[tculebra][2] := posculebra[tculebra - 1][2];
   end;
   case dir of
      'l' : posculebra[1][1] := posculebra[1][1] - 1;
      'r' : posculebra[1][1] := posculebra[1][1] + 1;
      'u' : posculebra[1][2] := posculebra[1][2] - 1;
      'd' : posculebra[1][2] := posculebra[1][2] + 1;
   end;
   
   if (posculebra[1][1] = poscomida[1]) and (posculebra[1][2] = poscomida[2]) then
   begin
      puntos := puntos + 10;
      puntos_es;
      if tamanoculebra <= 50 then
      begin
         tamanoculebra := tamanoculebra + 1;
         posculebra[tamanoculebra][1] := last[1];
         posculebra[tamanoculebra][2] := last[2];
      end;
      comida;
   end;
   if (posculebra[1][1] = 2) or (posculebra[1][1] = 79) or (posculebra[1][2] = 2) or (posculebra[1][2] = 24) then
   begin
      jugando := false;
   end;
   if en_arreglo(posculebra[1][1], posculebra[1][2], false) then
   begin
      jugando := false;
   end;
end;

procedure juego;
begin
   jugando := true;
   textbackground(black);
   clrscr;
   crear_mapa;
   puntos := 0;
   puntos_es;
   crea_culebra;
   crea1_culebra;
   comida;
   
   direction := 'l';
   repeat
      if not jugando then
         hit := #27
      else begin
         if keypressed then
            hit := readkey;
         
         if (hit = #75) and (direction <> 'r') then
            direction := 'l'
         else if (hit = #77) and (direction <> 'l') then
            direction := 'r'
         else if (hit = #72) and (direction <> 'd') then
            direction := 'u'
         else if (hit = #80) and (direction <> 'u') then
            direction := 'd';
         
         mov_culebra(direction);
         crea1_culebra;
         delay(velocidad);
      end;
   until hit = #27;
   
   if not jugando then
   begin
      textbackground(black);
      clrscr;
      gotoxy(35, 11);
      textcolor(yellow);
      write('Has Perdido!');
      gotoxy(15, 16);
      textcolor(red);
      write('ESC: ');
      textcolor(white);
      write('Salir');
      gotoxy(51, 16);
      textcolor(red);
      write('ESPACIO: ');
      textcolor(white);
      write('Continuar');
      repeat
         hit := readkey;
         if hit = #32 then
            juego;
      until (hit = #27) or (hit = #32);
   end
end;


begin
   port[$3D4] := $A;
   port[$3D5] := 16;
   
   jugando := false;
   clrscr;
   
   textcolor(red);
   gotoxy(21, 7);
   write('   _____      _      _              ');
   gotoxy(21, 8);
   write('  / ____|    | |    | |              ');
   gotoxy(21, 9);
   write(' | |    _   _| | ___| |__  _ __ __ _ ');
   gotoxy(21, 10);
   write(' | |   | | | | |/ _ \  _ \|  __/ _` |');
   gotoxy(21, 11);
   write(' | |___| |_| | |  __/ |_) | | | (_| |');
   gotoxy(21, 12);
   write('  \_____\__,_|_|\___|_.__/|_|  \__,_|');
   write('                                     ');
   gotoxy(21, 13);
   write('Realizado por Alexander Rodriguez |', '@AlexR1712');
   gotoxy(21, 14);
   
   
   readkey;
   juego;
   
   textbackground(black);
   clrscr;
   gotoxy(28, 7);
   textcolor(white);
   write('Un juego sencillo basado en el famoso juego de SNAKE y recreado con ayuda de tutoriales y Pastebin');
   gotoxy(35, 11);
   textcolor(yellow);
   write('La Culebrita 1.0 por Alexander Rodriguez');
   gotoxy(27, 14);
   textcolor(green);
   write('MegaHosting Venezuela ');
   textcolor(red);
   write('Salir');
   readkey;
end.
