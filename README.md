# Conception d'application réparties - Mise en place d'une passerelle REST communiquant avec un serveur FTP
#### Nicolas CACHERA - Pierre FALEZ
###### 24/02/2015

#### Introduction

Cette application est une passerelle de communication respectant les normes REST permettant d'accéder à un serveur FTP.

Pour faire fonctionner l'application, il est necessaire d'avoir un serveur FTP fonctionnel, qui est par defaut, à l'adresse 127.0.0.1 et sur le port 4223.

Ensuite, l'application sera accessible à l'aide d'un navigateur web en accédant à l'adresse adresseIP:2342/rest/api/ftp avec adresseIP l'adresse IP de la passerelle REST.

Cette application propose à l'utilisateur de naviguer dans les dossiers du serveur FTP et d'en récuperer les fichiers, de les supprimer ou d'en ajouter.

#### Utilisation (Il me semble que Monperrus nous avait conseillé de mettre ça à la fin du README, à confirmer)

#### Architecture

Cette application comprends 6 packages : rest.api, rest.config, rest.exception, rest.ftp, rest.ftp.output.html et rest.main.

Le package rest.main contient la méthode main de notre application qui lance notre passerelle ainsi que les ressources proposées par cette dernière.

Le package rest.api définie le chemin d'accès à l'application, "api" dans notre cas.

Le package rest.config définie la configuration de notre application, il rassemble aussi toutes les ressources disponibles dans notre application.

Le package rest.exception contient toutes les exceptions propres à notre application

Try/catch :
* IOException dans getDirectory()/GetRestRequest
* Socket/SocketTimeout/IO/FTPBadAnswerException dans processGetRequest()/RestToFTPRequest, process()/DeleteRestRequest, process()/PutRestRequest

Throw :
* Socket/SocketTimeout/IO/FTPBadAnswerException dans connect()/FTPSession
* IO/FTPBadAnswerException dans login()/FTPSession
* IOException dans close()/FTPSession, isDirectory()/FTPSession, getDirectory()/GetRestRequest

#### Code Samples
