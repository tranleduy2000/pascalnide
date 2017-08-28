Program Cos;

{ Program to demonstrate the Cos function. }

Var
  R : Real;

begin
  R := Cos(Pi); { R:=-1 }
  writeln(R);
  R := Cos(Pi / 2); { R:=0  }
  WriteLn(R);
  R := Cos(0); { R:=1  }
  WriteLn(R);
end.
