program classExample;

{$MODE OBJFPC} //directive to be used for creating classes
{$M+} //directive that allows class constructors and destructors
type
   Books = Class
   private
      title : String;
      price : real;
   
   public
      constructor Create(t : String; p : real);
   
   //default constructor
      
      procedure setTitle(t : String);
   
   //sets title for a book
      function getTitle() : String;
   
   //retrieves title
      
      procedure setPrice(p : real);
   
   //sets price for a book
      function getPrice() : real;
   
   //retrieves price
      
      procedure Display();
   
   // display details of a book
   end;
var
   physics, chemistry, maths : Books;

//default constructor
constructor Books.Create(t : String; p : real);
begin
   title := t;
   price := p;
end;

procedure Books.setTitle(t : String); //sets title for a book
begin
   title := t;
end;

function Books.getTitle() : String; //retrieves title
begin
   getTitle := title;
end;

procedure Books.setPrice(p : real); //sets price for a book
begin
   price := p;
end;

function Books.getPrice() : real; //retrieves price
begin
   getPrice := price;
end;

procedure Books.Display();
begin
   writeln('Title: ', title);
   writeln('Price: ', price: 5 : 2);
end;

begin
   physics := Books.Create('Physics for High School', 10);
   chemistry := Books.Create('Advanced Chemistry', 15);
   maths := Books.Create('Algebra', 7);
   
   physics.Display;
   chemistry.Display;
   maths.Display;
end.