Program example71;

{$mode objfpc}

{ This program demonstrates the Format function }

Uses sysutils;

Var
  P : Pointer;
  fmt, S : string;

{ Expected output:
        [%d] => [10]
        [%%] => [%]
      [%10d] => [        10]
      [%.4d] => [0010]
    [%10.4d] => [      0010]
      [%0:d] => [10]
    [%0:10d] => [        10]
  [%0:10.4d] => [      0010]
   [%0:-10d] => [10        ]
 [%0:-10.4d] => [0010      ]
    [%-*.*d] => [00010]
}
Procedure TestInteger;
begin
  Try
    Fmt := '[%d]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%%]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10d]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    fmt := '[%.4d]';
    S := Format (fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10.4d]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:d]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10d]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10.4d]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10d]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10.4d]';
    S := Format (fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%-*.*d]';
    S := Format (fmt, [4, 5, 10]);
    writeln(Fmt: 12, ' => ', s);
  except
    On E : Exception do
    begin
      Writeln ('Exception caught : ', E.Message);
    end;
  end;
  writeln ('Press enter');
  readln;
end;

{ Expected output:
        [%x] => [A]
      [%10x] => [         A]
    [%10.4x] => [      000A]
      [%0:x] => [A]
    [%0:10x] => [         A]
  [%0:10.4x] => [      000A]
   [%0:-10x] => [A         ]
 [%0:-10.4x] => [000A      ]
    [%-*.*x] => [0000A]
}
Procedure TestHexaDecimal;
begin
  try
    Fmt := '[%x]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10x]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10.4x]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:x]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10x]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10.4x]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10x]';
    S := Format (Fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10.4x]';
    S := Format (fmt, [10]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%-*.*x]';
    S := Format (fmt, [4, 5, 10]);
    writeln(Fmt: 12, ' => ', s);
  except
    On E : Exception do
    begin
      Writeln ('Exception caught : ', E.Message);
    end;
  end;
  writeln ('Press enter');
  readln;
end;

{ Expected output:
      [0x%p] => [0x0012D687]
    [0x%10p] => [0x  0012D687]
  [0x%10.4p] => [0x  0012D687]
    [0x%0:p] => [0x0012D687]
  [0x%0:10p] => [0x  0012D687]
[0x%0:10.4p] => [0x  0012D687]
 [0x%0:-10p] => [0x0012D687  ]
[0x%0:-10.4p] => [0x0012D687  ]
    [%-*.*p] => [0012D687]
}
Procedure TestPointer;
begin
  P := Pointer(1234567);
  try
    Fmt := '[0x%p]';
    S := Format (Fmt, [P]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[0x%10p]';
    S := Format (Fmt, [P]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[0x%10.4p]';
    S := Format (Fmt, [P]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[0x%0:p]';
    S := Format (Fmt, [P]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[0x%0:10p]';
    S := Format (Fmt, [P]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[0x%0:10.4p]';
    S := Format (Fmt, [P]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[0x%0:-10p]';
    S := Format (Fmt, [P]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[0x%0:-10.4p]';
    S := Format (fmt, [P]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%-*.*p]';
    S := Format (fmt, [4, 5, P]);
    writeln(Fmt: 12, ' => ', s);
  except
    On E : Exception do
    begin
      Writeln ('Exception caught : ', E.Message);
    end;
  end;
  writeln ('Press enter');
  readln;
end;

{ Expected output:
        [%s]=> [This is a string]
      [%0:s]=> [This is a string]
    [%0:18s]=> [  This is a string]
   [%0:-18s]=> [This is a string  ]
 [%0:18.12s]=> [      This is a st]
    [%-*.*s]=> [This is a st      ]
}
Procedure TestString;
begin
  try
    Fmt := '[%s]';
    S := Format(fmt, ['This is a string']);
    Writeln(fmt: 12, '=> ', s);
    fmt := '[%0:s]';
    s := Format(fmt, ['This is a string']);
    Writeln(fmt: 12, '=> ', s);
    fmt := '[%0:18s]';
    s := Format(fmt, ['This is a string']);
    Writeln(fmt: 12, '=> ', s);
    fmt := '[%0:-18s]';
    s := Format(fmt, ['This is a string']);
    Writeln(fmt: 12, '=> ', s);
    fmt := '[%0:18.12s]';
    s := Format(fmt, ['This is a string']);
    Writeln(fmt: 12, '=> ', s);
    fmt := '[%-*.*s]';
    s := Format(fmt, [18, 12, 'This is a string']);
    Writeln(fmt: 12, '=> ', s);
  except
    On E : Exception do
    begin
      Writeln ('Exception caught : ', E.Message);
    end;
  end;
  writeln ('Press enter');
  readln;
end;

{ Expected output:
        [%e] => [1.2340000000000000E+000]
      [%10e] => [1.2340000000000000E+000]
    [%10.4e] => [1.234E+000]
      [%0:e] => [1.2340000000000000E+000]
    [%0:10e] => [1.2340000000000000E+000]
  [%0:10.4e] => [1.234E+000]
   [%0:-10e] => [1.2340000000000000E+000]
 [%0:-10.4e] => [1.234E+000]
    [%-*.*e] => [1.2340E+000]
}
Procedure TestExponential;
begin
  Try
    Fmt := '[%e]';
    S := Format (Fmt, [1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10e]';
    S := Format (Fmt, [1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10.4e]';
    S := Format (Fmt, [1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:e]';
    S := Format (Fmt, [1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10e]';
    S := Format (Fmt, [1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10.4e]';
    S := Format (Fmt, [1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10e]';
    S := Format (Fmt, [1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10.4e]';
    S := Format (fmt, [1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%-*.*e]';
    S := Format (fmt, [4, 5, 1.234]);
    writeln(Fmt: 12, ' => ', s);
  except
    On E : Exception do
    begin
      Writeln ('Exception caught : ', E.Message);
    end;
  end;
  writeln ('Press enter');
  readln;
end;

{ Expected output:
        [%e] => [-1.2340000000000000E+000]
      [%10e] => [-1.2340000000000000E+000]
    [%10.4e] => [-1.234E+000]
      [%0:e] => [-1.2340000000000000E+000]
    [%0:10e] => [-1.2340000000000000E+000]
  [%0:10.4e] => [-1.234E+000]
   [%0:-10e] => [-1.2340000000000000E+000]
 [%0:-10.4e] => [-1.234E+000]
    [%-*.*e] => [-1.2340E+000]
}
Procedure TestNegativeExponential;
begin
  Try
    Fmt := '[%e]';
    S := Format (Fmt, [-1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10e]';
    S := Format (Fmt, [-1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10.4e]';
    S := Format (Fmt, [-1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:e]';
    S := Format (Fmt, [-1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10e]';
    S := Format (Fmt, [-1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10.4e]';
    S := Format (Fmt, [-1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10e]';
    S := Format (Fmt, [-1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10.4e]';
    S := Format (fmt, [-1.234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%-*.*e]';
    S := Format (fmt, [4, 5, -1.234]);
    writeln(Fmt: 12, ' => ', s);
  except
    On E : Exception do
    begin
      Writeln ('Exception caught : ', E.Message);
    end;
  end;
  writeln ('Press enter');
  readln;
end;

{ Expected output:
        [%e] => [1.2340000000000000E-002]
      [%10e] => [1.2340000000000000E-002]
    [%10.4e] => [1.234E-002]
      [%0:e] => [1.2340000000000000E-002]
    [%0:10e] => [1.2340000000000000E-002]
  [%0:10.4e] => [1.234E-002]
   [%0:-10e] => [1.2300000000000000E-002]
 [%0:-10.4e] => [1.234E-002]
    [%-*.*e] => [1.2340E-002]
}
Procedure TestSmallExponential;
begin
  Try
    Fmt := '[%e]';
    S := Format (Fmt, [0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10e]';
    S := Format (Fmt, [0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10.4e]';
    S := Format (Fmt, [0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:e]';
    S := Format (Fmt, [0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10e]';
    S := Format (Fmt, [0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10.4e]';
    S := Format (Fmt, [0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10e]';
    S := Format (Fmt, [0.0123]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10.4e]';
    S := Format (fmt, [0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%-*.*e]';
    S := Format (fmt, [4, 5, 0.01234]);
    writeln(Fmt: 12, ' => ', s);
  except
    On E : Exception do
    begin
      Writeln ('Exception caught : ', E.Message);
    end;
  end;
  writeln ('Press enter');
  readln;
end;

{ Expected output:
        [%e] => [-1.2340000000000000E-002]
      [%10e] => [-1.2340000000000000E-002]
    [%10.4e] => [-1.234E-002]
      [%0:e] => [-1.2340000000000000E-002]
    [%0:10e] => [-1.2340000000000000E-002]
  [%0:10.4e] => [-1.234E-002]
   [%0:-10e] => [-1.2340000000000000E-002]
 [%0:-10.4e] => [-1.234E-002]
    [%-*.*e] => [-1.2340E-002]
}
Procedure TestSmallNegExponential;
begin
  Try
    Fmt := '[%e]';
    S := Format (Fmt, [-0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10e]';
    S := Format (Fmt, [-0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%10.4e]';
    S := Format (Fmt, [-0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:e]';
    S := Format (Fmt, [-0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10e]';
    S := Format (Fmt, [-0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:10.4e]';
    S := Format (Fmt, [-0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10e]';
    S := Format (Fmt, [-0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%0:-10.4e]';
    S := Format (fmt, [-0.01234]);
    writeln(Fmt: 12, ' => ', s);
    Fmt := '[%-*.*e]';
    S := Format (fmt, [4, 5, -0.01234]);
    writeln(Fmt: 12, ' => ', s);
  except
    On E : Exception do
    begin
      Writeln ('Exception caught : ', E.Message);
    end;
  end;
  writeln ('Press enter');
  readln;
end;

begin
  TestInteger;
  TestHexadecimal;
  TestPointer;
  teststring;
  TestExponential;
  TestNegativeExponential;
  TestSmallExponential;
  TestSmallNegExponential;
end.