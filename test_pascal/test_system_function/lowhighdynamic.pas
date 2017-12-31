program consoleMenu;

uses CRT;

type
  TArrayOfString = array of string;

function setMenus(items : array of string) : TArrayOfString;
var
  i, l : integer;
begin
  l := high(items);
  setLength(setMenus, l + 1);
  for i := 0 to l do setMenus[i] := items[i]
end;

var
  menus : TArrayOfString;

begin
  writeln('Dengan menggunakan tombol panah,');
  writeln('pilih dari menu berimenukut ini:');
  // activate
  menus := setMenus(['menu 1', 'menu 2', 'menu 3', 'menu 4']);
  writeln(menus);
end.