Program Example10;

{ Program to demonstrate the cycletorad function. }

Uses math;

begin
    // Should print 1/2
    writeln(cos(cycletorad(1 / 6)));
    // should be sqrt(2)/2
    writeln(cos(cycletorad(1 / 8)));
end.
{http://www.freepascal.org/docs-html/rtl/math/cycletorad.html}