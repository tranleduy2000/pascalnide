{declare library for use clipboard on Android}
uses aClipboard;

var
  s, clip : string;

begin
  writeln('Please enter text: ');
  readln(s);

  {Put text in the clipboard.}
  setClipboard(s);

  {Read text from the clipboard.}
  clip := getClipboard;
  writeln(clip);
end.