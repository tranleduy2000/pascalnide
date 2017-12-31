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

procedure setColor(fore, back : byte);
begin
  textColor(fore);
  textBackground(back);
end;

procedure printMenu(items : TArrayOfString; selected : integer);
var
  i : integer;
begin
  for i := 0 to high(items) do
  begin
    if i = selected then setColor(blue, white)
    else setColor(white, black);
    writeln(' ', items[i], ' ');
  end;
  setColor(white, black);
end;

function selectMenu(items : TArrayOfString; top : integer) : integer;
var
  ch : char;
  i, l : integer;
begin
  i := 0;
  l := high(items);
  // print menu
  gotoXY(0, top);
  printMenu(items, i);
  repeat
    ch := readKey;
      // move selection
      //if ch = #0 then
  begin
    //ch := readKey; // read scancode
    case ch of
        // down and right
        //#77, #80
      'v', 'b' :
      begin
        i := i + 1;
        if i > l then i := 0;
      end;
        // up and left
        //#72, #75
      'g', 'c' :
      begin
        i := i - 1;
        if i < 0 then i := l;
      end;
    end;
  end;
      // print menu
    gotoXY(0, top);
    printMenu(items, i);
      // enter to choose, q to cancel
  until (ch = #10) or (ch = 'q');
  // determine result
  if ch = #10 then
    selectMenu := i
  else
    selectMenu := -1;
end;

var
  menus : TArrayOfString;
  selected : integer;

begin
  clrScr;
  writeln('Dengan menggunakan tombol panah,');
  writeln('pilih dari menu berikut ini:');
  // activate menu
  menus := setMenus(['menu 1', 'menu 2', 'menu 3', 'menu 4']);
  selected := selectMenu(menus, whereY);
  // print selection
  if selected >= 0 then
    writeln('Anda memilih "', menus[selected], '".')
  else
    writeln('Anda membatalkan pilihan.');
  writeln('Terima kasih.');
end.