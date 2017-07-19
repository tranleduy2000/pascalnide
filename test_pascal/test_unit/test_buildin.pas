Program Calculate_Area (input, output);
uses crt;
var
    a, b, c, s, area: real;

begin
    textbackground(white); (* gives a white background *)
    clrscr; (*clears the screen *)

    textcolor(green); (* text color is green *)
    gotoxy(30, 4); (* takes the pointer to the 4th line and 30th column)

   writeln('This program calculates area of a triangle:');
   writeln('Area = area = sqrt(s(s-a)(s-b)(s-c))');
   writeln('S stands for semi-perimeter');
   writeln('a, b, c are sides of the triangle');
   writeln('Press any key when you are ready');

   readkey;
   clrscr;
   gotoxy(20,3);

   write('Enter a: ');
   readln(a);
   gotoxy(20,5);

   write('Enter b:');
   readln(b);
   gotoxy(20, 7);

   write('Enter c: ');
   readln(c);

   s := (a + b + c)/2.0;
   area := sqrt(s * (s - a)*(s-b)*(s-c));
   gotoxy(20, 9);

   writeln('Area: ',area:10:3);
   readkey;
end