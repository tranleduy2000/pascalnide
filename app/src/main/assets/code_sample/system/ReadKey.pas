Program ReadKey;
uses Crt;

{ Program to demonstrate the ReadKey function. }

var
  ch : char;
begin
  writeln('Press a/b, q=Quit');
  repeat
    ch := ReadKey;
    case ch of
      #0 :
      begin
        ch := ReadKey; {Read ScanCode}
        case ch of
          #97 : WriteLn('pressed a');
          #98 : WriteLn('pressed b');
        end;
      end;
      #113 : WriteLn('quit');
    end;
  until ch = #113 {quit}
end.

{More information: https://en.wikipedia.org/wiki/ASCII}