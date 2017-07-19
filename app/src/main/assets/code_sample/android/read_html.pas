uses
    aHtml;
var
    result: string;
begin
    result := getContentHtml('https://www.google.com.vn/');
    writeln(result);
end.
