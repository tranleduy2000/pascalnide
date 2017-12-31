# Làm thế nào để viết thư viện cho ứng dụng?

## Một số thứ cần chuẩn bị

 Bạn phải có kiến thức về lập trình ``Java``, có thể ko cần sâu lắm.

## Về Reflection trong Java
Reflection là một kĩ thuật cho phép có thể duyệt và thay đổi các thuộc tính và phuơng thức của một class hoặc một interface tại thời điểm runtime, nó là một kĩ thuật rất mạnh và hữu ích cho các lập trình viên.

Nhờ kĩ thuật này mà Pascal N-IDE có thể gọi runtime các phương thức trong một class

## Các kiểu dữ liệu được chấp nhận

Ứng dụng chỉ chấp nhận các kiểu dữ liệu **trả về** và **tham số của phương thức** bao gồm

``byte, short, int, long, double, float, boolean, StringBuilder, File``

Mình sử dụng ``StringBuilder`` mà ko phải ``String`` là vì chống gc khi thực hiện các phép toán với kiểu dữ liệu này

## Viết thư viện

Truy cập vào đường dẫn này bạn sẽ thấy tất cả các thư viện hiện có của ứng dụng được viết bằng ngôn ngữ Java

https://github.com/tranleduy2000/pascalnide/tree/master/app/src/main/java/com/duy/pascal/backend/lib

Một số thư viện được mình viết lại như giống Free Pascal như các thư viện SysUtils, StrUtils, System, Dos..

Để viết một thư viện mới, bạn cần tạo một ``class`` implements ``com.duy.pascal.backend.lib.PascalLibrary``

Ví dụ mình sẽ tạo một thư viện có tên là ``MathLibrary`` thực hiện các tính toán số học

```java
package com.duy.pascal.backend.lib;

import java.util.Map;

/**
 * Created by Duy on 08-May-17.
 */
public class MathLibrary extends PascalLibrary{
    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }
}
```

Và bây giờ ta muốn có một hàm như trong Pascal, ví dụ hàm ``arccos`` trả vè arccos của x
```pascal
function arccos(x: real): real;
begin
    ...
end.
```

Việc đơn giản là chỉ cần viết một hàm trong java có tên là ``arccos`` và trả về kiểu ``double``
```java
package com.duy.pascal.backend.lib;

import java.util.Map;

/**
 * Created by Duy on 08-May-17.
 */

public class MathLibrary extends PascalLibrary {
    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    /**
     * @return the arc cosine of x value;
     */
    public double arccos(double x) {
        return Math.acos(x);
    }
}
```

Cuối cùng, ta cần thêm annotations ``PascalMethod`` cho phương thức để ứng dụng hiểu là phương thức đó sẽ được gọi
```java
import com.duy.pascal.backend.lib.annotations.PascalMethod;

import java.util.Map;

/**
 * Created by Duy on 08-May-17.
 */

public class MathLibrary extends PascalLibrary {
    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    /**
     * @return the arc cosine of x value;
     */
    @PascalMethod(description = "Return the arc cosine of x value;")
    public double arccos(double x) {
        return Math.acos(x);
    }
}
```

## Đưa thư viện bạn viết vào ứng dụng

Để đưa thư viện vào ứng dụng, bạn tạo một pull request, mình sẽ cập nhật thư viện của bạn
