{http://wiki.freepascal.org/Programming_Assignment_2}

{
Again find the sum and average of five numbers,
but this time read in five integers and display
the output in neat columns.
Refer to the original problem specification if needed.
You should type in the numbers separated by spaces
from the keyboard: 45 7 68 2 34.
The output should now look like this:
Number of integers = 5

Number1:      45
Number2:       7
Number3:      68
Number4:       2
Number5:      34
================
Sum:         156
Average:      31.2
}

(* Author:    Tao Yue
   Date:      19 June 1997
   Description:
      Find the sum and average of five predefined numbers
   Version:
      1.0 - original version
      2.0 - read in data from keyboard
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
  write ('Enter the first number: ');
  readln (A);
  write ('Enter the second number: ');
  readln (B);
  write ('Enter the third number: ');
  readln (C);
  write ('Enter the fourth number: ');
  readln (D);
  write ('Enter the fifth number: ');
  readln (E);
  Sum := A + B + C + D + E;
  Average := Sum / 5;
  writeln ('Number of integers = ', NumberOfIntegers);
  writeln;
  writeln ('Number1:', A: 8);
  writeln ('Number2:', B: 8);
  writeln ('Number3:', C: 8);
  writeln ('Number4:', D: 8);
  writeln ('Number5:', E: 8);
  writeln ('================');
  writeln ('Sum:', Sum: 12);
  writeln ('Average:', Average: 10 : 1);
end.