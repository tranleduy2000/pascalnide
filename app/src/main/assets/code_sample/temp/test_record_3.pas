uses crt;
type
    danhsach= record;
        tenhang: string[30];
        soluong: integer;
        dongia: real;
        thanhtien: real;
    end;
var
    DS: Array[1..50]of danhsach
    i: Byte;
begin
    clrscr;
    writeln('nhap danh sach cac mat hang');
    i := 0;
    repeat i := i + 1
        write('nhap thong tin cho hang hoa thu:',i);
        write('ten hang:');
        readln(DS[i].tenhang);
        if DS[i].tenhang <> '*' then
        begin
            write('so luong:');
            readln(DS[i].soluong);
            write('don gia:');
            readln(DS[i].dongia);
            DS[i].thanhtien := DS[i].dongia * DS[i].soluong;
            write('thanh tien',DS[i].thanhtien:4:2);
        end;
    until DS[i].tenhang = '*';
end.