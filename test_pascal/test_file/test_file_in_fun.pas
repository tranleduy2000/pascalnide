program addFiledata;
const
    MAX = 4;
type
    raindata = text;

var
    rainfile: raindata;
    filename: string;
procedure writedata(var f: raindata);

var
    data: real;
    i: integer;

begin
    rewrite(f, sizeof(data));
    for i:=1 to MAX do
    begin
        writeln('Enter rainfall data: ');
        readln(data);
        writeln(f, data);
    end;
    close(f);
end;

procedure computeAverage(var x: raindata);
var
    d, sum: real;
    average: real;

begin
    reset(x);
    sum := 0.0;
    while not eof(x) do

    begin
        read(x, d);
        sum := sum + d;
    end;

    average := sum / MAX;
    close(x);
    writeln('Average Rainfall: ', average:7:2);
end;

begin
    writeln('Enter the File Name: ');
    readln(filename);
    assign(rainfile, filename);
    writedata(rainfile);
    computeAverage(rainfile);
end.