const
	opt = ['+', '-', '*', '/', '(' , ')'];
var
	stack: array[1..10000] of string;
	c, top: integer;
	s: string;
	result: string;

function pop: string;
begin
	pop := stack[top];
	dec(top);
end;

procedure push(v: string);
begin
	inc(top);
	stack[top] := v;
	writeln('push ' +v );
end;

function get: string;
begin exit(stack[top]); end;

function nextToken: string;
var res: string;
begin
	res := '';
	while (s[c] <> ' ') and (c <= length(s)) do
	begin
		res := res + s[c];
		inc(c);
	end;
	inc(c);
	exit(res);
end;

function getPriority(token: string): integer;
begin
	case token of 
		'+', '-': exit(1);
		'*', '/': exit(2);
		'(': exit(0);
	end;
end;

procedure parse;
var t: string;
	tmp: string;
begin
	t := nextToken;
	while t <> '' do
	begin
		writeln('token: ', t);
		if t[1] in opt then
		begin
			case t of
				'(': push(t);
				')':
					repeat
						tmp := pop;
						if tmp <> '(' then
							result := result + ' '+ tmp;
					until tmp = '(';
				else 
					begin			
						if top = 0 then push(t)
						else
						begin
							while (top > 0) and (getPriority(get) >= getPriority(t)) do
							begin
								result := result + ' ' + pop;	
							end;
							push(t);
						end;
					end;
			end;
		end
		else result := result + ' ' + t;
		t := nextToken;
	end;
	
	while top > 0 do result := result + ' ' + pop;
	writeln('result :', result);
end;

procedure calc;
begin
end;

procedure reduce;
var i: integer;
begin
	for i := length(s) -1 downto 1 do
	begin
		if (s[i] in opt) or (s[i+1] in opt) then 
			insert(' ', s, i+1);
	end;
	for i := length(s) - 1 downto 1 do
		if (s[i] = ' ') and (s[i+1] = ' ') then delete(s, i, 1);
	s := s + ' ';
	writeln('reduce :', s);
end;

begin
	readln(s);
	c := 1;
	reduce;
	parse;
	calc;
end.
