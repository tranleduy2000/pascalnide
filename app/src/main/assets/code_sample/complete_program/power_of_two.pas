(* Author:    Tao Yue
   Date:      13 July 2000
   Description:
      Display all powers of two up to 20000, five per line
   Version:
      1.0 - original version
*)

{
Problem 2
Display all powers of 2 that are less than 20000.
Display the list in a properly formatted manner,
with commas between the numbers. Display five
numbers per line. The output should look like:
    1, 2, 4, 8, 16,
32, 64, 128, 256, 512,
1024, 2048, 4096, 8192, 16384
}

program power_of_two;

const
  numperline = 5;
  maxnum = 20000;
  base = 2;

var
  number : longint;
  linecount : integer;

begin
  (* Main *)
  writeln ('Powers of ', base, ', 1 <= x <= ', maxnum, ':');
  (* Set up for loop *)
  number := 1;
  linecount := 0;
  (* Loop *)
  while number <= maxnum do
  begin
    linecount := linecount + 1;
    (* Print a comma and space unless this is the first
       number on the line *)
    if linecount > 1 then
      write (', ');
    (* Display the number *)
    write (number);
    (* Print a comma and go to the next line if this is
       the last number on the line UNLESS it is the
       last number of the series *)
    if (linecount = numperline) and not (number * 2 > maxnum) then
    begin
      writeln (',');
      linecount := 0
    end;
    (* Increment number *)
    number := number * base;
  end; (* while *)
  writeln;

  (* This program can also be written using a
     REPEAT..UNTIL loop. *)

end.     (* Main *)