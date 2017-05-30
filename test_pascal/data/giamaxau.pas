const opt = ['*', '+', '(', ')'];
var s: string;

procedure reduce;
var i: integer;
begin
	for i := length(s) downto 2 do
	begin //A2(B3C)2
		case s[i] of 
			'0'..'9': 
				if not (s[i-1] in ['0'..'9']) then insert('*', s, i); //A2
			'(': insert('+', s, i);
			'A'..'Z':
				if not (s[i-1] = '(') then insert('+',s,i);
		end;
	end;
	for i := length(s) - 1 downto 1 do
		if (s[i] in opt) or (s[i+1]  in opt) then insert(' ', s, i + 1);
	s := s + ' ';
	writeln(s);
end;

var stack: array[1..1000] of string;
	top: longint;
procedure push(v: string); begin inc(top); stack[top] := v; end;
function pop: string; begin pop := stack[top]; dec(top); end;
function get: string; begin exit(stack[top]); end;

function next: string;
var res: string;
	i: integer;
begin
	res := '';
	if length(s) > 0 then
	begin
		i := 1;
		while (s[i] <> ' ') and (i <= length(s)) do inc(i);
		res := copy(s, 1, i - 1);
		delete(s, 1, i);
	end;
	exit(res);
end;

function priority(t: string): integer;
begin
	case t[1] of
		'(': exit(0);
		'*': exit(2);
		'+': exit(1);
	end;
end;

var rpn: array[1..1000] of string;
	num: integer;

procedure parse;
var tmp: string;
	i: integer;
begin
	top := 0;
	tmp := next;
	num := 0;
	while length(tmp) > 0 do
	begin
		if tmp[1] in opt then
		begin
			case tmp[1] of 
				')':
					repeat
						tmp := pop;
						if tmp <> '(' then
						begin
							inc(num);
							rpn[num] := tmp;
						end;
					until tmp = '(';
				'(': push(tmp);
				'*', '+': 
					begin
						if top = 0 then push(tmp)
						else
						begin
							while priority(tmp) < priority(get) do
							begin
								inc(num);
								rpn[num] := pop;
							end;
							push(tmp);
						end;
					end;
			end;
		end else 
		begin
			inc(num);
			rpn[num] := tmp;
		end;
		tmp := next;
	end;
	while top > 0 do
	begin
		inc(num);
		rpn[num] := pop;
	end;
	for i := 1 to num do write(rpn[i], ' ');
end;

begin
	readln(s);
	reduce;
	parse;
	{ CBBBB  
	* AA B
	* }
end.
