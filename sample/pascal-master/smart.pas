program test;
(* niveau difficile (intellignet) pour le jeu Tic-Tac-Toe *)
type
  tab = array [1..9] of string;

const
  t : tab = ('1', '' , '0',
             '' , '1', '1',
             '1', '', ''
    ); 

function difficile(t : tab) : byte;
var
  st : byte;

function com(c1, c2, c3 : string; n1, n2, n3 : byte; var foo : byte) : boolean;
begin
  if c1 = c2 then 
    begin  
      foo := n3;
      com := True;
    end
  else if c1 = c3 then 
    begin  
      foo := n2;
      com := True;
    end
  else if c2 = c3 then 
    begin  
      foo := n1;
      com := True;
    end
  else 
    com := False;
end;

begin
  if com(t[1], t[2], t[3], 1, 2, 3, st) or com(t[4], t[5], t[6], 4, 5, 6, st) or com(t[7], t[8], t[9], 7, 8, 9, st)
  or com(t[1], t[4], t[7], 1, 4, 7, st) or com(t[2], t[5], t[8], 2, 5, 8, st) or com(t[3], t[6], t[9], 3, 6, 9, st)
  or com(t[1], t[5], t[9], 1, 5, 9, st) or com(t[3], t[5], t[7], 3, 5, 7, st) then
     begin
     difficile := st;
     //writeln(st);
     end
  else 
    begin
      randomize;
      difficile := random(9) + 1;
    end;
end;

BEGIN
  writeln(difficile(t));
END.
fggg



