program exRecords;
type
    BooksPtr = ^Books;
    Books = record
        title: string;
        author: string;
        subject: string;
        book_id: longint;
    end;

var
        (* Declare Book1 and Book2 of pointer type that refers to Book type *)
    Book1, Book2: BooksPtr;

begin
    new(Book1);
    new(book2);

    (* book 1 specification *)
    Book1^.title := 'C Programming';
    Book1^.author := 'Nuha Ali ';
    Book1^.subject := 'C Programming Tutorial';
    Book1^.book_id := 6495407;

    (* book 2 specification *)
    Book2^.title := 'Telecom Billing';
    Book2^.author := 'Zara Ali';
    Book2^.subject := 'Telecom Billing Tutorial';
    Book2^.book_id := 6495700;

    (* print Book1 info *)
    writeln ('Book 1 title : ', Book1^.title);
    writeln('Book 1 author : ', Book1^.author);
    writeln( 'Book 1 subject : ', Book1^.subject);
    writeln( 'Book 1 book_id : ', Book1^.book_id);

    (* print Book2 info *)
    writeln ('Book 2 title : ', Book2^.title);
    writeln('Book 2 author : ', Book2^.author);
    writeln( 'Book 2 subject : ', Book2^.subject);
    writeln( 'Book 2 book_id : ', Book2^.book_id);

    dispose(Book1);
    dispose(Book2);
end.