# Hytale Master Angler

**Hytale Master Angler** est un plugin complet pour Hytale qui transforme l'expérience de pêche en un système RPG immersif et technique. Fini le simple clic-droit : préparez votre équipement, gérez la tension de la ligne et devenez le Maître Pêcheur !

## 🎣 Fonctionnalités Principales

### 1. Système de Canne à Pêche Modulaire
Créez votre propre canne à pêche unique en assemblant 4 composants distincts sur l'**Établi de Pêche** :
*   **Corps (Body)** : Détermine la durabilité et la résistance de la canne.
*   **Moulinet (Reel)** : Influence la vitesse de récupération (Reeling Speed).
*   **Ligne (Line)** : Définit la tension maximale supportée avant la rupture.
*   **Appât (Bait)** : Attire des types de poissons spécifiques (Biomes, Rareté).

### 2. Mécanique de Tension Réaliste
La pêche n'est plus automatique. Une fois le poisson ferré, un mini-jeu de tension s'active :
*   Une jauge de tension (UI Client) apparaît à l'écran.
*   Vous devez maintenir la tension dans la zone verte en moulinant ou en relâchant la ligne.
*   Si la tension est trop forte, la ligne casse. Si elle est trop faible, le poisson s'échappe.

### 3. Écosystème de Poissons Complet
*   Plus de **20 espèces de poissons** uniques (Carpe, Brochet, Thon, etc.).
*   Chaque poisson possède des statistiques générées dynamiquement : **Poids** et **Taille**.
*   Système de rareté influencé par votre **Puissance de Pêche** (Fishing Power).

### 4. Progression et RPG
*   **Système d'XP et de Niveaux** : Gagnez de l'expérience à chaque prise. L'XP dépend de la difficulté et de la taille du poisson.
*   **Armures de Pêcheur** : Équipez des sets complets (Chapeau, Gilet, Pantalon, Bottes) pour obtenir des bonus de statistiques et de l'XP supplémentaire.

### 5. Loot et Trésors
En plus des poissons, vous pouvez pêcher des **Caisses de Butin** (Bois, Fer, Or) contenant des matériaux précieux pour le crafting (Fibre de carbone, Titane, etc.).

## 🛠️ Installation

1.  Téléchargez le fichier `Hytale_MasterAngler-1.0.0.jar` depuis les **Releases**.
2.  Placez le fichier dans le dossier `plugins/` de votre serveur Hytale.
3.  Lancez le serveur. Le plugin générera automatiquement les fichiers de configuration nécessaires.

## 💻 Commandes

La commande principale est `/angler`.

| Commande | Description | Permission |
| :--- | :--- | :--- |
| `/angler level` | Affiche votre niveau de pêche et votre progression actuelle. | Tout le monde |
| `/angler xp` | Affiche votre montant exact d'XP. | Tout le monde |
| `/angler equip` | Ouvre l'interface d'équipement pour modifier votre canne. | Tout le monde |
| `/angler rod` | Donne une canne à pêche de test (Debug). | Admin |
| `/angler workbench` | Donne l'établi de pêche (Fishing Workbench). | Admin |
| `/angler repair` | Restaure la durabilité de la canne en main. | Admin |

## 🏗️ Développement et Build

Ce projet utilise **Gradle** et **Java 21**.

### Prérequis
*   JDK 21
*   Un fichier `HytaleServer.jar` valide (placé dans le dossier `Libs/`).

### Compiler le projet
```bash
./gradlew shadowJar
```
Le fichier JAR sera généré dans `build/libs/`.

### Tester localement
```bash
./gradlew runServer
```

---
*Développé par Jules pour la communauté Hytale.*
