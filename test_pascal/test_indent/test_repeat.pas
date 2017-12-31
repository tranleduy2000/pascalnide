program hdhd;

uses
  crt;

begin
  repeat
    textColor(random(15));
    write(random(10));
  until keyPressed;
  readln;
end .