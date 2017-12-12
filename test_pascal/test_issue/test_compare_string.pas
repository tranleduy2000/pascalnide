program bigNumber;
uses sysutils;

type
  bigNum = string;

function bigCmp(a, b : bigNum) : longint;
begin
  while length(a) < length(b) do a := '0' + a;
  while length(b) < length(a) do b := '0' + b;
  if a > b then exit(1);
  if a < b then exit(-1);
  exit(0)
end;

function bigAdd(a, b : bigNum) : bigNum;
var
  s, i, c : longint;
begin
end;

var
  x, y : string;

begin
  readln(x);
  ReadLn(y);
  WriteLn(bigCmp(x, y));
end.