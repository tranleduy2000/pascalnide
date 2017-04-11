Program Example32;

{ Program to demonstrate the Inc function. }

var
    C : Cardinal = 1;
    L : Longint = 1;
    I : Integer = 1;
    W : Word = 1;
    B : Byte = 1;
    SI : ShortInt = 1;
    CH : Char = 'A';

begin
    Inc (C);     { C:=2    }
    Inc (L,5);   { L:=6    }
    Inc (I,-3);  { I:=-2   }
    Inc (W,3);   { W:=4    }
    Inc (B,100); { B:=101  }
    Inc (SI,-3); { Si:=-2  }
    Inc (CH,1);  { ch:='B' }
end.
{http://www.freepascal.org/docs-html/rtl/system/inc.html}