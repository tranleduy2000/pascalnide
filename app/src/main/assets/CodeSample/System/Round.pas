Program Round;

{ Program to demonstrate the Round function. }

begin
  Writeln (Round(1234.56)); { Prints 1235     }
  Writeln (Round(-1234.56)); { Prints -1235    }
  Writeln (Round(12.3456)); { Prints 12       }
  Writeln (Round(-12.3456)); { Prints -12      }
  Writeln (Round(2.5)); { Prints 2 (down) }
  Writeln (Round(3.5)); { Prints 4 (up)   }

end.