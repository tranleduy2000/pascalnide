var
  url : java_net_URL;
  a : string;
  conn : java_net_URLConnection;
  br : java_io_BufferedReader;
    is :
  java_io_InputStreamReader;
  inputLine : String;
begin
  a := 'http://vnoi.info/';
  New(url, a);
  conn := url.openConnection();

  New( is, conn.getInputStream());
  New(br, is);

  while inputLine <> null do
  begin
    inputLine := br.readLine();
    writeln(inputLine);
  end;

  br.close();

  readln;
end.
