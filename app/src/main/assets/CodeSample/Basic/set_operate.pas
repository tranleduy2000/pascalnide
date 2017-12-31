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
  S3 := S1 * S2; { S3 = ['c'] }
  S3 := S1 - S2; { S3 = ['a', 'b'] }
  S3 := S1 >< S2; { S3 = ['a', 'b', 'd', 'e'] }

  S1 := ['c', 'd', 'e'];
  Result := S1 = S2; { False }
  Result := S1 < S2; { False }
  Result := S1 <= S2; { True }

  S1 := ['c', 'd'];
  Result := S1 <> S2; { True }
  Result := S2 > S1; { True }
  Result := S2 >= S1   { True }
end.

{http://www.gnu-pascal.de/gpc/Set-Operations.html}