program md5Cracker;
uses md5;

var
  wline, hline, wl, hf: string;
  wordlist, hashFile : textfile;
begin
  write('Enter Hash File > ');readln(hf);
  write('Enter Wordlist > ');readln(wl);
  assign(hashFile, hf);
  reset(hashFile);
  assign(wordlist, wl);
  reset(wordlist);
  repeat
    readln(hashFile, hline);
    repeat
      readln(wordlist, wline);
      if (MD5Print(MD5String(wline)) = hline) then
        writeln('[+] Cracked :', hline, ' -> ', wline);
    until EOF(wordlist);
  until EOF(hashFile);
end.
