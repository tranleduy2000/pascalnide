Program high_low_text;
uses Crt;

{ Program to demonstrate the LowVideo, HighVideo, NormVideo functions. }

begin
    LowVideo;
    WriteLn('This is written with LowVideo');
    HighVideo;
    WriteLn('This is written with HighVideo');
    NormVideo;
    WriteLn('This is written with NormVideo');
end.