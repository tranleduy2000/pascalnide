# TẠO ỨNG DỤNG ĐỌC CHỮ VỚI PASCAL N-IDE

## Giới thiệu sơ lược về chuyển văn bản thành giọng nói (TTS):

> Text-to-speech (TTS) gọi nôm na là tính năng chuyển văn bản thành giọng nói, được phát triển từ rất lâu trước đây. Có thể nói khởi nguồn của nó là năm 1779 khi mà nhà khoa học người Đan Mạch Christian Kratzenstein, lúc đó làm việc tại Viện Hàn lâm Khoa học Nga, xây dựng một mô hình có thể bắt chước giọng nói người với năm nguyên âm. Từ đó đến nay, qua nhiều giai đoạn phát triển thì công nghệ tổng hợp giọng nói đã có những bước phát triển vượt bậc, giọng nói đã tự nhiên hơn, dễ nghe hơn. Tuỳ vào công nghệ của từng nhà phát triển mà cho ra những giọng nói với chất lượng khác nhau.

Đầu tiên để viết một chương trình chuyển văn bản thành giọng nói bạn cần khai báo thư viện ``aSpeech`` bằng từ khóa ``uses``

```pascal
uses aTTSpeech;
```

# Code mẫu:
Chương trình thực hiện chuyển một chuỗi do người dùng nhập vào và phát thành tiếng

```pascal
uses aTTSpeech; {Android Text to speech}
var
    input: String;
begin
    {Hãy bật mạng, mic và cho phép ứng dụng sử dụng loa}
    writeln('Nhập văn bản cần đọc: ');
    readln(input);

    {Chuyển văn bản thành giọng nói}
    speak(input);

    readln;

    {Dừng đọc}
    stopSpeak();
end.
```