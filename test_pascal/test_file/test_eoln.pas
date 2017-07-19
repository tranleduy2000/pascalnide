var
  myFile : TextFile;
  letter : char;
  text : string;

begin
  // Try to open the Test.txt file for writing to
  AssignFile(myFile, 'Test.txt');
  ReWrite(myFile);

  // Write lines of text to the file
  WriteLn(myFile, 'Hello');
  WriteLn(myFile, 'To you');

  // Close the file
  CloseFile(myFile);

  // Reopen the file for reading
  Reset(myFile);

  // Display the file contents
  while not Eof(myFile) do
  begin
    // Proces one line at a time
    WriteLn('Start of a new line :');
    while not Eoln(myFile) do
    begin
      Read(myFile, letter); // Read and display one letter at a time
      WriteLn(letter);
    end;
    readln(myFile, text);
  end;
  writeln('End of File');
  // Close the file for the last time
  CloseFile(myFile);
end.