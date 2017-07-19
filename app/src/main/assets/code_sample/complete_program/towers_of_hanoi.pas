{http://wiki.freepascal.org/Programming_Assignment_4}

(* Author:    Tao Yue
   Date:      13 July 2000
   Description:
      Solves the Towers of Hanoi
   Version:
      1.0 - original version
*)

program TowersofHanoi;

var
  numdiscs : integer;

(* *******************************************************)

procedure DoTowers (NumDiscs, OrigPeg, NewPeg, TempPeg : integer);
(* Explanation of variables:
      Number of discs -- number of discs on OrigPeg
      OrigPeg -- peg number of the tower
      NewPeg -- peg number to move the tower to
      TempPeg -- peg to use for temporary storage
*)

begin
  (* Take care of the base case -- one disc *)
  if NumDiscs = 1 then
    writeln (OrigPeg, ' ---> ', NewPeg)
      (* Take care of all other cases *)
  else
  begin
    (* First, move all discs except the bottom disc
       to TempPeg, using NewPeg as the temporary peg
       for this transfer *)
    DoTowers (NumDiscs-1, OrigPeg, TempPeg, NewPeg);
    (* Now, move the bottommost disc from OrigPeg
       to NewPeg *)
    writeln (OrigPeg, ' ---> ', NewPeg);
    (* Finally, move the discs which are currently on
       TempPeg to NewPeg, using OrigPeg as the temporary
       peg for this transfer *)
    DoTowers (NumDiscs-1, TempPeg, NewPeg, OrigPeg)
  end
end;

(********************************************************)


begin    (* Main *)
  write ('Please enter the number of discs in the tower ===> ');
  readln (numdiscs);
  writeln;
  DoTowers (numdiscs, 1, 3, 2)
end.     (* Main *)