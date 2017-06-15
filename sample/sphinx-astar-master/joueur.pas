Unit joueur;

interface
	uses Crt, listesChainees;

	function case_depart(plateau : ArrayOfInteger) : Coordonnees;
	function copier_plateau(plateauACopier : ArrayOfInteger) : ArrayOfInteger;
	function creer_plateau(hauteur, largeur, nbDeMurs : Integer) : ArrayOfInteger;
	procedure afficher_plateau(plateau : ArrayOfInteger);
	procedure bouger_joueur(plateauIA : ArrayOfInteger; var plateauIAJoueur : ArrayOfInteger; caseActuelleJoueur : Coordonnees);
	procedure tour_joueur(var joueurGagne : Boolean; plateauIA : ArrayOfInteger; var plateauIAJoueur : ArrayOfInteger; var caseActuelleJoueur : Coordonnees);

implementation

	{
		void afficher_plateau(ArrayOfInteger plateau);
		* Affiche le contenu d'un plateau
	}

	procedure afficher_plateau(plateau : ArrayOfInteger);
	var  i, j : Integer;
	Const VIDE = ' '; MUR = '■'; ENTREE = '▲'; TRESOR = '☼'; CACHE = '?'; JOUEUR = '☻';
	begin

		//Création du séparateur haut et de la numérotation
		writeln;
		writeln;
		write('               ');
		for j := 1 to high(plateau[0]) + 1 do write(j, ' ');
		writeln;
		write('             ');
		for j := 0 to high(plateau[0]) + 2 do write(MUR, ' ');
		writeln;
		
		for i := 0 to high(plateau) do
		begin
			write('          ', i + 1);
			if( ((i + 1) / 10) < 1 ) then//pour éviter le décalage des nombres à deux chiffres.
				write(' ');
			write(' ', MUR, ' ');
			for j := 0 to high(plateau[i]) do
			begin
				case plateau[i,j] of
					0 : write(VIDE, ' ');
					1 : write(MUR, ' ');
					2 : write(ENTREE, ' ');
					3 : write(TRESOR, ' ');
					4 : write(CACHE, ' ');
					5 : write(JOUEUR, ' ');
				end;
			end;
			write(MUR);
			writeln;
		end;

		//Création séparateur bas
		write('             ');
		for j := 0 to high(plateau[0]) + 2 do write(MUR, ' ');

		writeln;
		writeln;
	end;

	{
		ArrayOfInteger copier_plateau(ArrayOfInteger plateauACopier);
		* Copie un plateau en ne dévoilant que les cases d'entrée et de trésor
	}
	function copier_plateau(plateauACopier : ArrayOfInteger) : ArrayOfInteger;
	var nouveauPlateau : ArrayOfInteger; i, j :Integer;
	begin
		setlength(nouveauPlateau, high(plateauACopier) + 1, high(plateauACopier[0]) + 1);

		for i := 0 to high(nouveauPlateau) do
		begin
			for j := 0 to high(nouveauPlateau[0]) do
			begin
				if( (plateauACopier[i, j] = 2) OR (plateauACopier[i, j] = 3) ) then
					nouveauPlateau[i, j] := plateauACopier[i, j]
				else
					nouveauPlateau[i, j] := 4;
			end;
		end;
		
		copier_plateau := nouveauPlateau;
	end;

	{
		 ArrayOfInteger creer_plateau(int hauteur, int largeur, int nbDeMurs);
		* Crée un plateau à partir des indications du joueur.
	}
	function creer_plateau(hauteur, largeur, nbDeMurs : Integer) : ArrayOfInteger;
	var plateau : Array of Array of Integer;
	i, j, currentx, currenty : Integer;
	currentChoiceOk : Boolean;
	begin
		setlength(plateau, hauteur, largeur);
		
		//Initialisation du plateau.
		for i := 0 to high(plateau) do
		begin
			for j := 0 to high(plateau[i]) do plateau[i,j] := 4;
		end;

		currentChoiceOk := FALSE;
		while(NOT(currentChoiceOk)) do
		begin
			afficher_plateau(plateau);
			writeln;
			writeln('Entrez les coordonnées de la porte d''entrée :');
			write('x : ');
			readln(currentx);
			write('y : ');
			readln(currenty);

			//Réglage du décalage du zéro
			currentx := currentx - 1;
			currenty := currenty - 1;

			ClrScr;
			
			if( (currentx >= 0) AND (currentx <= high(plateau)) AND (currenty >= 0) AND (currenty <= high(plateau[0]))) then
			begin
				plateau[currentx, currenty] := 2;
				currentChoiceOk := TRUE;
			end
			else
				writeln('Case invalide.');
		end;

		writeln;
		ClrScr;
		currentChoiceOk := FALSE;
		while(NOT(currentChoiceOk)) do
		begin
			afficher_plateau(plateau);
			writeln;
			writeln('Entrez les coordonnées du trésor :');
			write('x : ');
			readln(currentx);
			write('y : ');
			readln(currenty);

			//Réglage du décalage du zéro
			currentx := currentx - 1;
			currenty := currenty - 1;
			
			ClrScr;
			
			if( (currentx >= 0) AND (currentx <= high(plateau)) AND (currenty >= 0) AND (currenty <= high(plateau[0]))) then
			begin
				if(plateau[currentx, currenty] <> 2) then
				begin
					plateau[currentx, currenty] := 3;
					currentChoiceOk := TRUE;
				end
				else
					writeln('Cette case est déjà occupée par la case départ.');
			end
			else
				writeln('Case invalide');
		end;
		creer_plateau := plateau;
	end;

	{
		void tour_joueur(bool *joueurGagne, ArrayOfInteger plateauIA, ArrayOfInteger *plateauIAJoueur, Coordonnees *caseActuelleJoueur);
		* Joue le coup du joueur.
	}
	procedure tour_joueur(var joueurGagne : Boolean; plateauIA : ArrayOfInteger; var plateauIAJoueur : ArrayOfInteger; var caseActuelleJoueur : Coordonnees);
	var toucheEntree : Char;
	caseDemandee : Coordonnees;
	continuer : Boolean;
	begin
		writeln('C''est à votre tour de jouer !');
		writeln;
		
		caseDemandee.x := caseActuelleJoueur.x;
		caseDemandee.y := caseActuelleJoueur.y;

		continuer := TRUE;

		while( continuer ) do
		begin
			ClrScr;
			//On teste d'abord les collisions externes
			if( (caseDemandee.x >= 0) AND (caseDemandee.x <= high(plateauIAJoueur)) AND (caseDemandee.y >= 0) AND (caseDemandee.y <= high(plateauIAJoueur[0]))) then
			begin
				//Puis si c'est un mur
				if( plateauIA[caseDemandee.x, caseDemandee.y] <> 1 ) then
				begin
					plateauIAJoueur[caseDemandee.x, caseDemandee.y] := plateauIA[caseDemandee.x, caseDemandee.y];
					caseActuelleJoueur := caseDemandee;

					if( plateauIAJoueur[caseDemandee.x, caseDemandee.y] = 0 ) then
						writeln('La case demandée est vide.')
					else if( plateauIAJoueur[caseDemandee.x, caseDemandee.y] = 2 ) then
						writeln('Vous êtes à la case départ.')
					else if( plateauIAJoueur[caseDemandee.x, caseDemandee.y] = 3 ) then
					begin
						writeln('Bravo ! Vous avez trouvé le trésor.');
						afficher_plateau(plateauIA);
						joueurGagne := TRUE;
						continuer := FALSE;
						exit;
					end;
					bouger_joueur(plateauIA, plateauIAJoueur, caseActuelleJoueur);
				end
				else
				begin
					plateauIAJoueur[caseDemandee.x, caseDemandee.y] := 1;
					writeln('La case que vous avez demandée contient un mur.');
					continuer := FALSE;
					exit;
				end;
			end
			else
			begin
				writeln('Cette case est en dehors du plateau. Choisissez-en une autre.');
			end;
			
			afficher_plateau(plateauIAJoueur);
			
			writeln;
			write('Utilisez les flèches directionnelles pour vous déplacer.');
			
			repeat
				toucheEntree := ReadKey;
				case ReadKey of
					#72 : caseDemandee.x := caseActuelleJoueur.x - 1;
					#80 : caseDemandee.x := caseActuelleJoueur.x + 1;
					#75 : caseDemandee.y := caseActuelleJoueur.y - 1;
					#77 : caseDemandee.y := caseActuelleJoueur.y + 1;
				end;
			until( toucheEntree = #0 )
		end;
	end;

	{
		Coordonnees case_depart(ArrayOfInteger plateau);
		* Renvoie les coordonnées de la case départ du plateau envoyé en paramètre.
	}
	function case_depart(plateau : ArrayOfInteger) : Coordonnees;
	var i, j : Integer; caseDepart : Coordonnees;
	begin
		for i := 0 to high(plateau) do
		begin
			for j := 0 to high(plateau[0]) do
			begin
				if( plateau[i, j] = 2) then
				begin
					caseDepart.x := i;
					caseDepart.y := j;
					exit(caseDepart);
				end;
			end;
		end;
	end;

	{
		void bouger_joueur(ArrayOfInteger plateauIA, ArrayOfInteger *plateauIAJoueur, Coordonnees caseActuelleJoueur);
		* Change la position du joueur sur le plateau et redéfinit le contenu de la case précédément occupée par celui-ci.
	}
	procedure bouger_joueur(plateauIA : ArrayOfInteger; var plateauIAJoueur : ArrayOfInteger; caseActuelleJoueur : Coordonnees);
	begin
		//On commence par chercher la position stockée actuellement
		if( (caseActuelleJoueur.x - 1 >= 0) AND (plateauIAJoueur[caseActuelleJoueur.x - 1, caseActuelleJoueur.y] = 5) ) then
			plateauIAJoueur[caseActuelleJoueur.x - 1, caseActuelleJoueur.y] := plateauIA[caseActuelleJoueur.x - 1, caseActuelleJoueur.y]
		else if( (caseActuelleJoueur.x + 1 <= high(plateauIAJoueur)) AND (plateauIAJoueur[caseActuelleJoueur.x + 1, caseActuelleJoueur.y] = 5) ) then
			plateauIAJoueur[caseActuelleJoueur.x + 1, caseActuelleJoueur.y] := plateauIA[caseActuelleJoueur.x + 1, caseActuelleJoueur.y]
		else if( (caseActuelleJoueur.y - 1 >= 0) AND (plateauIAJoueur[caseActuelleJoueur.x, caseActuelleJoueur.y - 1] = 5) ) then
			plateauIAJoueur[caseActuelleJoueur.x, caseActuelleJoueur.y - 1] := plateauIA[caseActuelleJoueur.x, caseActuelleJoueur.y - 1]
		else if( (caseActuelleJoueur.y + 1 <= high(plateauIAJoueur[0])) AND (plateauIAJoueur[caseActuelleJoueur.x, caseActuelleJoueur.y + 1] = 5) ) then
			plateauIAJoueur[caseActuelleJoueur.x, caseActuelleJoueur.y + 1] := plateauIA[caseActuelleJoueur.x, caseActuelleJoueur.y + 1];
		
		//Et on place le joueur
		plateauIAJoueur[caseActuelleJoueur.x, caseActuelleJoueur.y] := 5;
	end;
begin
     {Corps du module joueur}
end.
