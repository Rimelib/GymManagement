-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 17 jan. 2026 à 21:00
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.0.30

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
                                                                                       (3, 'Annuel', 2500.00, 12, 'Accès 1 an'),
                                                                                       (4, 'Hebdomadaire', 100.00, 0, 'Accès 1 semaine'),
                                                                                       (5, 'Semestriel', 1500.00, 6, 'Accès 6 mois');

-- --------------------------------------------------------

--
-- Structure de la table `administrateur`
--

CREATE TABLE `administrateur` (
                                  `id_admin` int(11) NOT NULL,
                                  `username` varchar(50) NOT NULL,
                                  `password` varchar(255) NOT NULL,
                                  `nom` varchar(50) NOT NULL,
                                  `prenom` varchar(50) NOT NULL,
                                  `email` varchar(100) DEFAULT NULL,
                                  `date_creation` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `administrateur`
--

INSERT INTO `administrateur` (`id_admin`, `username`, `password`, `nom`, `prenom`, `email`, `date_creation`) VALUES
                                                                                                                 (1, 'admin', 'admin123', 'Administrateur', 'Principal', 'admin@salledesport.ma', '2026-01-01'),
                                                                                                                 (2, 'rim', 'rim123', 'EL IBRAHIMI', 'Rim', 'rim@salledesport.ma', '2026-01-01'),
                                                                                                                 (3, 'aya', 'aya123', 'AKHALOUI', 'Aya', 'aya@salledesport.ma', '2026-01-01');

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
                                                                                          (2, 'Alami', 'Sara', 'Cardio', '0623456789', 'sara@gym.com'),
                                                                                          (3, 'Hamdaoui', 'Youssef', 'Yoga', '0656789012', 'youssef@gym.com'),
                                                                                          (4, 'Tarek', 'Amina', 'CrossFit', '0667890123', 'amina@gym.com'),
                                                                                          (5, 'Chakir', 'Hassan', 'Boxe', '0678901234', 'hassan@gym.com');

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
                                                                                                                                     (2, 'Fatima', 'Zahra', '1998-07-22', 'fatima@example.com', '0645678901', '2026-01-11', 2),
                                                                                                                                     (3, 'Khalil', 'Mohamed', '1992-11-08', 'mohamed.k@example.com', '0656123456', '2025-12-15', 3),
                                                                                                                                     (4, 'Bennani', 'Leila', '2000-05-14', 'leila.b@example.com', '0667234567', '2026-01-05', 1),
                                                                                                                                     (5, 'Idrissi', 'Omar', '1996-09-20', 'omar.i@example.com', '0678345678', '2026-01-08', 2),
                                                                                                                                     (6, 'Rachidi', 'Salma', '1994-02-28', 'salma.r@example.com', '0689456789', '2025-11-20', 3),
                                                                                                                                     (7, 'Mansouri', 'Karim', '1999-12-03', 'karim.m@example.com', '0690567890', '2026-01-12', 4);

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

--
-- Déchargement des données de la table `paiement`
--

INSERT INTO `paiement` (`id_paiement`, `montant`, `date`, `membre_id`, `abonnement_id`, `methode_paiement`) VALUES
                                                                                                                (1, 300.00, '2026-01-10', 1, 1, 'Carte bancaire'),
                                                                                                                (2, 800.00, '2026-01-11', 2, 2, 'Espèces'),
                                                                                                                (3, 2500.00, '2025-12-15', 3, 3, 'Virement'),
                                                                                                                (4, 300.00, '2026-01-05', 4, 1, 'Carte bancaire'),
                                                                                                                (5, 800.00, '2026-01-08', 5, 2, 'Espèces'),
                                                                                                                (6, 2500.00, '2025-11-20', 6, 3, 'Carte bancaire'),
                                                                                                                (7, 100.00, '2026-01-12', 7, 4, 'Espèces');

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
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`id_seance`, `date`, `heure`, `type`, `coach_id`, `membre_id`) VALUES
                                                                                         (1, '2026-01-15', '08:00:00', 'Musculation', 1, 1),
                                                                                         (2, '2026-01-15', '09:00:00', 'Cardio', 2, 2),
                                                                                         (3, '2026-01-15', '10:00:00', 'Yoga', 3, 3),
                                                                                         (4, '2026-01-16', '08:30:00', 'Musculation', 1, 4),
                                                                                         (5, '2026-01-16', '10:00:00', 'CrossFit', 4, 5),
                                                                                         (6, '2026-01-17', '09:00:00', 'Boxe', 5, 6),
                                                                                         (7, '2026-01-17', '11:00:00', 'Yoga', 3, 7),
                                                                                         (8, '2026-01-18', '08:00:00', 'Cardio', 2, 1),
                                                                                         (9, '2026-01-18', '14:00:00', 'CrossFit', 4, 3),
                                                                                         (10, '2026-01-19', '09:30:00', 'Boxe', 5, 2);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `abonnement`
--
ALTER TABLE `abonnement`
    ADD PRIMARY KEY (`id_abonnement`);

--
-- Index pour la table `administrateur`
--
ALTER TABLE `administrateur`
    ADD PRIMARY KEY (`id_admin`),
    ADD UNIQUE KEY `username` (`username`);

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
    MODIFY `id_abonnement` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `administrateur`
--
ALTER TABLE `administrateur`
    MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `coach`
--
ALTER TABLE `coach`
    MODIFY `id_coach` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `membre`
--
ALTER TABLE `membre`
    MODIFY `id_membre` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `paiement`
--
ALTER TABLE `paiement`
    MODIFY `id_paiement` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `seance`
--
ALTER TABLE `seance`
    MODIFY `id_seance` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

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
