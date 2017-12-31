# TẠO ÂM THANH NHƯ BÀN PHÍM ĐIỆN THOẠI (DTMF) VỚI PASCAL NIDE. THƯ VIỆN ATONE

## Tổng quan

Thư viện này cung cấp API để phát các âm DTMF (ITU-T Recommendation Q.23)

## Function và procedure

### generateTones:

Tạo âm DTMF cho số điện thoại nhất định.

```pascal
procedure generateTones(number: string; duration: integer);
```

Tham số

* ``number`` chuỗi chỉ chứa các kí tự chữ số
* ``duration``: Thời lượng chạy của từng âm được tính bằng mili giây.
————————–

### generateSound:
Tạo và phát một âm thanh có tần số  freqency trong khoảng thời gian duration (mili giây)

```pascal
procedure generateSound(frequency: integer; duration: integer);
```
Tham số

* ``frequency`` tầng số của âm thanh
* ``duration`` thời gian phát âm thanh (mili giây)

## Code ví dụ

```pascal
uses aTone;
var
    phone: string;
    time: integer;
begin
    writeln('Nhập số điện thoại của bạn: ');
    readln(phone);

    time:= 1000; {1 giây}

    generateTones(phone, time);

    readln;
end.
```
## Video demo

https://youtu.be/t-DQU5hlhgM