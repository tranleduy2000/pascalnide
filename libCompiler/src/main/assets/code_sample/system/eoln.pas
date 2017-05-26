var
    myFile : TextFile;
    letter : char;
    text   : string;

begin
    // Try to open the Test.txt file for writing to
    AssignFile(myFile, 'Test.txt');
    ReWrite(myFile);

    // Write lines of text to the file
    WriteLn(myFile, 'Hello');
    WriteLn(myFile, 'To you');

    // Close the file
    CloseFile(myFile);

    Reset(myFile);
    while not Eof(myFile) do
    begin
        readln(myFile, text);
        writeln(text);
    end;
    CloseFile(myFile);

    // Reopen the file for reading
    Reset(myFile);

    // Display the file contents
    while not Eof(myFile) do
    begin
        // Proces one mLineNumber at a time
        writeln('Start of a new mLineNumber :');
        while not Eoln(myFile) do
        begin
            Read(myFile, letter);   // Read and display one letter at a time
            write(letter);
        end;
        ReadLn(myFile, text);
        writeln(text);
    end;

    // Close the file for the last time
    CloseFile(myFile);
end.

{http://www.delphibasics.co.uk/RTL.asp?Name=Eoln}