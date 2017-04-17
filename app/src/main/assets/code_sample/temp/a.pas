program easy10rekt420;
uses strutils,sysutils,crt,math;
const
    bitcount= 16;
var
    a: string; t, b, ap: string; i: integer;
begin
    write('Ievadiet skaitli: ');
    readln(a);
    a := trim(a);
    t := '';
    b := '';
    ap := '';
    if abs(strToInt(a)) > power(2,bitcount - 1) - 1 then
        writeln('Out of Bounds')
    else
    begin
        if strToInt(a) > 0 then
            t := '0' + intToBin(strToInt(a),bitcount - 1,0)
        else if strToInt(a) = 0 then
            if copy(a,1,1) = '-' then
                t := '1' + intToBin(abs(strToInt(a)),bitcount - 1,0)
            else
                t := '0' + intToBin(abs(strToInt(a)),bitcount - 1,0)
        else if strToInt(a) < 0 then
            t := '1' + intToBin(abs(strToInt(a)),bitcount - 1,0);
        writeln('Tiesaja koda: ',t);
        if strToInt(a) > 0 then
            ap := '0' + intToBin(strToInt(a),bitcount - 1,0)
        else if strToInt(a) = 0 then
            if copy(a,1,1) = '-' then
                ap := intToBin(-1,bitcount,0)
            else
                ap := intToBin(0,bitcount,0)
        else if strToInt(a) < 0 then
        begin
            b := '0' + intToBin(abs(strToInt(a)),bitcount - 1,0);
            for i:=1 to bitcount do
                if copy(b,i,1) = '1' then
                    ap := ap + '0'
                else
                    ap := ap + '1';
        end;
        writeln('Apgriezta koda: ',ap);
    end;
    ReadLn();
end.