Program Sphinx;
{
	Jeu de sphinx en console

	Copyright 2012 Stanislas Michalak <stanislas.michalak@live.fr>

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
	MA 02110-1301, USA.
}
Uses Crt, IA, joueur, listesChainees, Astar;
var choixMenu : Integer; continuer : Boolean;

{
	void parametres();
	* Affiche les paramètres actuels, demande la saisie et enregistre de nouveaux paramètres dans le fichier de config.
}
procedure parametres();
var settings : text;
params : array [0..2] of Integer;
i, error : Integer;
configNotOk : Boolean;
choix : Char;
begin
	ClrScr;

	{
		Le changement des paramètres se fait en deux temps :
		* d'abord, on vérifie que le fichier de config existe. Si c'est le cas, on affiche les valeurs actuelles.
		* ensuite, on propose à l'utilisateur de changer les valeurs.  
	}
	
	//Chargement du fichier de configuration en lecture.
	{$I-}
	assign(settings, './settings.txt');
	reset(settings);
	{$I+}

	error := IORESULT;
	if error <> 0 then//Si l'on a trouvé une erreur, on la traite.
	begin
		if( error = 2 ) then//Le fichier n'existe pas, on va donc en créer un nouveau
		begin
			rewrite(settings);
			close(settings);
		end
		else
		begin
			//Impossible de créer le fichier. On le signale et on retourne au menu
			writeln('Impossible de créer le fichier de sauvegarde des paramètres.');
			readln;
			exit;
		end;
	end
	else
	begin
		writeln('Paramètres');
		writeln;
		writeln('Voici la configuration actuelle :');
		
		for i:= 0 to 2 do
			read(settings, params[i]);//On stocke les paramètres dans des variables utilisables par la suite.

		close(settings);

		//Affichage des valeurs actuelles
		writeln('Hauteur : ', params[0], ' cases');
		writeln('Largeur : ', params[1], ' cases');
		writeln('Nombre de murs : ', params[2]);

		writeln;

		//On propose à l'utilisateur de changer ou non les paramètres actuels.
		writeln('Voulez-vous changer ces paramètres ? (o/n)');
		repeat
			choix := readkey;
		until((choix = 'o') OR (choix = 'n'));

		//Si c'est non, on retourne au menu.
		if( choix = 'n' ) then exit;
	end;

	configNotOk := TRUE;

	//Tant que les paramètres ne sont pas valides, on en demande de nouveaux.
	while( configNotOk ) do
	begin
		writeln('Nouvelle configuration :');
		write('Hauteur : ');
		readln(params[0]);
		write('Largeur : ');
		readln(params[1]);
		write('Nombre de murs : ');
		readln(params[2]);

		if( (params[0] > 0) AND (params[1] > 0) AND (params[2] > 0) ) then
		begin
			if( params[2] >= trunc(0.31 * params[0] * params[1]) ) then
			begin
				if( params[2] <= (params[0] * params[1]) - 2 ) then
					configNotOk := FALSE
				else
					writeln('Le nombre de murs demandé est trop élevé pour générer un labyrinthe.');
			end
			else
				writeln('Le nombre de murs demandé est insuffisant pour générer un labyrinthe.');
		end
		else
			writeln('Les paramètres doivent être strictement positifs.');

		writeln;
	end;

	//On recharge le fichier de configuration en écriture cette fois.
	{$I-}
	assign(settings, './settings.txt');
	rewrite(settings);
	{$I+}

	error := IORESULT;
	if( error = 5 ) then//Le fichier est en lecture seule, on prévient donc l'utilisateur et on retourne au menu.
	begin
		writeln('Le fichier de configuration est en lecture seule. Impossible de mettre-à-jour les paramètres.');
		readln;
		exit;
	end;

	//On écrit les nouveaux paramètres dans le fichier.
	writeln(settings, params[0]);
	writeln(settings, params[1]);
	writeln(settings, params[2]);

	writeln;
	writeln('La nouvelle configuration a été enregistrée');
	readln;

	close(settings);
end;

{
	void nouvelle_partie();
	* Gère le déroulement d'une partie
}
procedure nouvelle_partie();
var hauteur, largeur, nbDeMurs : Integer;
plateauIA, plateauIAJoueur, plateauJoueurIA : Array of Array of Integer;
settings : text; params : array [0..2] of Integer; i, error : Integer;
caseActuelleJoueur, caseActuelleIA, caseArriveeIA : Coordonnees;
joueurGagne, IAGagne : Boolean;
begin
	ClrScr;

	
	{
		* Chargement des paramètres et création des plateaux
	}
	
	//Chargement du fichier de configuration
	repeat
		{$I-}
		assign(settings, './settings.txt');
		reset(settings);
		{$I+}

		//Si le fichier de configuration n'existe pas, on redirige vers le menu paramètres.
		error := IORESULT;
		if error <> 0 then
			parametres();
	until(error = 0);

	ClrScr;

	//Sinon, on récupère les paramètres et on les stocke dans les variables.
	for i:= 0 to 2 do
		read(settings, params[i]);

	close(settings);
	hauteur := params[0];
	largeur := params[1];
	nbDeMurs := params[2];
	
	repeat//Tant que le plateau de l'IA n'est pas correct, on en regénère un.
		plateauIA := generer_plateau(hauteur, largeur, nbDeMurs);
	until( length(plateauIA) <> 0 );
	
	plateauIAJoueur := copier_plateau(plateauIA);
	plateauJoueurIA := creer_plateau(hauteur, largeur, nbDeMurs);

	{
		* Déroulement de la partie
	}

	//On positionne le joueur à la case départ.
	caseActuelleJoueur := case_depart(plateauIA);

	//Même chose pour l'IA. On récupère au passage la position de la case d'arrivée, nécessaire pour A*.
	caseActuelleIA := case_depart(plateauJoueurIA);
	caseArriveeIA := case_arrivee(plateauJoueurIA);

	//Par défaut, personne n'a gagné
	joueurGagne := FALSE;
	IAGagne := FALSE;

	ClrScr;

	//Qui commence ?
	case random(2) of
		0 :
		begin
			tour_joueur(joueurGagne, plateauIA, plateauIAJoueur, caseActuelleJoueur);
			tour_IA(IAGagne, plateauJoueurIA, caseActuelleIA, caseArriveeIA);//Nécessaire pour éviter que le joueur joue deux fois (voir while ci-dessous)
		end;
		1 : tour_IA(IAGagne, plateauJoueurIA, caseActuelleIA, caseArriveeIA);
	end;
	
	while( NOT(joueurGagne) AND NOT(IAGagne) ) do
	Begin
		tour_joueur(joueurGagne, plateauIA, plateauIAJoueur, caseActuelleJoueur);
		if( joueurGagne ) then
			break;//Correction d'un bogue (les conditions de la boucle while ne sont pas vérifiées à chaque instant, du coup le joueur IA peut continuer de jouer si l'on ne fait rien).
		tour_IA(IAGagne, plateauJoueurIA, caseActuelleIA, caseArriveeIA);
	End;
	readln;
end;

{
	void regles_du_jeu();
	* Affiche la règle de jeu.
}
procedure regles_du_jeu();
var rules : text; x : string;
begin
	ClrScr;
	
	//Chargement du fichier contenant les règles du jeu.
	{$I-}
	assign(rules, './rules.txt');
	reset(rules);
	{$I+}

	//S'il y a une erreur de chargement, on le signale à l'utilisateur et on retourne au menu.
	if( IORESULT <> 0 ) then
	begin
		writeln('Erreur de chargement des règles du jeu');
		readln;
		exit;
	end;

	//Affichage du contenu du fichier (donc affichage des règles du jeu).
	Repeat	
		readln(rules, x);
		writeln(x);
	Until(eof(rules));
	
	close(rules);
	readln;
end;

{
	Ordonnanceur général
	* Lance les fonctions de base et permet de quitter le programme.
}
begin
	RANDOMIZE;
	continuer := TRUE;
	
	while(continuer = TRUE) do
	begin
		ClrScr;
		writeln('                        __                      ');
		writeln('  ___                  /_I\                  ___');
		writeln(' /   \     __         /_I__\         __     /   \');
		writeln('/     \   /  \   _   /I___I_\   _   /  \   /     \');
		writeln('       \_/    \_/ \_/___I__I_\_/ \_/    \_/');
		writeln(' __________________/__I___I___\________________');
		writeln('                  /_I___I___I__\');
		writeln('                 /I___I___I___I_\');
		writeln('                /___I_______I___I\');
		writeln('               /__I___Sphinx__I___\');
		writeln('              /_I___I_______I___I__\');
		writeln('             /I___I___I___I___I___I_\');
		writeln('            /___I___I___I___I___I___I\');
		writeln('           /__I___I___I___I___I___I___\');
		writeln('          /_I___I___I___I___I___I___I__\');
		writeln;
		writeln('              1 - Nouvelle partie');
		writeln('              2 - Paramètres');
		writeln('              3 - Règles du jeu');
		writeln('              4 - Quitter');
		writeln;
		write('              Que voulez-vous faire ? ');
		readln(choixMenu);

		case choixMenu of
			1 : nouvelle_partie();
			2 : parametres();
			3 : regles_du_jeu();
			4 : continuer := FALSE;
		end;
	end;
end.
