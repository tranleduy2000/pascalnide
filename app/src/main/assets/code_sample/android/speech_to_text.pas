uses aRecognition;
var
    result: string ;
begin
    writeln('Hello, please speak something: ');

    {Wait...}
    result := speechToText;

    {Print to console}
    writeln(result);
    readln;
end.