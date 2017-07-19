# Làm thế nào để khởi tạo một object mới

## Khai báo
```pascal
Var
    <VariableName>: <ClassName>;
```
Trong đó
* ``VariableName`` là tên của object được đặt theo đúng quy tắc trong pascal

* ``ClassName`` là tên của class, cần lưu ý phải khai báo chính xác tên của class, thay dấu . bằng dấu _ khi truy cập các package class

Ví dụ
```pascal
Var
    m: java_lang_Math;
```
Hoặc
```pascal
Var
    list: java_util_ArrayList;
```

## Khởi tạo

Chúng ta sẽ sử dụng hàm ``new`` để khởi tạo một ``Object``

Lưu ý, không thể khởi tạo một ``final class``.

Cú pháp:
```pascal
    new(object);
```
Hoặc
```pascal
    new(object, list_parameters);
```

Trong đó ``list_parameters`` là danh sách các tham số để khởi tạo object

Ví dụ
```pascal

Var
    list: java_util_ArrayList;
Begin
    New(list);
End.
```
Hay
```pascal
var
    sk: java_net_Socket;
    IpAddress: string;
    Port: integer;
begin
    ipaddress := '192.168.1.1';
    port := 80;
    {Socket sk = new Socket (ipAddress, port)}
    New(sk, ipAddress, port);
end.
```

## Gọi một phương thức của Object

Rất đơn giản, nó giống như ``java``

Ví dụ ta cần gọi hàm ``isConnected`` của class ``Socket``
```pascal
var
    sk: java_net_Socket;
    ipAddress: string;
    Port: integer;
begin
    ipaddress := '192.168.1.1';
    port := 80;
    New(sk, ipAddress, port);
    writeln(sk);
    writeln(sk.isConnected());
end.
```

## Tạo một custom class

Hiện tại ứng dụng chưa hỗ trợ
