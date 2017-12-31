unit testdeclarations;

interface

const
  fred_const_1 = 34;
  fred_const_two = 'Fred';
  gimp = 'Glump';
  mo_money = 123.45;
  pigs_can_fly = false;
  fred_name : string = 'Fred!';
  fred_count : integer = 2;
  fred_money : currency = 234.4;
  bit_twiddled : integer = $F00F;
  hex_value : integer = $0BADBEEF;
  my_favorite_letter : char = #96;type
  tstuff = (ethis, ethat, etheother, esomethingelse);

  tstuffset =
      set of tstuff;const
  mystuff = [ethis, etheother];
  otherstuff = [ethat, esomethingelse];var
  fred1 : integer;
  fredtwo : string;
  f3 : boolean;type
  tfredproc = procedure(var psfred : integer)of object;

  tfredfunction = function(const psfred : string) : string;

  tfluggle = array[1..10]of boolean;

  tflig =
        array[1..12]of integer;var
  fred3 : integer = 42;
  myfluggle : tfluggle = (true, false, true, true, false
, true, false, true, true, false);
  myflig : tflig = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);function fnfred(var
                                                                             pifred : integer) : integer;

  function fnfredconst(const psfred : string) : string;