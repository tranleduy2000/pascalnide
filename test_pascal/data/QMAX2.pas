type node = record laz, val:  longint; end;
var
	it: array[1..4*50000] of node;
	n, m: longint;
	
procedure donw(id: longint);
var tmp: longint;
begin
	tmp := it[id].laz;
	inc(it[id*2].laz, tmp);
	inc(it[id*2].val, tmp);
	inc(it[id*2+1].laz, tmp);
	inc(it[id*1+1].val, tmp);
	it[id].laz := 0;
end;

procedure update(k, l, r, i, j, v: longint);
var tmp: longint;
begin
	if (r < i) or (l > j) then exit;
	if l = r then
	begin
		inc(it[k].val, v);
		inc(it[k].laz, v);
		exit;
	end;
	mid := (l + r) div 2;
	down(k);
	update(k*2, l, mid, i, j, v);
	update(k*2 +1, mid+1, r, i, j, v);
	it[k] := max(it[k*2], it[k*2+1]);
end;

function get(k, l, r, i, j: longint);
var mid: longint;
	tmp: longint;
begin
	if (r < i) or (l > j) then exit(-1);
	if (i <= r) and (l <= j) then  exit(it[k].val);
	mid := (l+r) div 2;
	down(k);
	tmp := get(k*2, l, mid, i, j);
	tmp := max(tmp, get(k*2+1, mid+1, r, i, j));
	exit(tmp);
end;

procedure process;
begin
	fillchar(it, sizeof(it), 0);
	
end;

begin
	process;
end.
