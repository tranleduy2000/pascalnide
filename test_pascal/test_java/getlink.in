var
  url : java_net_URL;
  a : string;
  conn : java_net_URLConnection;
  br : java_io_BufferedReader;
  inputStream : java_io_InputStreamReader;
  line : String;
begin
  a := 'http://vnoi.info/wiki/Home';
  New(url, a);
  conn := url.openConnection();

  New(inputStream, conn.getInputStream());
  New(br, inputStream);

  while line <> null do
  begin
    line := br.readLine();
    writeln(line);
  end;

  br.close();
  readln;
end.
