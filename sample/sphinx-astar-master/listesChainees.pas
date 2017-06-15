Unit listesChainees;

interface
	//Déclaration des structures pour tout le programme
	Type
	Coordonnees = record
		x : Integer;
		y : Integer;
	end;

	Contraintes = record
		haut : Boolean;
		bas : Boolean;
		gauche : Boolean;
		droite : Boolean;
	end;
	
	PCellule = ^Cellule;
	Cellule = record
		coordonneesCase : Coordonnees;
		contraintesCase : Contraintes; 
		precedent, suivant : PCellule;   
	end;
	
	Node = record
		f, g, h : Integer;
		x, y : Integer;
		visited, closed : Boolean;
		parent : Coordonnees;
	end;

	//Déclaration des types supplémentaires
	ArrayOfInteger = Array of Array of Integer;
	ArrayOfNode = Array of Node;
	DoubleArrayOfNode = Array of Array of Node;
	
	procedure ajouter_item(var liste: PCellule; x, y: Integer; var derniereCase : PCellule);
	procedure remove_item_end(liste: PCellule; var derniereCase : PCellule);
	procedure display_list(liste: PCellule);
	procedure clear_list(liste: PCellule);

implementation

	{
		procedure ajouter_item(var liste: PCellule; x, y: Integer);
		* Ajoute un item en fin de liste
	}
	procedure ajouter_item(var liste: PCellule; x, y: Integer; var derniereCase : PCellule);
	var p: PCellule;
	begin
		new(p);
		p^.coordonneesCase.x := x;
		p^.coordonneesCase.y := y;
		p^.suivant := liste;
		p^.suivant^.precedent := p;
		derniereCase := p;
		liste := p;
	end;
	
	{
		procedure remove_item_end(liste: PCellule);
		* Supprime un item en fin de liste
	}
	procedure remove_item_end(liste: PCellule; var derniereCase : PCellule);
	var p, r: PCellule;
	begin
		p := liste;
		if( p^.suivant <> NIL ) then//L'autre cas ne nous intéresse pas.
		begin
			while (p <> nil) do
			begin
				r := p^.suivant;
				if r = NIL then
				begin
					p^.precedent^.suivant := NIL;
					
					derniereCase := p^.precedent;
					dispose(p);
				end;
				 p := r;
			end;
		end;
	end;

	{
		procedure display_list(liste : PCellule);
		* Affiche le contenu d'une liste
	}
	procedure display_list(liste : PCellule);
	var p: PCellule;
	begin
		p := liste;
		while (p <> nil) do
		begin
			write('[', p^.coordonneesCase.x,', ', p^.coordonneesCase.y, '] ; ');
			p := p^.suivant;
		end;
		writeln;
	end;

	{
		procedure clear_list(liste: PCellule);
		* Vide une liste de cases de son contenu
	}
	procedure clear_list(liste: PCellule);
	var p, r: PCellule;
	begin
		p := liste;
		while (p <> nil) do
		begin
			r := p^.suivant;
			dispose(p);
			p := r;
		end;
	end;

begin
     {Corps du module listesChainees}
end.
