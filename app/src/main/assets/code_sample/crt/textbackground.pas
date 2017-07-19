Program ExampleBgColor;

uses Crt;

{ Program to demonstrate the TextBackground function. }

begin
    WriteLn('This is written in with the default background color');
    TextBackground(Green);
    WriteLn('This is written in with a Green background');
    TextBackground(Brown);
    WriteLn('This is written in with a Brown background');
    TextBackground(Blue);
    WriteLn('This is written in with a Blue background');
    TextBackground(Cyan);
    WriteLn('This is written in with a Cyan background');
    TextBackground(Red);
    WriteLn('This is written in with a Red background');
    TextBackground(Magenta);
    WriteLn('This is written in with a Magenta background');
    TextBackground(LightGray);
    WriteLn('This is written in with a LightGray background');
    TextBackground(DarkGray);
    WriteLn('This is written in with a DarkGray background');
    TextBackground(LightBlue);
    WriteLn('This is written in with a LightBlue background');
    TextBackground(LightGreen);
    WriteLn('This is written in with a LightGreen background');
    TextBackground(LightCyan);
    WriteLn('This is written in with a LightCyan background');
    TextBackground(LightRed);
    WriteLn('This is written in with a LightRed background');
    TextBackground(LightMagenta);
    WriteLn('This is written in with a LightMagenta background');
    TextBackground(Yellow);
    WriteLn('This is written in with a Yellow background');
    TextBackground(Black);
    WriteLn('Back with a black background');
    ReadLn;

end.
{http://www.freepascal.org/docs-html/rtl/crt/textbackground.html}