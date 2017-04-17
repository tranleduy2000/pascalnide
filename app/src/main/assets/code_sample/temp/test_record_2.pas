Uses Crt;
Type
    Complex = Record
        a, b: Real;
    End;

Var
    c1, c2, c3: Complex;
    dau: string;
Begin
    Writeln('Nhap so phuc c1:');
    Write('Phan thuc a = ');
    Readln(c1.a);
    Write('Phan ao b = ');
    Readln(c1.b);
    Writeln('Nhap so phuc c2:');
    Write('Phan thuc a = ');
    Readln(c2.a);
    Write('Phan ao b = ');
    Readln(c2.b);

{Tính tổng 2 số phức}

    c3.a := c1.a + c2.a;
    c3.b := c1.b + c2.b;

{In kết quả ra màn hình}

    Writeln('Tong cua 2 so phuc:');
    If c1.b >= 0 Then dau :='+i' else dau :='-i';
    Writeln('c1 = ', c1.a:0:2, dau, abs(c1.b):0:2); {Số phức c1}
    If c2.b >= 0 Then dau :='+i' else dau :='-i';
    Writeln('c2 = ', c2.a:0:2, dau, abs(c2.b):0:2); {Số phức c2}
    Writeln('La so phuc:');
    If c3.b >= 0 Then dau :='+i' else dau :='-i';
    Writeln('c3 = ', c3.a:0:2, dau, abs(c3.b):0:2); {Số phức c3}
    Readln;
End.