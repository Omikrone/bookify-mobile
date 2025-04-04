# Rapport de la SAE P42 - ABC

## Fonctionnalités

### Fonctionnalités implémentées

Pour l'application `Android Books Client`, conformément au cahier des charges, nous avons implémenté toutes les fonctionnalités qui nous étaient demandées. Nous en avons également rajouté quelques unes, donc voici la liste exhaustive:

- Affichage des auteurs dans un ``RecyclerView``

- Ajout d'une barre de recherche pour rechercher un auteur par son nom de famille

- Affichage des livres dans un `RecyclerView`

- Ajout d'une barre de recherche pour rechercher un livre par son titre

- Possibilité de supprimer les auteurs directement depuis le `RecyclerView` (ajout d'un bouton suppression dans l'item *auteur*)

- Affichage des détails de l'auteur sélectionné depuis le `RecyclerView`:
  
  - Photo de l'auteur
  
  - Prénom et nom
  
  - Biographie
  
  - Date de naissance et de décès
  
  - Affichage des livres de l'auteur (renvoie vers les détails du livre si clic sur un des livres)
  
  - Bouton de suppression qui renvoie sur la page d'affichage des auteurs en cas de succès

- Affichage des détails du livre sélectionné dans le `RecyclerView`:
  
  - Image de la couverture du livre
  
  - Titre
  
  - Notation moyenne du livre sous forme  de `RatingBar`
  
  - Année de publication
  
  - Description
  
  - Affichage des tags de l'auteur dans un `FlexboxLayout` (avec leur couleur récupérée via l'API)
  
  - Bouton de suppression qui renvoie sur la page d'affichage des livres en cas de succès

- Formulaire pour l'ajout d'un auteur depuis un ``FAB``, les champs à emplir sont les suivants:
  
  - Prénom de l'auteur
  
  - Nom
  
  - Biographie (optionnel)
  
  - Date de naissance sous forme de `DatePickerDialog` (optionnel)
  
  - Date de décès sous forme de `DatePickerDialog` (optionnel)
  
  - Bouton pour valider la saisie (les champs manquants sont marqués en rouge)

- Formulaire pour l'ajout d'un livre depuis un ``FAB``, les champs à remplir sont les suivants:
  
  - Titre

  - Année de publication

  - Description

  - Sélection de l'auteur sous forme de `AutoCompleteTextView` pour un Dropdown menu

  - Bouton pour valider la saisie (les champs manquants sont marqués en rouge)

### Bonus non implémentés et bugs

L'application comporte quelques bugs mineurs non résolus, qui ne gênent cependant pas l'utilisation globale de l'application:

- Le bouton de suppression sur la page d'affichage des livres n'est pas fonctionnel (il l'est cependant sur la page de détails du livre, on a juste oublié d'implémenter le premier)

- On ne charge pas tous les auteurs/livres en une requête (cela serait trop lourd), mais on a plutôt implémenté une pagination avec un `OnScrollListener` qui charge la prochaine page d'auteurs/livres lorsque l'on scrolle vers le bas. Cependant, cela implique une difficulté supplémentaire, celle de charger la bonne page. Lors de changements de fragments, l'index de la bonne page à charger n'est pas forcément réinitialisé, et des auteur/livres peuvent donc apparaître en doublons.


## Architecture du projet

Pour ce projet nous avons utilisé une architecture **MVVM**, cmme cela était requis dans le cahier des charges. Nous avons donc implémenter des ``Models`` pour nos données et des `ViewModels` pour faire le lien entre les ``Models`` et les `Views`, ces dernières étant directement prises en charge par Android Studio.

Nous avons aussi du utiliser des `Repository` pour exécuter les requêtes à `l'API` avec **RetroFit**, ainsi que des ``Adapter`` pour permettre l'affichage des `RecyclerView` avc des `ViewHolder`.

### Models

Afin de récupérer les différents auteurs et livres via l'API, il nous fallait convertir les données `JSON` en modèles. Nous avon utilisé pour cela une `GsonConverterFactory` dans la confguration de `retrofit` qui réalise cela automatiquement. Nous avons donc 3 modèles pour récupérer les données des requêtes:

- `Author` qui contient les données d'un auteur

- `Book` qui contient les données d'un livre

- `Tag` qui contient les données d'un tag (pour les livres)

En plus de cela, nous avons 2 autres modèles `AuthorRequest` et `BookRequest` utilisés cette fois-ci pour l'envoie de données vers l'API (car ils contiennent différents champs que leur modèle analogue), et ce afin de bien séparer les responsabilités.

### Fragments et Adapters

Conformément aux fonctionnalités implémentées, nous avons donc 6 fragments en tout, 3 pour les auteurs et 3 pour les livres. Les fragments pour les 2 parties sont quasiment pareils, en voici la liste:

- Fragment d'affichage d'un modèle (`AuthorFragment` et `BookFragment`) qui réalise notamment les fonctions suivantes:

  - Met en place le `RecyclerView` et le lie avec un nouvel `Adapter`
  
  - Met en place le `OnScrollListener`, la barre de recherche et le FAB

  - Charge les pages des auteurs/livres et met en place des obsevers pour réagir aux changements

  - Gère les actions pour les clics sur les items/boutons

- Fragment d'ajout d'un modèle (`AddAuthorFragment` et `AddBookFragment`) qui réalise notamment les fonctions suivantes:

  - Met en place les différents champs de saisie

  - Gère la saisie dans les champs et la validation du formulaire

  - Ajoute un auteur/livre via le `ViewModel`

- Fragment d'affichage des détails d'un modèle (`AuthorDetailsFragment` et `BookDetailsFragment`) qui réalise notamment les fonctions suivantes:

  - Met en place l'affichage des différents champs du modèle
    - pour un livre : récupère ses tags via le `ViewModel`
    - pour un auteur : récupère ses livres sous forme de ``RecyclerView``

  - Gère le clic sur le bouton de suppression

- Adapter d'un modèle (`AuthorAdapter` et `BookAdapter`) qui gère notamment les fonctions suivantes:

  - définit les interfaces pour les clicks sur les `Items` et sur les `Buttons`

  - lie les données à l'UI

  - instancie un `ViewHolder` pour optimiser l'affichage des composants dans le `RecyclerView`

### ViewModels

Nous avons également 2 `ViewModel`, un pour chaque modèle. Ils sont en charge de :

- gérer les données de l'UI, donc mettre à jour la liste des auteurs / de livres (via des `MutableLiveData` pour des données observables)

- appeler les méthodes dans le `Repository` pour effectuer des requêtes.

- restituer les données aux fragments quand appelé

### Repository

De la même manière, nous avons un ``Repository`` pour chaque modèle `Book` et `Author`, qui est en charge d'effectuer les requêtes à l'API afin de permettre l'ajout, la suppression ou la restitution de données. Les méthodes des `Repository` sont directement appelés par les `ViewModel` correspondants.

Les routes pour les requêtes de l'API sont définis dans une interface `APIService`.


## Difficultés recontrées et conclusion

Mis à part quelques bugs marginaux, nous avons implémenté toutes les fonctionnalités demandées dans le cachier des charges, en plus de bonus qui n'étaient pas requises. 

La pagination dans les `RecylerView` était la fonctionnalité qui nous a posé le plus de problèmes, d'autant plus que ce n'était pas la même pagination que lorsqu'on cherchait un auteur/livre, car il y avait alors des paramètres supplémentaires à prendre en compte. Cependant, c'était un bonus que nous nous sommes imposé par nous-mêmes, donc les bugs ne sont pas problématiques.