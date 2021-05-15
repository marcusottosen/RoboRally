# Roborally

## Hvordan spillet startes:
1. Unpack zip filen.
2. Lav en ny/ren database i MYSQL.
3. Åben projektet i din IDE.
4. Sørg for at du bruger java SDK 15.0.2 i din project structure.
5. Nagiver til Connetor.java og indtast de rette oplysninger til din database. (.../pisd/roborally/dal/Connector)
6. Kør programmet i StartRoboRally.java.
7. Vælg antal personer og et board.


## Spillets features:
Spillet har i denne version:
* 7 kort (move1, move2, move3, right, left, u-turn og lef/right).
* En dropdown menu som indeholder: Stop, Save Game og Exit Game.
* Mulighed for at gemme og loade spillet fra/til en lokal database.
* 3 knapper som kan bruges til at: færdiggøre programmeringsfasen, execute alle programkort og execute hvert programkort for sig.
* Mulighed for at skubbe til andre spillere.
* En ny VBox over spillepladen som viser spillernes informationer, herunder navn, score og liv samt opsamlede energyCubes.
* Opnås der 3 checkpoints vinder spilleren og der vises en "vinderbox".
* Der gives kun nye kort ved de kort der er blevet brugt.
* Mister en spiller alle sine liv, bliver spilleren sat tilbage til spawn og mistet alle checkpoints & energyCubes.
* Mulighed for at opsamle følgende energyCubes som spawner randomly:
    * Laser: giver en spiller mulighed for at skyde med en laser (kun én spiller kan have denne).
    * Ekstra liv: giver spilleren mulighed for at få 4 liv i alt.
    * Ekstra move: rykker spilleren et ekstra felt frem.
    * Deflector shield: et single-use skjold mod en laser.
    * Melee våben: Spilleren kan skade en anden spiller når der skubbes.
    * Nye kort: Spilleren kan vælge om han/hun vil have nye kort.
    

### Felter:
* Checkpoint felt som giver en spiller et point når spilleren står på det. Tæller automatisk op og skal være i den rette rækkefølge.
* Et felt med en væg, som kan sættes til hhv. NORTH, SOUTH, EAST, WEST.
* Pits som fjerner alle spillerens liv.
* Conveyorbelts i både gul og blå som rykker spilleren hhv. 1 og 2 felter.
* Gears i begge retninger som drejer spilleren.
* Toolbox som kan give spilleren nyt liv.
* PushPanel som skubber spilleren et felt.
* En laser emitter som skyder med laser ved slutningen af aktiveringsfasen. Skyder indtil laseren rammer en væg eller siden af boarded.
* EnergyCubes som spawner ved tilfældige frie felter. Hver cube kan kun bruges en gang.


## Hvordan spillet spilles:
Spillet spilles ved at:
1. Spillet køres fra IDE'en hvor du først vil blive bedt om at loade, starte et nyt spil eller at exit.
2. Ved tryk på "new game" vil du blive bedt om at angive antal spillere samt hvilket board der skal bruges.
3. Dernæst skal hver spiller trække op til 5 kommandokort op i programfelterne.
4. Der skal så trykkes på finish programming som låser kortene.
5. Spillet kan nu fortsætte ved tryk på "Execute Program" som udfører alle programkort indtil der evt. bedes om interaktion fra brugeren.
    * Der kan i stedet trykkes på "Execute Current Register" som udfører et programkort af gangen.
6. Prøv ikke at dø!
7. Når alle programkort er blevet udført, gives der nye kommandokort og der skal nu programmeres igen.
8. Spillet slutter når en spiller har fået alle checkpointsne.