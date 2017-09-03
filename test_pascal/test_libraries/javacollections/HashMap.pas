{https://www.tutorialspoint.com/java/java_hashmap_class.htm}
uses JavaCollections;

var
    // Create a hash map
  map : JHashMap;
  entries : JSet;
  i : JIterator;
  entry : JEntry;
  balance : Double;
begin
  // Put elements to the map
  map.put('Zara', (3434.34));
  map.put('Mahnaz', (123.22));
  map.put('Ayan', (1378.00));
  map.put('Daisy', (99.22));
  map.put('Qadir', (-19.08));

  // Get a set of the entries
  entries := map.entrySet();

  // Get an iterator
  i := entries.iterator();
  writeln(i);

  // Display elements
  while(i.hasNext()) do
  begin
    cast(entry, i.next());
    write(entry.getKey(), ' : ');
    writeln(entry.getValue());
  end;

  // Deposit 1000 into Zara's account
  writeln(map.get('Zara'));

  cast(balance, map.get('Zara'));
  map.put('Zara', (balance + 1000));
  writeln('Zara''s new balance : ', map.get('Zara'));
end.
