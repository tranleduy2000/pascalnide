uses aTTSpeech;

var
  input : String;
begin
  {Please enable network, audio permission, speaker ...}

  writeln('Enter your text: ');
  readln(input);

  {Text to speech, wait....}
  speak(input);

  readln;

  {Stop the speak}
  stopSpeak();
end.