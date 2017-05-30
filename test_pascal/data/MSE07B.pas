const
	maxn = 10000000;
type node = record val, id: longint; end;
var
	heapmin, heapmax: array[1..maxn] of node;
	posMin, posMax: array[1..maxn] of longint;
	nmin, nmax: longint;
	
procedure swap(var a, b: node);
var
	tmp: node;
begin
	tmp := a;
	a := b;
	b := tmp;
end;

procedure upheapmin(i: longint);
begin
	if i = 1 then exit;
	if heapmin[i div 2].val > heapmin[i].val then
	begin
		posMin[heapmin[i].val] := i div 2;
		posMin[heapmin[i div 2].val] := i;
		swap(heapmin[i], heapmin[i div 2]);
		upheapmin(i div 2);
	end;
end;

procedure upheapmax(i: longint);
begin
	if i = 1 then exit;
	if heapmax[i div 2].val < heapmax[i].val then
	begin
		posMax[heapmax[i].val] := i div 2;
		posMax[heapmax[i div 2].val] := i;
		swap(heapmax[i], heapmax[i div 2]);
		upheapmax(i div 2);
	end;
end;

procedure downheapmin(i: longint);
var
	c: longint;
begin
	c := i * 2;
	if c > nmin then exit;
	if (c < nmin) and (heapmin[c].val > heapmin[c+1].val)  then inc(c);
	if heapmin[i].val > heapmin[c].val then
	begin
		posMin[heapmin[i].val] := c;
		posMin[heapmin[c].val] := i;
		swap(heapmin[c], heapmin[i]);
		downheapmin(c);
	end;
end;

procedure downheapmax(i: longint);
var
	c: longint;
begin
	c := i * 2;
	if c> nmax then exit;
	if (c < nmax) and (heapmax[c+1].val > heapmax[c].val) then inc(c);
	if heapmax[i].val < heapmax[c].val then
	begin
		posMax[heapmax[i].val] := c;
		posMax[heapmax[c].val] := i;
		swap(heapmax[i], heapmax[c]);
		downheapmax(c);
	end;
end;

function popheapmax(i: longint): node;
begin
	popheapmax := heapmax[i];
	posMax[heapmax[nmax].val] := i;
	heapmax[i] := heapmax[nmax];
	dec(nmax);
	upheapmax(i);
	downheapmax(i);
end;

function popheapmin(i: longint): node;
begin
	popheapmin := heapmin[i];
	posMin[heapmin[nmin].val] := i;
	heapmin[i] := heapmin[nmin];
	dec(nmin);
	upheapmin(i);
	downheapmin(i);
end;

procedure push(id, val: longint);
var
	tmp: node;
begin
	tmp.id := id;
	tmp.val := val;
	inc(nmax);
	heapmax[nmax] := tmp;
	posMax[val] := nmax;
	inc(nmin);
	heapmin[nmin] := tmp;
	posMin[val] := nmin;
	upheapmax(nmax);
	upheapmin(nmin);
end;

procedure process;
var
	i, t, j, id, val: longint;
	tmp: node;
	f: text;
begin
	assign(f, 'file.inp');
	reset(f);
	repeat
		read(f, i);
		if i = 1 then
		begin
			readln(f, id, val);
			push(id, val);
		end else if i = 2 then
		begin
			if nmin = 0 then writeln(0)
			else 
			begin
				tmp := popheapmax(1);
				writeln(tmp.id);
				popheapmin(posMin[tmp.val]);
			end;
		end else if i = 3 then
		begin
			if nmin = 0 then writeln(0)
			else 
			begin
				tmp := popheapmin(1);
				writeln(tmp.id);
				popheapmax(posMax[tmp.val]);
			end;
		end; 
	until i = 0;
	close(f);
end;

begin
	process;
end.
