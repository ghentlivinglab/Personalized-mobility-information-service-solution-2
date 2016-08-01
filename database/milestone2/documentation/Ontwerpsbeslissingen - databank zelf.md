# Ontwerpsbeslissingen: databank v3

## v3.01 (15/03/2016)

### account

!!! **user** is een reserverd keyword in postgresql en database standaarden zeggen dat tabelnamen enkelvoud moeten zijn. Daarom is mijn voorstel om user te wisselen naar account in de databank.

* gender heeft geen nut: volledig eruit
* zoveel mogelijk velden zijn optioneel gemaakt: in principe is enkel een emailadres en een wachtwoord echt nodig. Zo kan het systeem ermee omgaan als we later slechts heel weinig gegevens zouden vragen (voor de privacy).
* emailadres moet uniek zijn over alle accounts


### travel

* foreign key: travel bevat een foreign key naar account. Geen gedoe meer met link tabellen aangezien er toch altijd een nieuwe travel moet aangemaakt worden (we kunnen travels dus niet hergebruiken).
* Voor de foreign key naar account geldt NOT NULL: een travel heeft altijd een account nodig. Om te vermijden dat programmeerfouten resulteren in verweesde travels is de foreign key dus verplicht.
* Voor de foreign key naar account geldt ON DELETE CASCADE en ON UPDATE CASCADE. Als een account verwijderd wordt, mogen al zijn travels er ook uit.
* Een travel bevat nog steeds zeven booleans om wekelijks herhaalde travels voor te stellen. Default staan ze op false.
* time interval is nu geen array meer (was weel heel complex voor enkel twee waarden voor te stellen). Nu is er begin_time en end_time. end_time mag null zijn indien er geen interval nodig is.

### route

* foreign key naar travel: Geen gedoe meer met link tabellen aangezin we toch telkens nieuw routes aanmaken.
* voor de foreign key naar travel geldt NOT NULL: een route heeft altijd een travel nodig. Om te vermijden dat programmeerfouten resulteren in verweesde routes is de foreign key dus verplicht.
* Voor de foreign key naar travel geldt ON DELETE CASCADE en ON UPDATE CASCADE. Als een travel verwijderd wordt, mogen al zijn routes er ook uit.
* Waarom hebben we eigenlijk nog een aparte route tabel nodig? --> Momenteel zijn er niet veel redenen om een aparte route tabel te hebben. In principe kan je alle informatie in travel steken. Bij API is echter besproken dat het mogelijk moet zijn om in een travel (waar dus alle informatie zoals reistijden inzit) meerdere alternatieve routes te steken.
* transportation_types bevatten standaard enkel "car" als niets anders wordt opgegeven.

### route_eventtype

* Hoewel het strikt gezien niet nodig is, heb ik een aparte id kolom toegevoegd aan de de route_eventtype tabel. Deze was de enige tabel zonder zo een kolom, en daarom zouden we het als een speciaal geval moeten rekenen in de generieke DAO's in de backend.

### city

* Het is de bedoeling dat alle mogelijke steden al in de databank komen.
* De combinatie van post code en land moet uniek zijn in de tabel. Een postcode kan dus niet horen bij twee steden.

### address

* Een address heeft een unique constraint over de volgende kolommen: street, housenumber en cityid. Op basis van deze velden kunnen we bepalen of een adres al in de databank zit en kan herbruikt worden.
* latitude en longitude zijn niet meer NOT NULL. Dit geeft ons een beetje meer beslissingsruimte later.
* ON DELETE voor foreing key naar city staat op default waarde van NO ACTION. Er wordt dan een exception opgesmeten als je een city probeert te verwijderen waar een address gebruik van maakt.

### eventtype

* type en subtype zijn samengevoegd tot een veld. Dit is algemener voor het geval we later ook datasources krijgen met andere indelingen dan Waze.
* relevant_transportation_types is NOT NULL en staat standaard op een array met enkel "car" in.
* er is een unique constraint op de type kolom. Een type kan dus maar een keer voorkomen.

### account_address

* twee foreign keys: een naar account en een naar address
* ON DELETE en ON UPDATE zijn CASCADE voor de account fk. Als een account verwijderd wordt, zullen dus al zijn points of interest ook verdwijnen.
* ON DELETE blijft NO ACTION voor address fk. Er wordt dan een exception gesmeten als je een adres probeert te verwijderen dat nog gebruikt wordt.
* name veld mag null zijn
* Er is een unique constraint over de velden userid en addressid.

### Triggers

Ik heb nog twee triggers overgehouden om de databank consistent te houden. Beiden zijn bedoeld om ongebruikte adressen te verwijderen. De eerste trigger, account_address_del, treedt op als er een point of interest wordt verwijderd. Er wordt dan gekeken of het er nog ergens wordt verwezen naar het adres dat in de poi zat. Dit kan enkel vanuit account_address zelf zijn of vanuit route. Indien dit niet het geval is, wordt het address verwijderd. Hetzelfde gebeurt in route_del, alleen wordt dit getriggerd bij het verwijderen van een route.


## v3.02 (26/03/2016)

### street

Met het oog op het koppelen van events hebben we meer voorzieningen nodig in de databank. Momenteel denken we eraan om een eerste filtering van relevantie te implementeren op basis van straten. Van elke route moet dan een reeks straten opgeslagen worden langs waar ze gaat. Daarom hebben we een nieuwe tabel toegevoegd voor een straat.
* Een straat heeft een straatnaam
* Een straat bevat een foreign key naar City. ON UPDATE staat op CASCADE (mocht in een uitzonderlijk geval de primary key van City veranderen, wordt dit ook in street aangepast). ON DELETE blijft op de default waarde van NO ACTION staan. Als je dan een city probeert te verwijderen die nog in een straat voorkomt, zal dit niet doorgaan en smijt de databank een exception op.
* Er is een unique constraint over de gecombineerde kolommen name en cityid.
* Een staat die in de werkelijkheid als slechts één staat aanzien wordt maar over meerdere gemeentes loopt, zal in deze databank meerdere keren voorkomen (maximaal één keer per gemeente waar ze doorheen loopt). Dit behoudt de eenvoud van de databank (geen extra linktabellen etc nodig). Mocht later blijken dat deze beslissing problemen oplevert, zullen we extra maatregelen nemen.

### address

* street en cityid zijn uit de address tabel gehaald en bevinden zich nu in de aparte kolom street.
* address heeft een foreign key, streetid, naar de tabel street.
* Unique constraints zijn aangepast om nieuwe tabelstructuur te reflecteren.

### route_street

* Deze tabel wordt gebruikt om van een route alle straten waarlangs hij loopt op te slagen.
* Bevat een foreign key naar route. ON UPDATE en ON DELETE zijn beiden CASCADE
* Bevat een foreign key naar street. Hier is ON DELETE echter niet CASCADE (we willen niet dat een straat verwijderd kan worden zolang er nog een route gebruik van maakt).
* Eigenlijk hebben we de keuze tussen twee aanpakken om de straten van een route op te slagen: ofwel via een linktabel (zoals hier dus), ofwel in een array in de tabel van route zelf. De array heeft als grote voordeel dat de volgorde van de straten (die uiteraard van belang is voor een route) bewaard blijft. Werken met een linktabel heeft echter nog een veel groter voordeel. We zullen namelijk vooral zoeken op routes die via een bepaalde straat lopen om er zo events aan te kunnen koppelen. Als we de straten zouden opslaan in een array, betekent dit dat we telkens alle routes moeten overlopen en controlleren of de straat in hun array voorkomt. Wanneer we voor een linktabel kiezen kunnen we makkelijk een select operatie uitvoeren op de street tabel en alle routes opvragen die hiernaar linken.
* Omdat we de volgorde van de straten door de linktabel kwijt zijn, is er nog een extra kolom nodig. waypoint_number bevat dus het volgnummer van de straat in de route.
* Er is een unique constraint over alle kolommen van de tabel. Dit betekent dat een straat in principe meerder keren kan voorkomen in een route (bijvoorbeeld voor routes waar even van een straat wordt afgeweken en verderop weer op dezelfde straat wordt verdergegaan).

### Triggers
* Er zijn twee triggers bijgekomen om ongebruikte straten te verwijderen. De eerste trigger komt in actie bij het verwijderen van een address en de tweede bij het verwijderen van een route_street. De functie van beide triggers is dezelfde: clear_street_on_address_del(). Deze functie kijkt of er nog een address of een route_street bestaat die naar de te verwijderen straat verwijst. Als dit niet het geval, wordt de straat verwijderd.

## v3.03 (27/03/2016)

Deze update voegt de functionaliteit toe om een 'mailbox' op te slaan met events die relevant zijn voor een bepaalde user. Momenteel zijn er drie mogelijkheden: ofwel is een event relevant voor een route, ofwel voor een point of interest (=location), ofwel is het event niet relevant. Hiervoor zijn er twee extra tabellen aangemaakt:

### route_event

* Deze tabel is bedoeld als een soort linktabel tussen events en de routes waarvoor ze relevant zijn.
* Bevat een foreign key naar route. ON UPDATE en ON DELETE zijn beiden CASCADE. Als een route verwijderd wordt, mogen zijn relevante events er natuurlijk ook uit.
* In de kolom eventid komt de "primaire sleutel" van het event in MongoDB.
* De kolom deleted geeft aan of de gebruiker heeft aangeduid dat het event verwijderd mag worden. In principe zou je dan ook gewoon de record kunnen verwijderen. Het probleem is echter dat Waze doorgaans events herhaalt in zijn datadumps. Wanneer we op basis hiervan de koppeling tussen events en accounts leggen, kan het dus zijn dat we telkens events blijven doorsturen naar de gebruiker, zelfs als hij ze al verwijderd heeft. Misschien is het dus een betere oplossing om de records te behouden en met een grafsteen te werken (namelijk deze kolom).

### location_event
* Location is andere naam voor point of interest
* uitleg volledig analoog als *route_event*. Hier gaat het echter over events relevant voor een point of interest van een gebruiker.

! Als we de koppeling tussen events en accounts op deze manier opslagen (als links tussen events en poi's/routes) is het mogelijk dat hetzelfde event meerder keren voorkomt als relevant voor hetzelfde account. Als een account over meerdere routes beschikt, kan een event in principe ook voor meerdere routes relevant zijn. Ook kan een event relevant zijn voor een bepaalde route en point of interest van hetzelfde account. De back end code moet dus slim genoeg zijn om deze dubbels eruit te halen bij het opvragen van alle relevante events voor een account.

## v3.04 (29/03/2016)

Deze update verwerkt de laatste API aanpassingen in de databank.

### account

* email_validated en cell_number_validated zijn twee extra kolommen om aan te geven of de gebruiker reeds zijn email resp. gsm nummer heeft gevalideerd. Alle twee zijn ze NOT NULL en DEFAULT op false.
* valid_cell_not_null is een constraint die ervoor zorgt dat cell_number_validated nooit op true kan staan als het cell_number zelf nog op NULL staat.

### travel
* Enkele elementen zijn uit route naar travel gehaald: een travel heeft nu een name en beginpoint en endpoint zitten ook in travel.

### location
* !!! acount_address is hernoemd naar location.
* notify_email en notify_cell duiden aan op welke manieren de gebruiker op de hoogte wenst gebracht te worden van relevante events voor deze locatie. (default op false)

### location_eventtype
* Deze extra tabel werkt volledig analoog als route_eventtype. De gebruiker kan dus eventtypes selecteren voor elke locatie waarvan hij op de hoogte wenst gehouden te worden.

### route
* Volgende kolommen zijn naar travel verplaatst: name, startpoint en endpoint.
* Met de active kolom kan de gebruiker een route tijdelijk uitschakelen.
* Net zoals bij location kan de gebruiker specifiek aangeven hoe hij op de hoogte wilt gehouden worden over events relevant voor deze route. notify_email en notify_cell zullen default op false staan.

### Triggers
* De triggers die met begin- en eindpunt van routes te maken hadden zitten nu in travel. Enkele kleine aanpassingen zijn hierom doorgevoerd aan de triggers.

## v3.05 (03/04/2016)

### Travel

* Een paar kleine aanpassingen zijn doorgevoerd aan travel om de API beter te reflecteren. Een travel beschikt niet meer over een specifieke datum. Er is enkel een begin_time en end_time, die nu beiden NOT NULL zijn.

## v3.06 (05/04/2016)

### route

* Slechts een kleine aanpassing. Een route bevat nu slechts één vervoersmiddel. De kolom transportation_types werd vervangen door transportation_type, die nu van het type text is in plaats van text[].


## v4.00 (20/04/2016)

We beginnen met versie 4 van de databank om de laatste milestone aan te duiden. Aangezien er in voorgaande versies al redleijk veel voorzien is om het koppelen van events te ondersteunen brengt deze nieuwe versie niet veel veranderingen met zich mee.

### account

* Een extra kolom van het type bytea toegevoegd met de naam 'salt'. Wordt gebruikt voor het hashen van paswoorden.
* Een extra kolom, refresh_token, is toegevoegd aan een account. Momenteel is dit nog het datatype "text", maar het kan goed zijn dat dit later moet veranderd worden naar een byte array.
* Extra velden om aan te duiden of het een admin of operator is. Beiden default op false.

### location_event en route_event

* event id foreign key aangepast van integer naar text, aangezien Mongo een string id gebruikt.

## v4.01 (24/04/2016)

### Address

* Toevoeging van kolommen cartesianX en cartesianY, van het type numeric. Deze waarden zijn bedoeld om de cartesische coordinaten van het adres in op te slagen ten opzicht van het referentiepunt uit de backend.

### Route

* heeft nu twee arrays. De vroegere waypoints zijn nu user_waypoints. Dit dient voor de specifieke punten op te slagen waar de gebruiker wilt passeren. full_waypoints geeft een volledige beschrijving van de route en wordt gebruikt om aan event matching te doen.

## v4.02 (25/04/2016)

### route_gridpoint

* In het kader van de nieuwe route preprocessing om beter events te kunnen koppelen, is er nood aan meer data in de databank. Elke route wordt nu bij het invoegen gerasterizeerd. Dit wilt zeggen dat er een vast grid over Gent wordt getrokken, en gekeken in welke vakjes de route allemaal ligt. Deze vakjes worden opgeslagen in deze tabel.

## v4.03 (28/04/2016)

### account

* Alles te maken met gsm nummer is uit user gehaald omdat het toch niet meer gebruikt wordt. Alles dat de databank kan clutteren moet eruit, dus ook cell en cell_number_validated.
* Er is een timestamp veld toegevoegd dat default op de huidige tijd. Zo weten we exact wanneer een gebruiker wordt toegevoegd. Dit zal ook gebruikt worden om accounts te verwijderen die niet binnen de 24 uur hun email gevalideerd hebben.

### route_street

* Deze hele tabel gaat eruit. We werken nu op basis van een ander idee om te koppelen.

### location_street

* Deze hele tabel gaat eruit. We werken nu op basis van een ander idee om te koppelen.

### triggers

* De triggers die straten horen op te kuisen werden aangepast omdat route_street en location_street eruit zijn.

## v4.04 (15/05/2016)

# account

* De kolom push_token is toegevoegd als text. Deze mag null zijn.
