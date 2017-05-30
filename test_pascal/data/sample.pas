program Rally_Regulirdad;
uses dos, crt, sysutils;
type
    Tramo = record
        numtr : integer;
        kmini : real;
        kmfin : real;
        vel : real;
        ttramo : real;
        hfin : real;
    end;

    Tipo_Rally = array [1..2] of Tramo;


var
    a, b, c, d, i : word;
    ti, hh, mm, ss: real;
    Rally : Tipo_Rally;



function print(w: integer) : string;
var
    s: string;

begin
    str(w, s);
    if w < 10 then print := '0' + s else print := s;
end;

function deg(x: real) : real;
var
    tmp, tmp2, res: real;

begin
    res := int(x);
    tmp := int((x - res) * 100);
    tmp2 := frac((x - res) * 100) * 100;
//writeln('tmp1: ',tmp,'tmp2: ',tmp2);
    res := res + tmp / 60 + tmp2 / 3600;
    deg := res;
end;//deg

function dms(x: real) : real;
var
    tmp, tmp1, tmp2, res: real;

begin
// writeln('x : ',x);
    res := int(x);
    tmp := (x - res) * 60;
    tmp1 := int(tmp);
    tmp2 := (x - res - tmp1 / 60) * 3600;
// writeln(tmp1,'   ',tmp2);
    dms := res + tmp1 / 100 + tmp2 / 10000;
end;//Dms

procedure leer_tramo(numtramo: integer );

begin
    Write ('Numero de tramo...');
    writeln(numtramo);
    Rally[numtramo].numtr := numtramo;
    Write ('Km Inicio de tramo...');
    readln(Rally[numtramo].kmini);
    Write ('Km Fin de tramo...');
    readln(Rally[numtramo].kmfin);
    Write ('Velocidad...');
    readln(Rally[numtramo].vel);
    if Rally[numtramo].vel = 0 then
    begin
        Write ('Tiempo Tramo X.XXXX...');
        readln(Rally[numtramo].ttramo);
        Rally[numtramo].vel
            := (Rally[numtramo].kmfin - Rally[numtramo].kmini) / deg(Rally[numtramo].ttramo);
        Writeln ('La Velocidad es:  ',Rally[numtramo].vel);

    end

    else
    begin

        Rally[numtramo].ttramo
            := dms((Rally[numtramo].kmfin - Rally[numtramo].kmini) / Rally[numtramo].vel);
        writeln('El tiempo Tramo es:  ',Rally[numtramo].ttramo);
    end; //else

    Rally[numtramo].hfin := dms(deg(Rally[numtramo - 1].hfin) + deg(Rally[numtramo].ttramo));
    writeln('Hora fin de Tramo es:  ',Rally[numtramo].hfin);

end; //end of procedure leer_tramo  


begin
    //principal
{
i:=1;
while true do
begin

writeln(i);

   leer_tramo(i);   
   writeln('Sal  de tramo:  ',i);
        
   i := i+1;

end;
}
    gettime(a, b, c, d);
    writeln('Current time');
    writeln(print(a) , ':', print(b) , ':', print(c));
    mm := b;
    mm := mm / 100;
    ss := c;
    ss := ss / 10000;
    ti := a + mm + ss;
    writeln('  Deg: ',deg(ti), '  Dms : ',dms(deg(ti)));
    delay(3300);

end.

{if you want to improve this }
