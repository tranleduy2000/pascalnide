PROGRAM Flappy;
USES CRT;

VAR
   TREN, DUOI, L, tangXDT, tangYDT, tangXNV : BYTE;
   XDT, YDT, XNV, F, DIEM0, DIEM, TGROI : INTEGER;
   TEN : STRING;
   _FILE : TEXT;

procedure TAOCNV(cot, tren, duoi : integer); {---HAM NAY DE TAO CHUONG NGAI VAT.--- }

var
   i : integer;

begin
   for i := 1 to tren do
   begin
      gotoxy(cot, i);
      write(#25);
   end;
   
   for i:= 21 - duoi to 21 do
   begin
      gotoxy(cot, i);
      write(#24);
   end;
end;

procedure VIETDT(x, y : integer); {----HAM NAY DUNG DE TAO DOI TUONG.2 BIEN LA TOA DO CUA DOI TUONG-----}

begin
   f := 20;
   gotoxy(x, y);
   write(#2);

end;

BEGIN
   CLRSCR;
   RANDOMIZE;
   TEXTBACKGROUND(25);
   TEXTCOLOR(5);
   
   XNV := 30; {DAY LA TOA DO x CUA NGAI VAT , NGAI VAT CHI CAN MOI X .}
{---XDT VA YDT LA CAC TOA DO CUA DOI TUONG.---}
   XDT := 20;
   YDT := 10;
   
   GOTOXY(25, 15);
   WRITELN('AN T DE DI CHUYEN NHAN VAT ...');
   
   DELAY(2000);
   
   VIETDT(XDT, YDT);

{-------------TRO CHOI .. START WRITE------------------}
   
   DIEM := 0;
   
   TREN := 4 + RANDOM(6);
   DUOI := 4 + RANDOM(6);
   L := 0;
   tangXDT := 1;
   tangYDT := 2; {-----XDT = CONST , T/M XDT PHAI CO THE BANG XNV. }
   tangXNV := 8;
   TGROI := 120;
   WHILE L = 0 DO
   
   BEGIN

{-----TAO CHUONG NGAI VAT-----}

{----TREN VA DUOI LA DO DAI CUA CAC COT NGAI VAT O TREN VA DUOI.}

{TANG MUC DO KHO :}
      IF DIEM = 12 THEN
      BEGIN
         TGROI := 85;
         tangXNV := 6; {tangYDT := 2 ;}
      END;
      IF DIEM = 25 THEN
      BEGIN
         TGROI := 70; {tangYDT := 2 ;} tangXNV := 4;
      END;
      
      TAOCNV(XNV, TREN, DUOI);
{-------TAO DOI TUONG --------}

{-- Luc chua co kich thich--- }
      WHILE NOT KEYPRESSED DO
      BEGIN
         DELAY(TGROI);
         CLRSCR;
         TAOCNV(XNV, TREN, DUOI);
         YDT := YDT + 1;
         VIETDT(XDT, YDT);
         
         IF (YDT > = 21) OR (YDT = 1) THEN
         
         BEGIN
            GOTOXY(25, 15);
            WRITE('GAMEOVER');
            DELAY(1500);
            L := 1;
            BREAK;
         END;
      
      END;
      
      IF L = 1 THEN BREAK;

{-- Luc da co kich thich--- }
      IF UPCASE(READKEY) = 'T' THEN
      BEGIN
         
         CLRSCR;
         XDT := XDT + tangXDT;
         YDT := YDT - tangYDT;
         TAOCNV(XNV, TREN, DUOI);
         VIETDT(XDT, YDT);
      
      END;
{----KET THUC PHAN CHINH----/}
{--- XU LY------}
      IF (YDT > = 21) OR (YDT = 1) THEN
      BEGIN
         GOTOXY(25, 15);
         WRITE('GAMEOVER');
         DELAY(1500);
         L := 1;
      END;
      IF (XDT = XNV) THEN
      BEGIN
         IF (YDT <= TREN + 1) OR (YDT > = (21 - DUOI)) THEN
         BEGIN
            GOTOXY(25, 15);
            WRITE('GAMEOVER');
            DELAY(1500);
            L := 1;
         END
         ELSE
         BEGIN
            SOUND(25);
            SOUND(125);
            DIEM := DIEM + 1;
            XNV := XNV + tangXNV;
            TREN := 5 + RANDOM(4);
            DUOI := 5 + RANDOM(5);
         END;
      END;
{--- DOI TUONG DA DI HET XDT > 74 >>> QUA MAN HINH ------}
      
      IF XDT > 74 THEN
      BEGIN
         CLRSCR;
         XDT := 5;
         XNV := 10;
      
      END;
   
   END;
   
   CLRSCR;
   GOTOXY(25, 15);
   WRITE('DIEM CUA BAN LA: ', DIEM, ' DIEM . ');
   GOTOXY(1, 1);
{CLRSCR;
GOTOXY(25,15);
WRITE('DIEM CUA BAN LA: ',DIEM);
{---- TAO FILE VA KIEM TRA FILE LUU KET QUA . ----}
   DIEM0 := 0;
   ASSIGN(_FILE, 'D:\TUAN_FLAPPY');

{$I-} {T?t vi?c ki?m tra Vào/Ra}
   RESET (_FILE);
   READLN;
{$I+}; {M? vi?c ki?m tra Vào/Ra}
   
   IF IOResult <> 0 THEN {BI LOI >> FILE KHONG TON TAI.}
   BEGIN
      REWRITE(_FILE);
      WRITELN(_FILE, 0);
      WRITELN(_FILE, 'NGUYEN DINH TUAN');
   END
   ELSE
   BEGIN
      {BEGIN OF ELSE}
      IF NOT EOF(_FILE) THEN
      BEGIN
         READLN(_FILE, DIEM0);
         READLN(_FILE, TEN);
      END;
      IF DIEM > DIEM0 THEN
      BEGIN
         GOTOXY(25, 16);
         WRITE('THANH TICH MOI: ', DIEM);
         GOTOXY(25, 17);
         WRITE('THANH TICH CU LA: ', DIEM0, ' THUOC VE BAN ', TEN);
         ASSIGN(_FILE, 'D:\TUAN_FALPPY');
         REWRITE(_FILE);
         CLRSCR;
         GOTOXY(5, 1);
         WRITE('TEN CUA BAN LA : ');
         READLN(TEN);
         WRITELN(_FILE, DIEM);
         WRITELN(_FILE, TEN);
      END
      ELSE
      BEGIN
         GOTOXY(25, 17);
         WRITE('THANH TICH CAO NHAT LA: ', DIEM0, ' THUOC VE BAN ', TEN);
      END;
      DELAY(2000);
   END; {END OF ELSE}
   
   DELAY(3000);
   }
   READLN;
END .
