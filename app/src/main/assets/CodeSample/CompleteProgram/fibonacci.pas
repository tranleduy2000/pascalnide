(* Author:    Tao Yue
   Date:      19 July 1997
   Description:
      Find the first 10 Fibonacci numbers
   Version:
      1.0 - original version
*)

{
Problem 1
Find the first 10 numbers in the Fibonacci sequence.
The Fibonacci sequence starts with two numbers: 1 1

Each subsequent number is formed by adding the two numbers before it.
 1+1=2, 1+2=3, 2+3=5, etc. This forms the following sequence:
1 1 2 3 5 8 13 21 34 55 89 144 ...
}

program Fibonacci;

var
  Fibonacci1, Fibonacci2 : integer;
  temp : integer;
  count : integer;

begin
  (* Main *)
  writeln ('First ten Fibonacci numbers are:');
  count := 0;
  Fibonacci1 := 0;
  Fibonacci2 := 1;
  repeat
    write (Fibonacci2: 7);
    temp := Fibonacci2;
    Fibonacci2 := Fibonacci1 + Fibonacci2;
    Fibonacci1 := Temp;
    count := count + 1
  until count = 10;
  writeln;

  (* Of course, you could use a FOR loop or a WHILE loop
     to solve this problem. *)

end.     (* Main *)