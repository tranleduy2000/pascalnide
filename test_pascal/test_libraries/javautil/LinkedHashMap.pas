uses JavaData;

var
// Create a hash map
   lhm : JLinkedHashMap;
begin
   // Put elements to the map
   lhm.put('Zara', (3434.34));
   lhm.put('Mahnaz', (123.22));
   lhm.put('Ayan', (1378.00));
   lhm.put('Daisy', (99.22));
   lhm.put('Qadir', (-19.08));
   
   // Get a set of the entries
   Set set = lhm.entrySet();
   
   // Get an iterator
   Iterator i = set.iterator();
   
   // Display elements
   while(i.hasNext()) {
         Map.Entry me = (Map.Entry)i.next();
         System.out.print(me.getKey() + ': ');
         System.out.println(me.getValue());
      }
      System.out.println();
   
   // Deposit 1000 into Zara's account
   double balance = ((Double)lhm.get('Zara')).doubleValue();
   lhm.put('Zara', (balance + 1000));
   System.out.println('Zara's new balance: ' + lhm.get('Zara'));
end.

{See https://www.tutorialspoint.com/java/java_linkedlist_class.htm}