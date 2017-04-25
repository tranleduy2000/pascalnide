uses aSpeech;
var
    str: String;
begin
    {Please enable network, audio permission, speaker ...}

    writeln('Enter your text: ');
    readln(str);

    {Text to speech}
    speak(srt);

    readln;

    {Stop the speak}
    stopSpeak();
end.