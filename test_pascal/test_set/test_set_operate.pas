program SetOpDemo;

type
  TCharSet = set of Char;

var
  S1, S2, S3 : TCharSet;
  Result : Boolean;

begin
  S1 := ['a', 'b', 'c'];
  S2 := ['c', 'd', 'e'];
  S3 := S1 + S2; { S3 = ['a', 'b', 'c', 'd', 'e'] }
  writeln(s3);
  S3 := S1 * S2; { S3 = ['c'] }
  writeln(s3);
  S3 := S1 - S2; { S3 = ['a', 'b'] }
  writeln(s3);
  S3 := S1 >< S2; { S3 = ['a', 'b', 'd', 'e'] }
  writeln(s3);

  S1 := ['c', 'd', 'e'];
  Result := S1 = S2; { False }
  writeln(Result);
  Result := S1 < S2; { False }
  writeln(Result);
  Result := S1 <= S2; { True }
  writeln(Result);

  S1 := ['c', 'd'];
  Result := S1 <> S2; { True }
  writeln(Result);

  Result := S2 > S1; { True }
  writeln(Result);

  Result := S2 >= S1; { True }
  writeln(s3);

end.

{http://www.gnu-pascal.de/gpc/Set-Operations.html}