program exRecords;
type
    Books = record
        title: string;
        author: string;
        subject: string;
        book_id: longint;
    end;

var
    Book1, Book2: Books; (* Declare Book1 and Book2 of type Books *)

(* procedure declaration *)
procedure printBook( var book: Books );

begin
    (* print Book info *)
    writeln ('Book  title : ', book.title);
    writeln('Book  author : ', book.author);
    writeln( 'Book  subject : ', book.subject);
    writeln( 'Book book_id : ', book.book_id);
end;

begin
    (* book 1 specification *)
    Book1.title := 'C Programming';
    Book1.author := 'Nuha Ali ';
    Book1.subject := 'C Programming Tutorial';
    Book1.book_id := 6495407;

    (* book 2 specification *)
    Book2.title := 'Telecom Billing';
    Book2.author := 'Zara Ali';
    Book2.subject := 'Telecom Billing Tutorial';
    Book2.book_id := 6495700;

    (* print Book1 info *)
    printbook(Book1);
    writeln;

    (* print Book2 info *)
    printbook(Book2);
end.