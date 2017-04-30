Program Quan_ly_can_bo;

Uses Crt;

Var
    f: Text; hoten: String[20];
    c1, heso: real; c2, i, n, socon: byte;
    Ten: string[12];

Begin

    Clrscr;
    Write('cho biet ten tep');
    readln(ten);

    Assign(f,ten);
    Rewrite(f);

    Write('nhap bao nhieu nguoi');
    readln(n);

    For i:=1 to n do
    Begin
        Write('Hoten');
        Readln(hoten);

        Write('He so');
        Readln(heso);

        Write('So con');
        Readln(socon);

        Writeln(f,hoten);
        Writeln(f,heso:4:2);
        Writeln(f,socon);

    End;

    Close(f);
    Assign(f,ten);
    Reset(f);
    Writeln('------------------------------------------------------');
    Writeln ('| Ho va ten | Hs | socon | Luong |');
    Writeln('------------------------------------------------------');
    While not eof(f) do
    Begin
        Readln(f,hoten);
        Readln(f,heso);
        Readln(f,socon);
        Writeln('|',ten:19,'|',heso:4:2,'|',socon:4,'|',heso * 540000:10:2,'|');
    End;
    Readln;
End.