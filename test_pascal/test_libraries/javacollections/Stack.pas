{https://www.tutorialspoint.com/java/java_stack_class.htm}
uses JavaCollections;

var
  st : JStack;

procedure showpush(st : JStack; a : integer);
begin
  st.push(a);
  writeln('push(' + a + ')');
  writeln('stack : ' + st);
end;

procedure  showpop(st : JStack);
var
  a : integer;
begin
  write('pop -> ');
  cast(a, st.pop());
  writeln(a);
  writeln('stack : ', st);
end;


begin
  writeln('stack : ' + st);
  showpush(st, 42);
  showpush(st, 66);
  showpush(st, 99);
  showpop(st);
  showpop(st);
  showpop(st);
end.
