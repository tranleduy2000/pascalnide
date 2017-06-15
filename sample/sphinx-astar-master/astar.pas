Unit Astar;

interface
	uses Crt, listesChainees;

	function distance(x1, y1, x2, y2 : Integer) : Integer;
	procedure remove_from_tab(lowestInd : Integer; var openList : ArrayOfNode);
	procedure add_to_tab(toAdd : Node; var tab : ArrayOfNode);
	procedure init_nodes(var nodesList : DoubleArrayOfNode);
	procedure modif_tab(nodeToUp : Node; var tab : ArrayOfNode);
	procedure remove_from_open_list(lowestInd : Integer; var openList : ArrayOfNode; var nodesList : DoubleArrayOfNode);
	function find_path(plateau : ArrayOfInteger; caseFrom : Coordonnees; caseTo : Coordonnees) : ArrayOfNode;
	function get_neighbors(nodesList : DoubleArrayOfNode; currentNode : Node) : ArrayOfNode;

implementation

	{
		int distance(int x1, int y1, int x2, int y2);
		* Retourne la distance de Manhattan d'un point à un autre.
	}
	function distance(x1, y1, x2, y2 : Integer) : Integer;
	begin
		distance := abs(x2 - x1) + abs(y2 - y1);
	end;

	{
		void add_to_tab(Node toAdd, ArrayOfNode *tab);
		* Ajoute un nœud à la liste ouverte.
	}
	procedure add_to_tab(toAdd : Node; var tab : ArrayOfNode);
	var nouvelleTaille, i : Integer; newTab : Array of Node;
	begin
		if( length(tab) <> 0 ) then
		begin
			nouvelleTaille := high(tab) + 2;
			setlength(newTab, nouvelleTaille);

			for i := 0 to high(tab) do
				newTab[i] := tab[i];
				
			finalize(tab);
		end
		else
		begin
			setlength(newTab, 1);
			nouvelleTaille := 1;
		end;
		
		newTab[high(newTab)] := toAdd;
		tab := newTab;
	end;

	{
		void remove_from_tab(int lowestInd, ArrayOfNode *openList);
		* Supprime un nœud dans la liste ouverte.
	}
	procedure remove_from_tab(lowestInd : Integer; var openList : ArrayOfNode);
	var nouvelleTaille, i, i2 : Integer; newOpenList : Array of Node;
	begin
		nouvelleTaille := length(openList) - 1;
		if( nouvelleTaille < 0 ) then
		begin
			finalize(openList);
			exit;
		end;
		setlength(newOpenList, nouvelleTaille);

		i2 := 0;
		for i := 0 to nouvelleTaille - 1 do
		begin
			if( i = lowestInd ) then
				inc(i2);

			newOpenList[i] := openlist[i2];

			inc(i2);
		end;
		finalize(openList);
		
		openList := newOpenList;
	end;

	{
		void modif_tab(Node nodeToUp, ArrayOfNode *tab);
		* Remplace le contenu d'un nœud.
	}
	procedure modif_tab(nodeToUp : Node; var tab : ArrayOfNode);
	var i : Integer;
	begin
		for i := 0 to high(tab) do
			if( (tab[i].x = nodeToUp.x) AND (tab[i].y = nodeToUp.y)) then
			begin
				tab[i] := nodeToUp;
				exit;
			end;
	end;

	{
		void init_nodes(DoubleArrayOfNode *nodesList);
		* Initialise un tableau de nodes.
	}
	procedure init_nodes(var nodesList : DoubleArrayOfNode);
	var i, j : Integer;
	begin
		for i := 0 to high(nodesList) do
		begin
			for j := 0 to high(nodesList[0]) do
			begin
				nodesList[i, j].x := i;
				nodesList[i, j].y := j; 				
				nodesList[i, j].f := 0;
				nodesList[i, j].g := 0; 
				nodesList[i, j].h := 0;
				nodesList[i, j].visited := FALSE;
				nodesList[i, j].closed := FALSE;
				nodesList[i, j].parent.x := -1;
				nodesList[i, j].parent.y := -1;
			end;
		end;
	end;

	{
		void remove_from_open_list(int lowestInd, ArrayOfNode *openList, DoubleArrayOfNode *nodesList);
		* Supprime un nœud de la liste ouverte et le place dans la liste fermée.
	}
	procedure remove_from_open_list(lowestInd : Integer; var openList : ArrayOfNode; var nodesList : DoubleArrayOfNode);
	var caseToRemove : Node;
	begin
		caseToRemove := openList[lowestInd];
		remove_from_tab(lowestInd, openList);
		nodesList[caseToRemove.x, caseToRemove.y].closed := TRUE;
	end;

	{
		ArrayOfNode find_path(ArrayOfInteger plateau, Coordonnees caseFrom, Coordonnees caseTo);
		* Implémentation de l'algorithme A*. Trouve une solution pour aller d'un point From à un point To.
	}
	function find_path(plateau : ArrayOfInteger; caseFrom : Coordonnees; caseTo : Coordonnees) : ArrayOfNode;
	var nodesList : DoubleArrayOfNode;
	openList : ArrayOfNode;
	lowestInd, i, gScore : Integer;
	currentNode, neighbor : Node;
	path, reversedPath, neighbors : Array of Node;
	beenVisited : Boolean;
	begin
		setlength(nodesList, high(plateau) + 1, high(plateau[0]) + 1);
		init_nodes(nodesList);

		openList := NIL;
		reversedPath := NIL;

		add_to_tab(nodesList[caseFrom.x, caseFrom.y], openList);

		while(length(openList) > 0) do
		begin
			//On récupère le nœud de la liste ouverte qui a le coût f le plus faible.
			lowestInd := 0;
			
			for i := 0 to high(openList) do
				if(openList[i].f < openList[lowestInd].f) then
					lowestInd := i;
					
			//Ce nœud devient le nœud courant.
			currentNode := openList[lowestInd];

			{
				Si l'on est arrivé à la case trésor, on recompose le chemin.
				* On commence par retracer l'itinéraire en récupérant les parents successifs, jusqu'à la case de départ.
				* Et on inverse le chemin pour l'avoir dans le bon sens.
			}
			if((currentNode.x = caseTo.x) AND (currentNode.y = caseTo.y))then
			begin
				currentNode := nodesList[currentNode.x, currentNode.y];
				while(((currentNode.parent.x) <> -1) AND ((currentNode.parent.y) <> -1)) do
				begin
					add_to_tab(currentNode, reversedPath);
					currentNode := nodesList[currentNode.parent.x, currentNode.parent.y];
				end;

				//Le chemin est à l'envers, on le retourne.
				setlength(path, high(reversedPath) + 1);
				writeln(high(reversedPath));
			
				for i := 0 to high(reversedPath) do
				begin
					path[high(reversedPath) - i] := reversedPath[i];
				end;
				 
				exit(path);
			end;

			{
				Cas normal :
				* On met le nœud actuel dans la liste fermée
				* Et on vérifie les nœuds voisins.
			}
			remove_from_open_list(lowestInd, openList, nodesList);
			currentNode.closed := true;

			neighbors := get_neighbors(nodesList, currentNode);

			for i := 0 to high(neighbors) do
			begin
				neighbor := neighbors[i];

				if((neighbor.closed) OR (plateau[neighbor.x, neighbor.y] = 1) ) then
				 //Le nœud n'est pas valide (déjà dans la liste fermé ou mur), on passe au nœud suivant.
				 continue;

				//le coût G est la distance entre le nœud de départ et le nœud actuel.
				gScore := currentNode.g + 1; //auquel on ajoute 1 (la distance entre le nœud actuel et le nœud voisin)

				beenVisited := neighbor.visited;

				if(NOT(neighbor.visited) OR (gScore < neighbor.g)) then
				begin
					//Le nouveau chemin vers le voisin est le meilleur. On enregistre les nouvelles informations.
					neighbor.visited := true;
					neighbor.parent.x := currentNode.x;
					neighbor.parent.y := currentNode.y;
					neighbor.h := neighbor.h OR distance(neighbor.x, neighbor.y, caseTo.x, caseTo.y);
					neighbor.g := gScore;
					neighbor.f := neighbor.g + neighbor.h;

					if(NOT(beenVisited)) then
					begin
						//Le nœud n'a pas été visité, on l'ajoute à la liste ouverte.
						add_to_tab(neighbor, openList);
						nodesList[neighbor.x, neighbor.y] := neighbor;
					end
					else
					begin
						//Le nœud appartient déjà la liste ouverte, on le met à jour.
						modif_tab(neighbor, openList);
						nodesList[neighbor.x, neighbor.y] := neighbor;
					end;
				end;
			end;
		end;
		//Aucun chemin trouvé.
		find_path := NIL;
	end;

	{
		ArrayOfNode get_neighbors(DoubleArrayOfNode plateau, Node currentNode);
		* Retourne la liste des voisins d'une case.
	}
	function get_neighbors(nodesList : DoubleArrayOfNode; currentNode : Node) : ArrayOfNode;
	var x, y : Integer; casesVoisines : Array of Node;
	begin
		x := currentNode.x;
		y := currentNode.y;

		if(x - 1 >= 0) then
		begin
			add_to_tab(nodesList[x - 1, y], casesVoisines);
		end;
		
		if(x + 1 <= high(nodesList[0])) then
		begin
			add_to_tab(nodesList[x + 1, y], casesVoisines);
		end;
		
		if(y - 1 >= 0) then
		begin
			add_to_tab(nodesList[x, y - 1], casesVoisines);
		end;

		if(y + 1 <= high(nodesList)) then
		begin
			add_to_tab(nodesList[x, y + 1], casesVoisines);
		end;
		
		get_neighbors := casesVoisines;
   end;

begin
     {Corps du module astar}
end.
