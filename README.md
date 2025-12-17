# SOAPCompteApp - Application Android de Gestion de Comptes Bancaires

Application Android native qui consomme un service SOAP pour gérer des comptes bancaires (COURANT et EPARGNE).

## 📋 Table des matières

- [Description](#description)
- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Structure du projet](#structure-du-projet)
- [Utilisation](#utilisation)
- [Service SOAP](#service-soap)
- [Captures d'écran](#captures-décran)
- [Auteur](#auteur)

## 📝 Description

SOAPCompteApp est une application Android développée en Kotlin qui permet de gérer des comptes bancaires via un service web SOAP. L'application offre une interface utilisateur moderne utilisant Material Design et permet d'effectuer des opérations CRUD (Create, Read, Delete) sur les comptes.

## ✨ Fonctionnalités

### ✅ Fonctionnalités implémentées

- **Afficher la liste des comptes** - Récupération et affichage de tous les comptes depuis le serveur SOAP
- **Ajouter un compte** - Création d'un nouveau compte avec un solde et un type (COURANT ou EPARGNE)
- **Supprimer un compte** - Suppression d'un compte existant avec confirmation
- **Actualisation automatique** - Rechargement de la liste après chaque opération
- **Gestion des erreurs** - Messages d'erreur clairs et logs de débogage

### 📊 Informations affichées pour chaque compte

- Numéro du compte (ID)
- Solde en DH (Dirhams)
- Type de compte (COURANT/EPARGNE) avec chip coloré
- Date de création

## 🏗️ Architecture

L'application suit le pattern **MVC** (Model-View-Controller) avec une séparation claire des responsabilités:

```
app/
├── beans/          # Modèles de données (Compte, TypeCompte)
├── ws/             # Service SOAP client
├── adapter/        # Adaptateurs RecyclerView
└── MainActivity    # Contrôleur principal
```

### Composants principaux

1. **MainActivity.kt** - Activité principale qui gère l'UI et la logique
2. **Service.kt** - Client SOAP pour communiquer avec le web service
3. **CompteAdapter.kt** - Adaptateur pour afficher la liste des comptes
4. **Compte.kt** - Classe de données représentant un compte
5. **TypeCompte.kt** - Énumération des types de compte

## 🛠️ Technologies utilisées

### Langages
- **Kotlin** - Langage principal (100%)

### Bibliothèques Android
- **AndroidX Core KTX** - Extensions Kotlin pour Android
- **AppCompat** - Compatibilité avec les anciennes versions d'Android
- **Material Components** - Interface utilisateur Material Design
- **RecyclerView** - Affichage de listes performant
- **Lifecycle & Coroutines** - Gestion asynchrone et cycle de vie

### Web Services
- **ksoap2-android 3.6.2** - Client SOAP pour Android

### Build & Gradle
- **Gradle 8.13** - Système de build
- **Android Gradle Plugin 8.13.2**
- **Kotlin 2.0.21**

## 📋 Prérequis

### Côté Android
- **Android Studio** Hedgehog ou plus récent
- **JDK 11** ou supérieur
- **Android SDK** avec API Level 24 minimum (Android 7.0)
- **Émulateur Android** ou appareil physique

### Côté Serveur
- **Serveur SOAP** fonctionnel avec les méthodes:
  - `getComptes()` - Récupérer tous les comptes
  - `createCompte(double solde, String type)` - Créer un compte
  - `deleteCompte(Long id)` - Supprimer un compte
- **URL du service** : `http://10.0.2.2:8082/services/ws`
- **Namespace** : `http://ws.tp_13.example.com/`

## 🚀 Installation

### 1. Cloner le projet
```bash
git clone <url-du-repo>
cd SOAPCompteApp
```

### 2. Ouvrir dans Android Studio
1. Ouvrez Android Studio
2. File → Open
3. Sélectionnez le dossier du projet
4. Attendez la synchronisation Gradle

### 3. Configuration du serveur SOAP
Assurez-vous que votre serveur SOAP est démarré et accessible:

```bash
# Pour tester depuis l'émulateur Android
# L'adresse 10.0.2.2 correspond à localhost de votre machine
curl http://10.0.2.2:8082/services/ws?wsdl
```

### 4. Compiler et exécuter
```bash
# Via ligne de commande
./gradlew assembleDebug

# Ou dans Android Studio
Run → Run 'app'
```

## ⚙️ Configuration

### Modifier l'URL du serveur SOAP

Dans `Service.kt`, modifiez les constantes:

```kotlin
private val NAMESPACE = "http://ws.tp_13.example.com/"
private val URL = "http://10.0.2.2:8082/services/ws"
```

**Notes:**
- `10.0.2.2` : localhost depuis l'émulateur Android
- Pour un appareil physique, utilisez l'IP de votre machine (ex: `http://192.168.1.100:8082/services/ws`)

## 📁 Structure du projet

```
SOAPCompteApp/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/ma/projet/soapclient/
│   │   │   │   ├── MainActivity.kt           # Activité principale
│   │   │   │   ├── adapter/
│   │   │   │   │   └── CompteAdapter.kt      # Adaptateur RecyclerView
│   │   │   │   ├── beans/
│   │   │   │   │   ├── Compte.kt             # Modèle Compte
│   │   │   │   │   └── TypeCompte.kt         # Enum TypeCompte
│   │   │   │   ├── ws/
│   │   │   │   │   └── Service.kt            # Client SOAP
│   │   │   │   └── ui/theme/                 # Thème Compose
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml     # Layout principal
│   │   │   │   │   ├── item.xml              # Layout item compte
│   │   │   │   │   └── popup.xml             # Dialog ajout compte
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml            # Couleurs
│   │   │   │   │   ├── strings.xml           # Textes
│   │   │   │   │   └── themes.xml            # Thème Material
│   │   │   │   └── ...
│   │   │   └── AndroidManifest.xml
│   │   └── ...
│   └── build.gradle.kts
│
├── gradle/
│   ├── libs.versions.toml                    # Versions des dépendances
│   └── wrapper/
│
├── build.gradle.kts
├── settings.gradle.kts
└── README.md                                  # Ce fichier
```

## 📱 Utilisation

### Lancer l'application

1. **Démarrez le serveur SOAP** (port 8082)
2. **Lancez l'application** sur l'émulateur ou appareil
3. La liste des comptes s'affiche automatiquement

### Ajouter un compte

1. Cliquez sur le bouton **"Ajouter"**
2. Entrez le **solde** (ex: 1000.0)
3. Sélectionnez le **type** (COURANT ou EPARGNE)
4. Cliquez sur **"Ajouter"**
5. ✅ Le compte apparaît dans la liste

### Supprimer un compte

1. Cliquez sur le bouton **"Supprimer"** d'un compte
2. Confirmez la suppression
3. ✅ Le compte disparaît de la liste

### Logs de débogage

Les logs sont affichés dans Logcat (filtre: `System.out`):

```
loadComptes: Début du chargement...
loadComptes: 6 comptes récupérés
  - Compte #1: 1500.5 DH, COURANT
  - Compte #2: 3200.75 DH, EPARGNE
  ...
loadComptes: Liste mise à jour dans l'adaptateur
```

## 🌐 Service SOAP

### Méthodes supportées

#### 1. getComptes()
```xml
<soap:Envelope>
  <soap:Body>
    <ns:getComptes xmlns:ns="http://ws.tp_13.example.com/"/>
  </soap:Body>
</soap:Envelope>
```

**Retour:** Liste de comptes avec id, solde, type, dateCreation

#### 2. createCompte(double solde, String type)
```xml
<soap:Envelope>
  <soap:Body>
    <ns:createCompte xmlns:ns="http://ws.tp_13.example.com/">
      <solde>1000.0</solde>
      <type>COURANT</type>
    </ns:createCompte>
  </soap:Body>
</soap:Envelope>
```

**Retour:** boolean (succès/échec)

#### 3. deleteCompte(Long id)
```xml
<soap:Envelope>
  <soap:Body>
    <ns:deleteCompte xmlns:ns="http://ws.tp_13.example.com/">
      <id>7</id>
    </ns:deleteCompte>
  </soap:Body>
</soap:Envelope>
```

**Retour:** boolean (succès/échec)

## 🎨 Interface Utilisateur

### Thème
- **Material Components** - Design moderne
- **Couleurs:**
  - Primary: Purple 500 (#6200EE)
  - Primary Dark: Purple 700 (#3700B3)
  - Accent: Teal 200 (#03DAC5)
  - Bouton Supprimer: Red (#D32F2F)

### Composants
- **RecyclerView** - Liste déroulante des comptes
- **MaterialButton** - Boutons d'action
- **MaterialAlertDialog** - Dialogues de confirmation
- **Chip** - Affichage du type de compte
- **TextInputEditText** - Saisie du solde

## 🐛 Dépannage

### Problème: "Cannot connect to server"
**Solution:** Vérifiez que:
- Le serveur SOAP est démarré
- L'URL est correcte (`10.0.2.2` pour émulateur)
- Le port 8082 est ouvert
- La permission INTERNET est dans le manifest

### Problème: "SOAP Fault: method not recognized"
**Solution:** Vérifiez que:
- Le namespace est correct
- Les noms de méthodes correspondent au WSDL
- Les paramètres sont correctement nommés

### Problème: "Les IDs ne s'affichent pas dans l'IDE"
**Solution:**
1. `File` → `Invalidate Caches...`
2. Cochez "Clear file system cache"
3. Cliquez sur "Invalidate and Restart"

## 📄 Licence

Ce projet est un travail pratique éducatif.

## 👨‍💻 Auteur

**Projet TP** - Gestion de comptes bancaires avec SOAP

---

[Tp14.webm](https://github.com/user-attachments/assets/c5c2478b-8fc9-4900-8df0-09acfc3ae20c)

## 📚 Ressources

- [Documentation Android](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [ksoap2-android](https://github.com/simpligility/ksoap2-android)
- [Material Design](https://material.io/develop/android)

---

**Date de création:** Décembre 2024  
**Dernière mise à jour:** 17 Décembre 2024  
**Version:** 1.0

