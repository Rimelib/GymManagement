-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 13 jan. 2026 à 20:03
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `salle_sport`
--

-- --------------------------------------------------------

--
-- Structure de la table `abonnement`
--

CREATE TABLE `abonnement` (
                              `id_abonnement` int(11) NOT NULL,
                              `type` varchar(50) NOT NULL,
                              `prix` decimal(10,2) NOT NULL,
                              `duree` int(11) NOT NULL,
                              `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `abonnement`
--

INSERT INTO `abonnement` (`id_abonnement`, `type`, `prix`, `duree`, `description`) VALUES
                                                                                       (1, 'Mensuel', 300.00, 1, 'Accès 1 mois'),
                                                                                       (2, 'Trimestriel', 800.00, 3, 'Accès 3 mois'),
                                                                                       (3, 'Annuel', 2500.00, 12, 'Accès 1 an');

-- --------------------------------------------------------

--
-- Structure de la table `coach`
--

CREATE TABLE `coach` (
                         `id_coach` int(11) NOT NULL,
                         `nom` varchar(50) NOT NULL,
                         `prenom` varchar(50) NOT NULL,
                         `specialite` varchar(50) DEFAULT NULL,
                         `telephone` varchar(20) DEFAULT NULL,
                         `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `coach`
--

INSERT INTO `coach` (`id_coach`, `nom`, `prenom`, `specialite`, `telephone`, `email`) VALUES
                                                                                          (1, 'Benali', 'Karim', 'Musculation', '0612345678', 'karim@gym.com'),
                                                                                          (2, 'Alami', 'Sara', 'Cardio', '0623456789', 'sara@gym.com');

-- --------------------------------------------------------

--
-- Structure de la table `membre`
--

CREATE TABLE `membre` (
                          `id_membre` int(11) NOT NULL,
                          `nom` varchar(50) NOT NULL,
                          `prenom` varchar(50) NOT NULL,
                          `date_naissance` date DEFAULT NULL,
                          `email` varchar(100) DEFAULT NULL,
                          `telephone` varchar(20) DEFAULT NULL,
                          `date_inscription` date DEFAULT NULL,
                          `abonnement_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `membre`
--

INSERT INTO `membre` (`id_membre`, `nom`, `prenom`, `date_naissance`, `email`, `telephone`, `date_inscription`, `abonnement_id`) VALUES
                                                                                                                                     (1, 'Ahmed', 'Said', '1995-03-15', 'ahmed@example.com', '0634567890', '2026-01-10', 1),
                                                                                                                                     (2, 'Fatima', 'Zahra', '1998-07-22', 'fatima@example.com', '0645678901', '2026-01-11', 2);

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

CREATE TABLE `paiement` (
                            `id_paiement` int(11) NOT NULL,
                            `montant` decimal(10,2) NOT NULL,
                            `date` date NOT NULL,
                            `membre_id` int(11) DEFAULT NULL,
                            `abonnement_id` int(11) DEFAULT NULL,
                            `methode_paiement` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `seance`
--

CREATE TABLE `seance` (
                          `id_seance` int(11) NOT NULL,
                          `date` date NOT NULL,
                          `heure` time NOT NULL,
                          `type` varchar(50) DEFAULT NULL,
                          `coach_id` int(11) DEFAULT NULL,
                          `membre_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `abonnement`
--
ALTER TABLE `abonnement`
    ADD PRIMARY KEY (`id_abonnement`);

--
-- Index pour la table `coach`
--
ALTER TABLE `coach`
    ADD PRIMARY KEY (`id_coach`);

--
-- Index pour la table `membre`
--
ALTER TABLE `membre`
    ADD PRIMARY KEY (`id_membre`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `abonnement_id` (`abonnement_id`);

--
-- Index pour la table `paiement`
--
ALTER TABLE `paiement`
    ADD PRIMARY KEY (`id_paiement`),
  ADD KEY `membre_id` (`membre_id`),
  ADD KEY `abonnement_id` (`abonnement_id`);

--
-- Index pour la table `seance`
--
ALTER TABLE `seance`
    ADD PRIMARY KEY (`id_seance`),
  ADD KEY `coach_id` (`coach_id`),
  ADD KEY `membre_id` (`membre_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `abonnement`
--
ALTER TABLE `abonnement`
    MODIFY `id_abonnement` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `coach`
--
ALTER TABLE `coach`
    MODIFY `id_coach` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `membre`
--
ALTER TABLE `membre`
    MODIFY `id_membre` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `paiement`
--
ALTER TABLE `paiement`
    MODIFY `id_paiement` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `seance`
--
ALTER TABLE `seance`
    MODIFY `id_seance` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `membre`
--
ALTER TABLE `membre`
    ADD CONSTRAINT `membre_ibfk_1` FOREIGN KEY (`abonnement_id`) REFERENCES `abonnement` (`id_abonnement`);

--
-- Contraintes pour la table `paiement`
--
ALTER TABLE `paiement`
    ADD CONSTRAINT `paiement_ibfk_1` FOREIGN KEY (`membre_id`) REFERENCES `membre` (`id_membre`),
  ADD CONSTRAINT `paiement_ibfk_2` FOREIGN KEY (`abonnement_id`) REFERENCES `abonnement` (`id_abonnement`);

--
-- Contraintes pour la table `seance`
--
ALTER TABLE `seance`
    ADD CONSTRAINT `seance_ibfk_1` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id_coach`),
  ADD CONSTRAINT `seance_ibfk_2` FOREIGN KEY (`membre_id`) REFERENCES `membre` (`id_membre`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
