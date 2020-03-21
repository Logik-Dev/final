#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: Adresse
#------------------------------------------------------------

CREATE TABLE Adresse(
        id          Int  Auto_increment  NOT NULL ,
        libelle     Varchar (255) NOT NULL ,
        code_postal Mediumint NOT NULL ,
        ville       Varchar (50) NOT NULL ,
        longitude   Decimal (12,8) NOT NULL ,
        latitude    Decimal (12,8) NOT NULL
	,CONSTRAINT Adresse_PK PRIMARY KEY (id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Utilisateur
#------------------------------------------------------------

CREATE TABLE Utilisateur(
        id            Int  Auto_increment  NOT NULL ,
        prenom        Varchar (50) NOT NULL ,
        nom           Varchar (50) NOT NULL ,
        mot_de_passe  Varchar (50) NOT NULL ,
        email         Varchar (100) NOT NULL ,
        compte_valide Bool NOT NULL ,
        roles         Enum ("ADMIN","UTILISATEUR") NOT NULL ,
        id_Adresse    Int NOT NULL
	,CONSTRAINT Utilisateur_PK PRIMARY KEY (id)

	,CONSTRAINT Utilisateur_Adresse_FK FOREIGN KEY (id_Adresse) REFERENCES Adresse(id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Type
#------------------------------------------------------------

CREATE TABLE Type(
        id      Int  Auto_increment  NOT NULL ,
        libelle Varchar (20) NOT NULL
	,CONSTRAINT Type_PK PRIMARY KEY (id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Salle
#------------------------------------------------------------

CREATE TABLE Salle(
        id             Int  Auto_increment  NOT NULL ,
        prix           Decimal (6,2) NOT NULL ,
        taille         Decimal (8,2) NOT NULL ,
        capacite_max   Smallint NOT NULL ,
        note           TinyINT NOT NULL ,
        id_Utilisateur Int NOT NULL ,
        id_Adresse     Int NOT NULL ,
        id_Type        Int NOT NULL
	,CONSTRAINT Salle_PK PRIMARY KEY (id)

	,CONSTRAINT Salle_Utilisateur_FK FOREIGN KEY (id_Utilisateur) REFERENCES Utilisateur(id)
	,CONSTRAINT Salle_Adresse0_FK FOREIGN KEY (id_Adresse) REFERENCES Adresse(id)
	,CONSTRAINT Salle_Type1_FK FOREIGN KEY (id_Type) REFERENCES Type(id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Commentaire
#------------------------------------------------------------

CREATE TABLE Commentaire(
        id               Int  Auto_increment  NOT NULL ,
        contenu          Text NOT NULL ,
        note             TinyINT NOT NULL ,
        date_publication Date NOT NULL ,
        id_Utilisateur   Int NOT NULL ,
        id_Salle         Int NOT NULL
	,CONSTRAINT Commentaire_PK PRIMARY KEY (id)

	,CONSTRAINT Commentaire_Utilisateur_FK FOREIGN KEY (id_Utilisateur) REFERENCES Utilisateur(id)
	,CONSTRAINT Commentaire_Salle0_FK FOREIGN KEY (id_Salle) REFERENCES Salle(id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Réservation
#------------------------------------------------------------

CREATE TABLE Reservation(
        id                      Int  Auto_increment  NOT NULL ,
        debut                   Date NOT NULL ,
        fin                     Date NOT NULL ,
        recurrence_hebdomadaire TinyINT NOT NULL ,
        statut                  Varchar (20) NOT NULL ,
        prix                    Mediumint NOT NULL ,
        id_Utilisateur          Int NOT NULL ,
        id_Salle                Int NOT NULL
	,CONSTRAINT Reservation_PK PRIMARY KEY (id)

	,CONSTRAINT Reservation_Utilisateur_FK FOREIGN KEY (id_Utilisateur) REFERENCES Utilisateur(id)
	,CONSTRAINT Reservation_Salle0_FK FOREIGN KEY (id_Salle) REFERENCES Salle(id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Equipement
#------------------------------------------------------------

CREATE TABLE Equipement(
        id      Int  Auto_increment  NOT NULL ,
        libelle Varchar (20) NOT NULL
	,CONSTRAINT Equipement_PK PRIMARY KEY (id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Photo
#------------------------------------------------------------

CREATE TABLE Photo(
        id       Int  Auto_increment  NOT NULL ,
        fichier  Longblob NOT NULL ,
        id_Salle Int NOT NULL
	,CONSTRAINT Photo_PK PRIMARY KEY (id)

	,CONSTRAINT Photo_Salle_FK FOREIGN KEY (id_Salle) REFERENCES Salle(id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Notifications
#------------------------------------------------------------

CREATE TABLE Notifications(
        id             Int  Auto_increment  NOT NULL ,
        id_reservation Int NOT NULL
	,CONSTRAINT Notifications_PK PRIMARY KEY (id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Avoir6
#------------------------------------------------------------

CREATE TABLE Avoir6(
        id       Int NOT NULL ,
        id_Salle Int NOT NULL
	,CONSTRAINT Avoir6_PK PRIMARY KEY (id,id_Salle)

	,CONSTRAINT Avoir6_Equipement_FK FOREIGN KEY (id) REFERENCES Equipement(id)
	,CONSTRAINT Avoir6_Salle0_FK FOREIGN KEY (id_Salle) REFERENCES Salle(id)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Avoir2
#------------------------------------------------------------

CREATE TABLE Avoir2(
        id             Int NOT NULL ,
        id_Utilisateur Int NOT NULL
	,CONSTRAINT Avoir2_PK PRIMARY KEY (id,id_Utilisateur)

	,CONSTRAINT Avoir2_Notifications_FK FOREIGN KEY (id) REFERENCES Notifications(id)
	,CONSTRAINT Avoir2_Utilisateur0_FK FOREIGN KEY (id_Utilisateur) REFERENCES Utilisateur(id)
)ENGINE=InnoDB;

