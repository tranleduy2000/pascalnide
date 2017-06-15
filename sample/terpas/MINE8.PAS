program digger;
uses crt;

label metka1, l1, l2, m4, m5;
const
   x = 72;
   y = 24;
var
   m : array [1..x, 1..y] of char;
   i, j, vert, hor, zx, zy, dist, bl : byte;
   l, h, c, g, b, phon, h1, gr, d : char;
   fil : text;
   S : string[x];
   k, health, ammo, d1, cont, detonate : integer;
   n : boolean;
begin
   textbackground(8);
   textcolor(7);
   clrscr;
   assign(fil, 'ground1.txt');
   reset(fil);
   b := '-';
   d := chr(9);
   write('Put health,ammo>>');
   readln(health, ammo);
   randomize;
   metka1 : for j:=1 to y do
begin
   READLN(fil, S);
   for i:=1 to x do
   begin
      m[i, j] := S[i];
      write(m[i, j]);
   end;
   writeln;
end;
   l := chr(2);
   c := chr(1);
   g := chr(178);
   phon := chr(177);
   gr := chr(176);
   hor := 20;
   vert := 8;
   m[hor, vert] := l;
   h1 := 's';
   h := 's';
   while 2 > 1    do
   begin
      h := h1;
      if keypressed then h := readkey;
      case h of
         'a' :
         begin
            if (m[hor - 1, vert] = ' ') or (m[hor - 1, vert] = phon) then
            begin
               m[hor, vert] := ' ';
               if m[hor - 1, vert] = phon then m[hor, vert] := phon;
               hor := hor - 1;
               m[hor, vert] := l;
            end;
         end;
         'd' :
         begin
            if (m[hor + 1, vert] = ' ') or (m[hor + 1, vert] = phon) then
            begin
               m[hor, vert] := ' ';
               if m[hor + 1, vert] = phon then m[hor, vert] := phon;
               hor := hor + 1;
               m[hor, vert] := l;
            end;
         end;
         'q' :
         begin
            if ((m[hor, vert - 1] = ' ') or (m[hor, vert - 1] = phon)) and
            ((m[hor - 1, vert - 1] = ' ') or (m[hor - 1, vert - 1] = phon)) then if m[hor, vert - 1] = phon then
            begin
               m[hor, vert] := phon;
               vert := vert - 1;
               m[hor, vert] := l;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textcolor(7);
                        chr(9) : textbackground(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
               m[hor, vert] := phon;
               hor := hor - 1;
               m[hor, vert] := l;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textcolor(7);
                        chr(9) : textbackground(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
            end
            else begin
               m[hor, vert] := ' ';
               vert := vert - 1;
               m[hor, vert] := l;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textcolor(7);
                        chr(9) : textbackground(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
               m[hor, vert] := ' ';
               hor := hor - 1;
               m[hor, vert] := l;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textcolor(7);
                        chr(9) : textbackground(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
            end;
         end;
         'e' :
         begin
            if ((m[hor, vert - 1] = ' ') or (m[hor, vert - 1] = phon)) and
            ((m[hor + 1, vert - 1] = ' ') or (m[hor + 1, vert - 1] = phon)) then if m[hor, vert - 1] = phon then
            begin
               m[hor, vert] := phon;
               vert := vert - 1;
               m[hor, vert] := l;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textcolor(7);
                        chr(9) : textbackground(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
               m[hor, vert] := phon;
               hor := hor + 1;
               m[hor, vert] := l;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textcolor(7);
                        chr(9) : textbackground(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
            end
            else begin
               m[hor, vert] := ' ';
               vert := vert - 1;
               m[hor, vert] := l;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textcolor(7);
                        chr(9) : textbackground(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
               m[hor, vert] := ' ';
               hor := hor + 1;
               m[hor, vert] := l;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textbackground(7);
                        chr(9) : textcolor(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
            end;
         end;
         'w' : if (m[hor, vert - 1] = ' ') or (m[hor, vert - 1] = phon) then
         begin
            m[hor, vert] := g;
            vert := vert - 1;
            m[hor, vert] := l;
         end;
         'g' : if (m[hor, vert + 1] <> gr) and (m[hor, vert + 1] <> ' ') then m[hor, vert + 1] := phon;
         't' : if (m[hor, vert - 1] <> gr) and (m[hor, vert - 1] <> ' ') then m[hor, vert - 1] := phon;
         'f' : if (m[hor - 1, vert] <> gr) and (m[hor - 1, vert] <> ' ') then m[hor - 1, vert] := phon;
         'h' : if (m[hor + 1, vert] <> gr) and (m[hor + 1, vert] <> ' ') then m[hor + 1, vert] := phon;
         'i' : if (m[hor, vert - 1] <> c) then m[hor, vert - 1] := g;
         'k' : if (m[hor, vert + 1] <> c) then m[hor, vert + 1] := g;
         'j' : if (m[hor - 1, vert] <> c) then m[hor - 1, vert] := g;
         'l' : if (m[hor + 1, vert] <> c) then m[hor + 1, vert] := g;
         'z' : if ammo > 0 then
         begin
            ammo := ammo - 1;
            for dist:=1 to 15 do
            begin
               m[hor - dist, vert] := b;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textcolor(7);
                        chr(9) : textbackground(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
               if (m[hor - dist - 1, vert] = d) or (m[hor - dist - 1, vert] = c) then
               begin
                  m[hor - dist - 1, vert] := phon;
                  for j:=1 to 2 do
                  begin
                     if m[hor - dist - 1 + j, vert] <> ' '
                     then if m[hor - dist - 1 + j, vert] = d then
                     begin
                        m[hor - dist - 1 + j, vert] := phon;
                        dist := dist - j;
                     end
                     else m[hor - dist - 1 + j, vert] := phon;
                     if m[hor - dist - 1 - j, vert] <> ' '
                     then m[hor - dist - 1 - j, vert] := phon;
                     if m[hor - dist - 1, vert + j] <> ' '
                     then m[hor - dist - 1, vert + j] := phon;
                     if m[hor - dist - 1, vert - j] <> ' '
                     then m[hor - dist - 1, vert - j] := phon;
                  end;
                  if m[hor - dist, vert + 1] <> ' '
                  then m[hor - dist, vert + 1] := phon;
                  if m[hor - dist - 2, vert + 1] <> ' '
                  then m[hor - dist - 2, vert + 1] := phon;
                  if m[hor - dist, vert - 1] <> ' '
                  then m[hor - dist, vert - 1] := phon;
                  if m[hor - dist - 2, vert - 1] <> ' '
                  then m[hor - dist - 2, vert - 1] := phon;
                  delay(100);
                  dist := 15;
               end;
               m[hor - dist, vert] := ' ';
            end;
         end;
         'x' : if ammo > 0 then
         begin
            ammo := ammo - 1;
            for dist:=1 to 15 do
            begin
               m[hor + dist, vert] := b;
               for j:=1 to y do
               begin
                  for i:=1 to x do
                  begin
                     case m[i, j] of
                        chr(1) : textcolor(green);
                        chr(178) : textcolor(brown);
                        chr(176) : textcolor(7);
                        chr(9) : textbackground(red);
                        chr(219) : textcolor(green);
                        chr(247) : textbackground(blue);
                     else if j <= 8 then textbackground(lightblue)
                     else textbackground(8);
                     end;
                     write(m[i, j]);
                     textcolor(7);
                     textbackground(8);
                  end;
                  
                  writeln;
               end;
               textcolor(red);
               for i:=1 to health do write(chr(3));
               textcolor(7);
               delay(100);
               if (m[hor + dist + 1, vert] = d) or (m[hor + dist + 1, vert] = c) then
               begin
                  m[hor + dist + 1, vert] := phon;
                  for j:=1 to 2 do
                  begin
                     if m[hor + dist + 1 + j, vert] <> ' '
                     then m[hor + dist + 1 + j, vert] := phon;
                     if m[hor + dist + 1 - j, vert] <> ' '
                     then m[hor + dist + 1 - j, vert] := phon;
                     if m[hor + dist + 1, vert + j] <> ' '
                     then m[hor + dist + 1, vert + j] := phon;
                     if m[hor + dist + 1, vert - j] <> ' '
                     then m[hor + dist + 1, vert - j] := phon;
                  end;
                  if m[hor + dist, vert + 1] <> ' '
                  then m[hor + dist, vert + 1] := phon;
                  if m[hor + dist + 2, vert + 1] <> ' '
                  then m[hor + dist + 2, vert + 1] := phon;
                  if m[hor + dist, vert - 1] <> ' '
                  then m[hor + dist, vert - 1] := phon;
                  if m[hor + dist + 2, vert - 1] <> ' '
                  then m[hor + dist + 2, vert - 1] := phon;
                  delay(100);
                  dist := 15;
               end;
               
               m[hor + dist, vert] := ' ';
            end;
         end;
         'c' :
         begin
            read(bl);
            g := chr(bl);
         end;
      end;
      
      for j:=1 to 24 do
         for i:=1 to 72 do if m[i, j] = c then
         begin
            if hor - 1 > i then
            begin
               if (m[i + 1, j] = ' ') or (m[i + 1, j] = phon) then
               begin
                  m[i, j] := ' ';
                  if m[i + 1, j] = phon then m[i, j] := phon;
                  m[i + 1, j] := c;
                  i := i + 1;
               end
               else if (m[i + 1, j - 1] = ' ') or (m[i + 1, j - 1] = phon) then
               begin
                  m[i, j] := ' ';
                  if m[i + 1, j - 1] = phon then m[i, j] := phon;
                  m[i + 1, j - 1] := c;
               end;
            end;
            if hor + 1 < i then
            begin
               if (m[i - 1, j] = ' ') or (m[i - 1, j] = phon) then
               begin
                  m[i, j] := ' ';
                  if m[i - 1, j] = phon then m[i, j] := phon;
                  m[i - 1, j] := c;
               end
               else if (m[i - 1, j - 1] = ' ') or (m[i - 1, j - 1] = phon) then
               begin
                  m[i, j] := ' ';
                  if m[i - 1, j - 1] = phon then m[i, j] := phon;
                  m[i - 1, j - 1] := c;
               end;
            end;
         end;
      for j:=1 to 24 do
         for i:= 1 to 72 do
         begin
            if (m[i, j] = c) and ((m[i, j + 1] = phon) or (m[i, j + 1] = ' ')) then
            begin
               zx := i;
               zy := j;
               while (m[zx, zy + 1] = phon) or (m[zx, zy + 1] = ' ') do
               begin
                  m[zx, zy] := m[zx, zy + 1];
                  zy := zy + 1;
                  m[zx, zy] := c;
               end;
            end;
         end;
      zx := random(72);
      zy := random(24);
      if (m[zx, zy] = ' ') and (m[zx, zy + 1] <> ' ') and (m[zx, zy + 1] <> phon) and (m[zx, zy + 1] <> c) then
      begin
         m[zx, zy] := c;
      end;
      clrscr;
      for j:=1 to y do
      begin
         for i:=1 to x do
         begin
            case m[i, j] of
               chr(1) : textcolor(green);
               chr(178) : textcolor(brown);
               chr(176) : textcolor(7);
               chr(9) : textbackground(red);
               chr(219) : textcolor(green);
               chr(247) : textbackground(blue);
            else if j <= 8 then textbackground(lightblue)
            else textbackground(8);
            end;
            write(m[i, j]);
            textcolor(7);
            textbackground(8);
         end;
         writeln;
      end;
      textcolor(red);
      for i:=1 to health do write(chr(3));
      textcolor(blue);
      write (' ammo ', ammo);
      textcolor(7);
      delay(100);
      if vert = 23 then
      begin
         textcolor(green);
         writeln('You have WON');
         write('You have done ', k, ' moves,Continue?1/0');
         read(cont);
         if cont = 1 then
         begin
            assign(fil, 'ground2.txt');
            reset(fil);
            goto metka1;
         end;
         readln;
         break;
      end;
      if health = 0 then
      begin
         textcolor(red);
         write('You have been killed.');
         break;
      end;
      if (m[hor + 1, vert] = c) or (m[hor - 1, vert] = c) or (m[hor, vert + 1] = c) or (m[hor, vert - 1] = c) then
      begin
         if health > 3 then health := health - 3
         else health := 0;
         m[hor, vert] := phon;
         for j:=1 to 2 do
         begin
            if m[hor + j, vert] <> ' '
            then m[hor + j, vert] := phon;
            if m[hor - j, vert] <> ' '
            then m[hor - j, vert] := phon;
            if m[hor, vert + j] <> ' '
            then m[hor, vert + j] := phon;
            if m[hor, vert - j] <> ' '
            then m[hor, vert - j] := phon;
         end;
         if m[hor - 1, vert + 1] <> ' '
         then m[hor - 1, vert + 1] := phon;
         if m[hor + 1, vert + 1] <> ' '
         then m[hor + 1, vert + 1] := phon;
         if m[hor, vert - 1] <> ' '
         then m[hor, vert - 1] := phon;
         if m[hor + 1, vert - 1] <> ' '
         then m[hor + 1, vert - 1] := phon;
         delay(100);
         clrscr;
         for j:=1 to y do
         begin
            for i:=1 to x do
            begin
               case m[i, j] of
                  chr(1) : textcolor(green);
                  chr(178) : textcolor(brown);
                  chr(176) : textcolor(7);
                  chr(9) : textbackground(red);
                  chr(219) : textcolor(green);
                  chr(247) : textbackground(blue);
               else if j <= 8 then textbackground(lightblue)
               else textbackground(8);
               end;
               write(m[i, j]);
               textcolor(7);
               textbackground(8);
            end;
            writeln;
         end;
         textcolor(red);
         for i:=1 to health do write(chr(3));
         textcolor(7);
         delay(100);
      end;
      if (m[hor, vert + 1] = ' ') or (m[hor, vert + 1] = phon) then while (m[hor, vert + 1] = ' ') or (m[hor, vert + 1] = phon)
      do
      begin
         d1 := d1 + 1;
         m[hor, vert] := m[hor, vert + 1];
         vert := vert + 1;
         m[hor, vert] := l;
         clrscr;
         for j:=1 to y do
         begin
            for i:=1 to x do
            begin
               case m[i, j] of
                  chr(1) : textcolor(green);
                  chr(178) : textcolor(brown);
                  chr(176) : textcolor(7);
                  chr(9) : textbackground(red);
                  chr(219) : textcolor(green);
                  chr(247) : textbackground(blue);
               else if j <= 8 then textbackground(lightblue)
               else textbackground(8);
               end;
               write(m[i, j]);
               textcolor(7);
               textbackground(8);
            end;
            writeln;
         end;
         textcolor(red);
         for i:=1 to health do write(chr(3));
         textcolor(7);
         delay(100);
      end;
      if d1 > 2 then health := health - (d1 div 2);
      d1 := 0;
   end;
   readln;
end.

