Program Length;

{ Program to demonstrate the Length function. }

type
  somebytes = array [6..10] of byte;

  somewords = array [3..10] of word;

Var
  S : String;
  I : Integer;
  bytes : somebytes;
  words : somewords;

begin
  S := '';
  for i:=1 to 10 do
  begin
    S := S + '*';
    Writeln (Length(S): 2, ' : ', s);
  end;
  Writeln('Bytes : ', length(bytes));
  Writeln('Words : ', length(words));
end.