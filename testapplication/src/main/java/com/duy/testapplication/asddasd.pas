uses aTTSpeech, {Thư viện chuyển văn bản thành giọng nói}
    aRecognition; {Thư viện chuyển giọng nói thành văn bản}
var
    name: string; {hỗ trợ UTF-8}
begin
    {In ra màn hình}
    writeln('Tên của bạn là gì? ');

    speak('Tên của bạn là gì');

    {Đợi khoảng vài giây, có thể do điều kiện mạng mà
    ứng dụng sẽ đọc nhanh hay chậm, các bạn điểu chỉnh thời
    gian tạm dừng để được như ý muốn}
    delay(3000);

    name := speechToText;
    {Ứng dụng sẽ hiện lên hộp thoại}

    writeln('Xin chào ' + name);
    speak('Xin chào ' + name);
    delay(3000);

    readln;
    stopSpeak(); {dừng đọc nếu còn đang đọc}
end.
