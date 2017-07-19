{http://wiki.freepascal.org/Programming_Assignment}

(* Author:    Tao Yue
   Date:      19 June 1997
   Description:
      Find the sum and average of five predefined numbers
   Version:
      1.0 - original version
*)

program SumAverage;

const
  NumberOfIntegers = 5;

var
  A, B, C, D, E : integer;
  Sum : integer;
  Average : real;

begin
  (* Main *)
  A := 45;
  B := 7;
  C := 68;
  D := 2;
  E := 34;
  Sum := A + B + C + D + E;
  Average := Sum / NumberOfIntegers;
  writeln ('Number of integers = ', NumberOfIntegers);
  writeln ('Number1 = ', A);
  writeln ('Number2 = ', B);
  writeln ('Number3 = ', C);
  writeln ('Number4 = ', D);
  writeln ('Number5 = ', E);
  writeln ('Sum = ', Sum);
  writeln ('Average = ', Average)
end.