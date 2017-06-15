Unit IA;

interface
	uses listesChainees, joueur, astar, Crt;

	function case_arrivee(plateau : ArrayOfInteger) : Coordonnees;
	function generer_plateau(hauteur, largeur, nombreDeMurs : Integer) : ArrayOfInteger;
	function verif_cases_adjacentes(plateau : ArrayOfInteger; x, y : Integer; coordonneesCase : Coordonnees) : Boolean;
	procedure tour_IA(var IAGagne : Boolean; var plateauJoueurIA : ArrayOfInteger; var caseActuelleIA, caseArriveeIA : Coordonnees);

implementation

	{
		Coordonnees case_arrivee(ArrayOfInteger plateau);
		* Renvoie les coordonnées de la case trésor du plateau envoyé en paramètre.
	}
	function case_arrivee(plateau : ArrayOfInteger) : Coordonnees;
	var i, j : Integer; caseArrivee : Coordonnees;
	begin
		for i := 0 to high(plateau) do
		begin
			for j := 0 to high(plateau[0]) do
			begin
				if( plateau[i, j] = 3) then
				begin
					caseArrivee.x := i;
					caseArrivee.y := j;
					exit(caseArrivee);
				end;
			end;
		end;
	end;

	{
		function verif_cases_adjacentes(plateau, x, y : Integer; coordonneesCase : Coordonnees) : Boolean;
		* Vérifie que les cases ajacentes à la case dont les coordonnées sont les deux premiers paramètres ne sont pas des cases vides ou l'entrée (sauf la case indiquée en 4ème paramètre)
	}

	function verif_cases_adjacentes(plateau : ArrayOfInteger; x, y : Integer; coordonneesCase : Coordonnees) : Boolean;
	begin
		if( ((x - 1) < 0) OR (x - 1 = coordonneesCase.x) OR ((plateau[x - 1, y] <> 0) AND (plateau[x - 1, y] <> 2)) ) then//Haut
		begin
			if( ((x + 1) > high(plateau)) OR (x + 1 = coordonneesCase.x) OR ((plateau[x + 1, y] <> 0) AND (plateau[x + 1, y] <> 2)) ) then//Bas
			begin
				if( ((y - 1) < 0) OR (y - 1 = coordonneesCase.y) OR ((plateau[x, y - 1] <> 0) AND (plateau[x, y - 1] <> 2)) ) then//Gauche
				begin
					if( ((y + 1) > high(plateau[0])) OR (y + 1 = coordonneesCase.y) OR ((plateau[x, y + 1] <> 0) AND (plateau[x, y + 1] <> 2)) ) then//Droite
					begin
						verif_cases_adjacentes := TRUE;
					end
					else
						verif_cases_adjacentes := FALSE;
				end
				else
					verif_cases_adjacentes := FALSE;
			end
			else
				verif_cases_adjacentes := FALSE;
		end
		else
			verif_cases_adjacentes := FALSE;
	end;

	{
		function generer_plateau(hauteur, largeur, nombreDeMurs : Integer) : ArrayOfInteger;
		* Génère un plateau à partir des paramètres renseignés
	}
	function generer_plateau(hauteur, largeur, nombreDeMurs : Integer) : ArrayOfInteger;

	var plateau : Array of Array of Integer;
	i, j, nbDeMursRestant, direction : Integer;
	listeCases,	caseActuelle : PCellule;

	Const HAUT = 1; BAS = 2; GAUCHE = 3; DROITE = 4;

	begin
		{
			* Création et initialisation du plateau 
		}
		setlength(plateau, hauteur, largeur);

		for i := 0 to high(plateau) do
		begin
			for j := 0 to high(plateau[i]) do plateau[i,j] := 1;
		end;

		{
			* Génération du labyrinthe 
		}
		
		//Initialisation de la liste des cases composant le chemin actuel
		new(listeCases);
		listeCases^.coordonneesCase.x := random(high(plateau) + 1);
		listeCases^.coordonneesCase.y := random(high(plateau[0]) + 1);
		listeCases^.contraintesCase.haut := FALSE;
		listeCases^.contraintesCase.bas := FALSE;
		listeCases^.contraintesCase.gauche := FALSE;
		listeCases^.contraintesCase.droite := FALSE;
		listeCases^.precedent := NIL;
		listeCases^.suivant := NIL;
		caseActuelle := listeCases;

		//On commence par choisir une case de départ au hasard
		plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] := 2;

		nbDeMursRestant := hauteur * largeur - 1;

		while(nbDeMursRestant > nombreDeMurs) do
		begin
			if( NOT(caseActuelle^.contraintesCase.haut) OR NOT(caseActuelle^.contraintesCase.bas) OR NOT(caseActuelle^.contraintesCase.gauche) OR NOT(caseActuelle^.contraintesCase.droite) ) then
			begin
				//Génération de la direction
				direction := random(4) + 1;

				{
					On va maintenant vérifier que la direction est correcte, c'est-à-dire :
					* qu'il n'existe pas déjà une contrainte qui interdise le déplacement
					* que l'on ne rencontre pas une des limites du plateau
					* que la case demandée ne soit pas déjà attribuée à la case départ
					* que l'une des cases adjacentes à la case demandée n'est pas déjà rattachée au labyrinthe (sauf la case en cours bien entendu) 
				}
				case direction of
					HAUT :
					begin
						if( NOT(caseActuelle^.contraintesCase.haut) ) then
						begin
							if( caseActuelle^.coordonneesCase.x - 1 >= 0 ) then
							begin
								if( plateau[caseActuelle^.coordonneesCase.x - 1, caseActuelle^.coordonneesCase.y] <> 2 ) then
								begin
									if( verif_cases_adjacentes(plateau, caseActuelle^.coordonneesCase.x - 1, caseActuelle^.coordonneesCase.y, caseActuelle^.coordonneesCase) ) then
									begin
										//Toutes les conditions requises sont remplies, on ajoute donc la case à l'historique et on remplace le contenu de la case actuelle par un vide.
										ajouter_item(listeCases, caseActuelle^.coordonneesCase.x - 1, caseActuelle^.coordonneesCase.y, caseActuelle);
										if( plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] <> 0 ) then
										begin
											plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] := 0;
											nbDeMursRestant := nbDeMursRestant - 1;
										end;
									end
									else
										caseActuelle^.contraintesCase.haut := TRUE;//Si l'une des cases adjacentes à la case demandée est déjà rattachée au labyrinthe (sauf la case en cours), on bloque cette direction pour le prochain tour de boucle.
								end
								else
									caseActuelle^.contraintesCase.haut := TRUE;//Si la case demandée est déjà occuppée par l'entrée, on bloque la direction.
							end
							else
								caseActuelle^.contraintesCase.haut := TRUE;//Si l'on voit que l'on a rencontré la limite haute du plateau, on bloque cette direction pour le prochain tour de boucle.
						end;
					end;
					BAS :
					begin
						if( NOT(caseActuelle^.contraintesCase.bas) ) then
						begin
							if( caseActuelle^.coordonneesCase.x + 1 <= high(plateau) ) then
							begin
								if( plateau[caseActuelle^.coordonneesCase.x + 1, caseActuelle^.coordonneesCase.y] <> 2 ) then
								begin
									if( verif_cases_adjacentes(plateau, caseActuelle^.coordonneesCase.x + 1, caseActuelle^.coordonneesCase.y, caseActuelle^.coordonneesCase) ) then
									begin
										//Toutes les conditions requises sont remplies, on ajoute donc la case à l'historique et on remplace le contenu de la case actuelle par un vide.
										ajouter_item(listeCases, caseActuelle^.coordonneesCase.x + 1, caseActuelle^.coordonneesCase.y, caseActuelle);
										if( plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] <> 0 ) then
										begin
											plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] := 0;
											nbDeMursRestant := nbDeMursRestant - 1;
										end;
									end
									else
										caseActuelle^.contraintesCase.bas := TRUE;//Si l'une des cases adjacentes à la case demandée est déjà rattachée au labyrinthe (sauf la case en cours), on bloque cette direction pour le prochain tour de boucle.
								end
								else
									caseActuelle^.contraintesCase.bas := TRUE;//Si la case demandée est déjà occuppée par l'entrée, on bloque la direction.
							end
							else
								caseActuelle^.contraintesCase.bas := TRUE;//Si l'on voit que l'on a rencontré la limite basse du plateau, on bloque cette direction pour le prochain tour de boucle.
						end;
					end;
					GAUCHE :
					begin
						if( NOT(caseActuelle^.contraintesCase.gauche) ) then
						begin
							if( caseActuelle^.coordonneesCase.y - 1 >= 0 ) then
							begin
								if( plateau[caseActuelle^.coordonneesCase.x, caseActuelle^.coordonneesCase.y - 1] <> 2 ) then
								begin
									if( verif_cases_adjacentes(plateau, caseActuelle^.coordonneesCase.x, caseActuelle^.coordonneesCase.y - 1, caseActuelle^.coordonneesCase) ) then
									begin
										//Toutes les conditions requises sont remplies, on ajoute donc la case à l'historique et on remplace le contenu de la case actuelle par un vide.
										ajouter_item(listeCases, caseActuelle^.coordonneesCase.x, caseActuelle^.coordonneesCase.y - 1, caseActuelle);
										if( plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] <> 0 ) then
										begin
											plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] := 0;
											nbDeMursRestant := nbDeMursRestant - 1;
										end;
									end
									else
										caseActuelle^.contraintesCase.gauche := TRUE;//Si l'une des cases adjacentes à la case demandée est déjà rattachée au labyrinthe (sauf la case en cours), on bloque cette direction pour le prochain tour de boucle.
								end
								else
									caseActuelle^.contraintesCase.gauche := TRUE;//Si la case demandée est déjà occuppée par l'entrée, on bloque la direction.
							end
							else
								caseActuelle^.contraintesCase.gauche := TRUE;//Si l'on voit que l'on a rencontré la limite gauche du plateau, on bloque cette direction pour le prochain tour de boucle.
						end;
					end;
					DROITE :
					begin
						if( NOT(caseActuelle^.contraintesCase.droite) ) then
						begin
							if( caseActuelle^.coordonneesCase.y + 1 <= high(plateau[0]) ) then
							begin
								if( plateau[caseActuelle^.coordonneesCase.x, caseActuelle^.coordonneesCase.y + 1] <> 2 ) then
								begin
									if( verif_cases_adjacentes(plateau, caseActuelle^.coordonneesCase.x, caseActuelle^.coordonneesCase.y + 1, caseActuelle^.coordonneesCase) ) then
									begin
										//Toutes les conditions requises sont remplies, on ajoute donc la case à l'historique et on remplace le contenu de la case actuelle par un vide.
										ajouter_item(listeCases, caseActuelle^.coordonneesCase.x, caseActuelle^.coordonneesCase.y + 1, caseActuelle);
										if( plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] <> 0 ) then
										begin
											plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] := 0;
											nbDeMursRestant := nbDeMursRestant - 1;
										end;
									end
									else
										caseActuelle^.contraintesCase.droite := TRUE;//Si l'une des cases adjacentes à la case demandée est déjà rattachée au labyrinthe (sauf la case en cours), on bloque cette direction pour le prochain tour de boucle.
								end
								else
									caseActuelle^.contraintesCase.droite := TRUE;//Si la case demandée est déjà occuppée par l'entrée, on bloque la direction.
							end
							else
								caseActuelle^.contraintesCase.droite := TRUE;//Si l'on voit que l'on a rencontré la limite basse du plateau, on bloque cette direction pour le prochain tour de boucle.
						end;
					end;
				end;
			end
			else
			begin
				//Si toutes les contraintes sont activées, on recule d'une case.
				remove_item_end(listeCases, caseActuelle);

				//Si le processus de génération est bloqué, on termine la procédure.
				if( listeCases^.suivant = NIL ) then
				begin
					SetLength(plateau, 0);
					exit(plateau);
				end;
			end;
		end;
		plateau[caseActuelle^.coordonneesCase.x,caseActuelle^.coordonneesCase.y] := 3;//On place le trésor
		clear_list(listeCases);
		generer_plateau := plateau;
	end;

	{
		procedure tour_IA(var IAGagne : Boolean; var plateauJoueurIA : ArrayOfInteger; var caseActuelleIA, caseArriveeIA : Coordonnees);
		* Joue le coup de l'IA.
	}
	procedure tour_IA(var IAGagne : Boolean; var plateauJoueurIA : ArrayOfInteger; var caseActuelleIA, caseArriveeIA : Coordonnees);
	var choix : Char;
	itineraire : ArrayOfNode;
	caseDemandee : Node;
	caseSuivante : Integer;
	begin
		itineraire := find_path(plateauJoueurIA, caseActuelleIA, caseArriveeIA);
		caseDemandee := itineraire[0];
		caseSuivante := 1;
		
		while( ( plateauJoueurIA[caseActuelleIA.x, caseActuelleIA.y] <> 3 ) ) do
		begin
			ClrScr;
			afficher_plateau(plateauJoueurIA);
			if( plateauJoueurIA[caseDemandee.x, caseDemandee.y] <> 4) then
			begin
				if( plateauJoueurIA[caseDemandee.x, caseDemandee.y] <> 2 ) then
				begin
					caseActuelleIA.x := caseDemandee.x;
					caseActuelleIA.y := caseDemandee.y;
					caseDemandee := itineraire[caseSuivante];
					inc(caseSuivante);
				end;
			end
			else
			begin
				//Le contenu est inconnu, il faut donc le demander au joueur
				writeln('Quel est le contenu de la case [', caseDemandee.x + 1, ',', caseDemandee.y + 1, '] ?');
				writeln('(v)ide');
				writeln('(m)ur');
				repeat
					choix := readkey;
				until((choix = 'm') OR (choix = 'v'));
				case choix of
					'm' :
					begin
						plateauJoueurIA[caseDemandee.x, caseDemandee.y] := 1;
						finalize(itineraire);
						exit;
					end;
					'v' :
					begin
						caseActuelleIA.x := caseDemandee.x;
						caseActuelleIA.y := caseDemandee.y;
						plateauJoueurIA[caseActuelleIA.x, caseActuelleIA.y] := 0;
						caseDemandee := itineraire[caseSuivante];
						inc(caseSuivante);
					end;
				end;
			end;
		end;
		if( plateauJoueurIA[caseActuelleIA.x, caseActuelleIA.y] = 3 ) then
		begin
			writeln('J''ai gagné !');
			finalize(itineraire);
			IAGagne := TRUE;
		end;
	end;

begin
     {Corps du module IA}
end.
