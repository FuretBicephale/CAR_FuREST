# Conception d'application réparties - Mise en place d'une passerelle REST communiquant avec un serveur FTP
#### Nicolas CACHERA - Pierre FALEZ
###### 24/02/2015

#### Introduction

Passerelle de communication de type REST permettant d'accéder à un serveur FTP

#### Architecture

Try/catch :
* IOException dans getDirectory()/GetRestRequest
* Socket/IO/FTPBadAnswerException dans processGetRequest()/RestRequest

Throw :
* -

#### Code Samples