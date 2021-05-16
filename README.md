# Roborally
Der henvises til Konfiguration- og Spilleguide kapitlerne i den vedlagte rapport frem for vejledningen her.

En guide på opsætningen af projektet kan både findes nedenfor og ved kapitlet "Vejledning til installation og opsætning" under konfigurationskapitlet.

## Opsætning:
1. Unpack zip filen.
2. Opret en ny/ren database i MySQL og navngiv den "roborally".
3. Åben den udpakkede mappe fra zip-filen i IntelliJ eller anden IDE.
4. Sørg for at du bruger Java SDK 15.0.2 i din project structure.
5. Nagiver til Connector.java og indtast de rette oplysninger til din database (src/main/java/dk/dtu/compute/se/pisd/roborally/dal/Connector.java)
6. Sørg for at DATABASE navnet stemmer overens med navnet på din database.
7. Hvis du bruger en anden user end root, skriv dette ved USERNAME.
8. Skriv dit kodeord til databasen ved PASSWORD.
9.  Du kan nu køre programmet i StartRoboRally.java ved at trykke på den grønne pil ved filen.