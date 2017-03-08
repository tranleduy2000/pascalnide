**Phần mềm lập trình Pascal trên android**
Bạn muốn viết chương trình pascal nhưng không phải lúc nào cũng mạng máy tính theo bên mình, ứng dụng Pascal N-IDE sẽ giúp bạn thực hiện điều đó. Bạn có thể viết các chương trình Pascal đơn giản với phần mềm này.

CÁC TÍNH NĂNG IDE HỖ TRỢ

- Biên dịch và thực thi chương trình.
- Báo lỗi khi biên dịch
- Editor mạnh mẽ với nhiều tính năng thông minh:
Highlight code: làm nổi bật các từ khóa
Auto suggest: hiển thị cửa sổ nhỏ gợi ý các từ trùng với từ đang gõ
Undo/redo: Hỗ trợ undo, redo khi gõ.
Auto format: tự động định dạng lại code để dễ nhìn hơn.
Find/Find and replace: tìm kiếm và thay thế, có hỗ trợ regex.

=================================

Các hàm và phương thức được hỗ trợ giống như Turbo pascal. Tuy nhiên cần một số lưu ý như sau:
- FUNCTION: để gán giá trị cho hàm cần dùng từ khóa 
result, không dùng tên hàm để gán
- USES: không sử dụng từ khóa này vào chương trình, IDE sẽ tự động thêm các thư viện CRT, MATH, DOS, SYSTEM, GRAPH.
- READLN: chỉ hỗ trợ nhập vào 1 biến

=================================
KEY WORD
program, begin, end, procedure, function

DECLARE KEY WORD
const, var, type, array, record

SUPPORT SOME FUNCTION
readln(single variable); {important, "readln" only support single variable}

BASIC VARIABLE TYPE
char, integer, real, boolean, string, longint, byte, word, extended

BOOLEAN OPERATOR
and, or, xor, not

RELATION OPERATORS
<, >, =, <>, <=, >=

ARTHMETIC OPERATOR
+, -, *, /, div, mod, shl, shr

LOOP
while ... do ...
for ... to ... do ...
for ... downto ... do ...
repeat ... until ...
case ...

DECISION MARKING
if ... then ...
if ... then ... else ...