# Database v2

## Probleemstelling

De databank code die we voor milestone 1 hebben geschreven hing teveel af van de rest van het programma. Van zodra er iets kleins veranderde aan de modellen wou de helft van de DAO's niet meer compileren. Verder hingen de DAO's ook veel te veel van elkaar af. Dit was zeer goed zichtbaar bij pakweg Travel: een Travel bevat een route, een route een address en een address een city. We kregen toestanden waar een aanpassing aanleiding gaf tot een watervalsysteem van wel vier DAO's diep. Die "recursie" in DAO's resulteerde ook in een nieuwe connectie naar de databank voor ieder niveau.

De problemen zijn kortom:

1. DAO's hangen teveel af van modellen
2. DAO's hangen teveel af van elkaar
3. DAO's die elkaar oproepen resulteert in teveel openstaande connecties naar databank
4. Codeduplicatie in elke DAO aangezien ze logisch gezien allemaal dezelfde taak hebben.

## Vernieuwde databank component

Om de bovenstaande problemen op te lossen stellen we het volgende ontwerp voor.

In de eerste plaats delen we databank component op in twee deelcomponenten, in een gelaagde structuur. De onderste laag, de DAOLayer, staat het dichtste bij de fysieke databank zelf. De laag erboven vormt dan weer een interface naar de rest van de backend code en sluit het dichtste aan bij de toepassing. Communicatie tussen deze twee lagen gebeurt door databank modellen.

### Databank modellen

De databank component zal vanaf nu gebruik maken van zijn eigen modellen. Hiermee lossen we het eerste probleem op: we doorbreken namelijk in grote mate de afhankelijkheid van de modellen uit de rest van de applicatie. De databank modellen zijn voornamelijk bedoeld om gemakkelijk met de fysieke databank zelf te communiceren. In feite is het de bedoeling dat de databank modellen een één op één relatie vormen met de tabellen zoals ze aanwezig zijn in de database. De CRUDModel interface stelt een aantal vereisten op. Elke klasse die CRUDModel implementeert stelt dus een tabel in de databank voor. Wanneer er een aanpassing gebeurd aan een tabel of er komt er zelfs een bij, moet er enkel een databank model aangepast/aangemaakt worden.

## DAOLayer

De DAOLayer communiceert met de databank zelf. De taak van deze subcomponent is dus enkel om databank modellen te mappen op hun tabellen. Er is nog maar één generieke DAO over die alle modellen kan verwerken. Van zodra een model de CRUDModel interface correct implementeert, kan de DAO hem mappen.

 De basisbewerkingen van de DAO zijn Create, Read, Update en Delete. Hun werking spreekt voor zichzelf. Verder is het mogelijk om alle records van een tabel op te vragen. Er zijn ook twee search methodes. De eerste, simpleSearch, biedt de mogelijkheid om in een enkele tabel op zoek te gaan. Hierbij kunnen voorwaarden gelegd worden aan eender welke kolom, dus zoeken gaat verder dan opvragen op id. complexSearch biedt dan weer de mogelijkheid om te zoeken in tabellen waar een join operatie nodig is. In onze applicatie is een goed voorbeeld alle routes van een bepaalde account (vroeger user) opvragen. Hiervoor zijn twee joins nodig: account met travel en travel met route. Met de nieuwe DAO is het mogelijk om deze join abstract voor te stellen met de TableJoin klasse (zie javadoc van deze klasse).

 De DAO is dus doelbewust _dom_ gehouden. Voordien riepen alle DAO's elkaar op. Nu moet al deze informatie al correct in de modellen staan voor de DAO ermee aan de slag kan. Deze beperktere taak van de DAO maakt hem veel generieker wat dus de huidige aanpak mogelijk maakt.

 ## Databank controller

 De databank controller staat op zijn beurt dichter bij de applicatie. De taak van deze component bestaat erin applicatiemodellen te verwerken en deze te mappen op databank modellen. De controller geeft dan de aangemaakte databank modellen door aan de DAOLayer, vanwaar ze naar de databank worden gestuurd. Als er dus iets verandert aan de applicatie modellen, is het voldoende om de mapping in deze component aan te passen. Het waterval probleem van elkaar oproepende DAO's wordt ook in deze component opgelost. Stel dat we een applicatiemodel binnen krijgen waarin veel afhankelijkheden zitten (bijvoorbeeld een travel met een route in, waar op zijn beurt dan weer een address inzit). Dan is het de taak van de databankcontroller om deze afhankelijkheden serieel af te werken. Eerst handelt hij het adres af, dan de route en dan uiteindelijk het de travel zelf. Aangezien al deze logica nu in een centrale component zit, kan dit allemaal sequentieel gebeuren over dezelfde connectie, sterker nog, met dezelfde DAO!