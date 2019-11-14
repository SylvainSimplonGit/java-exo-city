- [Contexte](#org2b49f88)
- [Données](#org1ffd950)
- [Opérations](#org3680241)
- [Chargement](#orgd807425)
- [Programme principal](#org587c39d)



<a id="org2b49f88"></a>

# Contexte

Considérant les programmes qui composent un [*système d'information*](https://fr.wikipedia.org/wiki/Système_d'information), on a vu que la modélisation des données qui nous intéressent spécifiquement (*entités* du domaine d'application) pouvaient être modélisées en Java par des *objets*, instances de *classes*.

Pour que ces données survivent à l'exécution des programmes, il faut qu'elles *persistent* sous la forme d'un stockage permanent, par exemple sous la forme de fichier.

Grâce aux possibilités d'abstractions fournies par le langage (héritage, implémentation d'*interfaces*), l'implémentation proprement dites (fichier local ou données récupérées à partir d'une URL par exemple), n'ont pas vraiment d'impact tant qu'on peut considérer la source comme un *flux* de données. On peut facilement lire l'intégralité des données et *construire* les objets au fur et à mesure en les stockant dans une structure de données, par exemple l'une des nombreuses *collections* de la bibliothèque standard Java.

On pourra ensuite utiliser ces données pour interroger le programme afin de sélectionner un certain sous-ensemble des données selon divers critères.


<a id="org1ffd950"></a>

# Données

On va devoir manipuler des communes caractérisées par :

-   un nom
-   une latitude
-   une longitude

-   **Exercice:** Définir une classe `City` avec les attributs nécessaires, ainsi qu'un constructeur permettant de les initialiser.


<a id="org3680241"></a>

# Opérations

On veut permettre de :

-   calculer la distance d'une commune à un point quelconque repéré par ses latitude et longitude.
-   calculer la distance d'une commune à une autre commune.
-   trouver la commune la plus proche d'une commune, parmi un ensemble quelconque de communes.
-   trouver les communes qui sont présentes dans un rayon donné autour d'un point quelconque repéré par ses latitude et longitude.
-   trouver les communes qui sont simultanément dans un rayon donnée d'un ensemble de points quelconques repérés par leur latitude et longitude.

-   **Exercice:** Définir les méthodes de la classe `City` nécessaires à ces opérations.


<a id="orgd807425"></a>

# Chargement

On voudra pouvoir charger des données à partir de fichiers comme [celui-ci](https://github.com/simplonco/corp-bnp-renault/blob/master/session1/ressource/Communes.csv).

-   **Exercice:** Définir les méthodes ou fonctions permettant de charger ces données. Il faut gérer le fait que le fichier puisse contenir des erreurs.
-   **Bonus:** Permettre le chargement à partir d'une URL.


<a id="org587c39d"></a>

# Programme principal

Écrire un programme qui permette à l'utilisateur d'utiliser les différentes fonctionnalités en passant le fichier (chemin ou URL) en argument.
