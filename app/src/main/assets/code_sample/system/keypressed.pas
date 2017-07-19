Program Example2;
uses Crt;

{ Program to demonstrate the KeyPressed function. }

begin
    WriteLn('Waiting until a key is pressed');
    repeat
    until KeyPressed;
    { The key is not Read,
      so it should also be outputted at the commandline}
end.
{http://www.freepascal.org/docs-html/rtl/crt/keypressed.html}