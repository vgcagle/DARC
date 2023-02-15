-- phpMyAdmin SQL Dump
-- version 4.4.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:8889
-- Creato il: Feb 13, 2018 alle 16:25
-- Versione del server: 5.5.42
-- Versione PHP: 5.6.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `BarbiereSiviglia`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `Action`
--

CREATE TABLE `Action` (
  `idAction` int(11) NOT NULL,
  `printAction` mediumtext NOT NULL,
  `Unit_idUnit` int(11) DEFAULT NULL,
  `nameAction` mediumtext NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Agent`
--

CREATE TABLE `Agent` (
  `idAgent` int(11) NOT NULL,
  `printAgent` mediumtext NOT NULL,
  `nameAgent` varchar(45) DEFAULT NULL,
  `pleasantAgent` tinyint(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Annotator`
--

CREATE TABLE `Annotator` (
  `idAnnotator` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Attitude`
--

CREATE TABLE `Attitude` (
  `idAttitude` int(11) NOT NULL,
  `Attitude_idAgent` int(11) NOT NULL,
  `objectAttitude` varchar(200) NOT NULL,
  `typeAttitude` enum('liking','disliking') NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `CSSPlan`
--

CREATE TABLE `CSSPlan` (
  `id` int(11) NOT NULL,
  `Set` int(11) NOT NULL,
  `idState` varchar(150) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `CSSTimeline`
--

CREATE TABLE `CSSTimeline` (
  `id` int(11) NOT NULL,
  `Set` int(11) NOT NULL,
  `printSet` mediumtext,
  `idState` varchar(150) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Goal`
--

CREATE TABLE `Goal` (
  `idGoal` int(11) NOT NULL,
  `printGoal` varchar(45) NOT NULL,
  `nameGoal` mediumtext NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Pair`
--

CREATE TABLE `Pair` (
  `idPair` int(11) NOT NULL,
  `precedesPair` int(11) NOT NULL,
  `followsPair` int(11) NOT NULL,
  `Timeline_idTimeline` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Plan`
--

CREATE TABLE `Plan` (
  `idPlan` int(11) NOT NULL,
  `printPlan` mediumtext,
  `Agent_idAgent` int(11) DEFAULT NULL,
  `Goal_idGoal` int(11) DEFAULT NULL,
  `Action_idAction` int(11) DEFAULT NULL,
  `Timeline_idTimeline` int(11) DEFAULT NULL,
  `typePlan` enum('Rec','Base') NOT NULL,
  `preconditionsPlan` int(11) DEFAULT NULL,
  `effectsPlan` int(11) DEFAULT NULL,
  `namePlan` mediumtext NOT NULL,
  `mappingInit` int(10) DEFAULT NULL,
  `mappingEnd` int(10) DEFAULT NULL,
  `accomplished` tinyint(1) DEFAULT '1',
  `inConflictWithPlan` int(11) DEFAULT NULL,
  `inSupportOfPlan` int(11) DEFAULT NULL,
  `valueAtStakePlan_p` int(11) DEFAULT NULL,
  `valueBalancedPlan_p` int(11) DEFAULT NULL,
  `valueAtStakePlan_e` int(11) DEFAULT NULL,
  `valueBalancedPlan_e` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Plan2Plan`
--

CREATE TABLE `Plan2Plan` (
  `idPlan2Plan` int(11) NOT NULL,
  `idPlan1` int(11) NOT NULL,
  `idPlan2` int(11) NOT NULL,
  `relationType` enum('support_p','conflict_p') NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Plan2Value`
--

CREATE TABLE `Plan2Value` (
  `idPlan2Value` int(11) NOT NULL,
  `Plan_idPlan` int(11) NOT NULL,
  `Value_idValue` int(11) NOT NULL,
  `relationType` enum('conflict_p','support_p','conflict_e','support_e') NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Project`
--

CREATE TABLE `Project` (
  `idProject` int(11) NOT NULL,
  `nameProject` varchar(200) NOT NULL,
  `respProject` varchar(200) DEFAULT NULL,
  `descrProject` mediumtext,
  `dateProject` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `State`
--

CREATE TABLE `State` (
  `idState` int(11) NOT NULL,
  `typeState` enum('SOA','BEL','VAS','ACC','NIL','NEG') NOT NULL,
  `printState` mediumtext NOT NULL,
  `statusState` tinyint(1) NOT NULL,
  `nameState` mediumtext NOT NULL,
  `idValueAtStake` int(11) DEFAULT NULL,
  `idValueBalanced` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `SubplanOf`
--

CREATE TABLE `SubplanOf` (
  `idSubplanOf` int(11) NOT NULL,
  `idFatherPlan` int(11) NOT NULL COMMENT 'FK del piano genitore',
  `idChildPlan` int(11) NOT NULL COMMENT 'FK del piano figlio',
  `order` int(11) NOT NULL COMMENT 'Attributo della relazione: ordine del sottopiatto nel piano'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Text`
--

CREATE TABLE `Text` (
  `idText` int(11) NOT NULL,
  `textTitle` varchar(255) NOT NULL,
  `textAuthor` varchar(255) DEFAULT NULL,
  `textDescription` text,
  `textUrl` tinytext NOT NULL,
  `textContent` mediumtext
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Timeline`
--

CREATE TABLE `Timeline` (
  `idTimeline` int(11) NOT NULL,
  `effectsTimeline` int(11) NOT NULL,
  `preconditionsTimeline` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Unit`
--

CREATE TABLE `Unit` (
  `idUnit` int(11) NOT NULL,
  `printUnit` varchar(45) NOT NULL,
  `TimelineUnit` int(11) NOT NULL,
  `nameUnit` mediumtext,
  `Project_idProject` int(11) DEFAULT NULL,
  `text_idText` int(11) DEFAULT NULL,
  `startLine` int(11) DEFAULT NULL,
  `endLine` int(255) DEFAULT NULL,
  `textUnit` mediumtext,
  `idReferenceUnit` int(11) DEFAULT NULL,
  `isReference` enum('reference','annotation') NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Unit2Reference`
--

CREATE TABLE `Unit2Reference` (
  `idUnit2Reference` int(11) NOT NULL,
  `Unit_idUnit` int(11) NOT NULL,
  `idReferenceUnit` int(11) NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Unit2Value`
--

CREATE TABLE `Unit2Value` (
  `idUnit2Value` int(11) NOT NULL,
  `Unit_idUnit` int(11) NOT NULL,
  `Value_idValue` int(11) NOT NULL,
  `relationType` enum('conflict_p','support_p','conflict_e','support_e') NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Unit_Annotator`
--

CREATE TABLE `Unit_Annotator` (
  `idUnit_Annotator` int(11) NOT NULL,
  `Unit_idUnit` int(11) NOT NULL,
  `Annotator_idAnnotator` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `Value`
--

CREATE TABLE `Value` (
  `idValue` int(11) NOT NULL,
  `nameValue` text NOT NULL,
  `descriptionValue` text,
  `Agent_idAgent` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `Action`
--
ALTER TABLE `Action`
  ADD PRIMARY KEY (`idAction`),
  ADD KEY `fk_Action_Unit1_idx` (`Unit_idUnit`);

--
-- Indici per le tabelle `Agent`
--
ALTER TABLE `Agent`
  ADD PRIMARY KEY (`idAgent`);

--
-- Indici per le tabelle `Annotator`
--
ALTER TABLE `Annotator`
  ADD PRIMARY KEY (`idAnnotator`);

--
-- Indici per le tabelle `Attitude`
--
ALTER TABLE `Attitude`
  ADD PRIMARY KEY (`idAttitude`);

--
-- Indici per le tabelle `CSSPlan`
--
ALTER TABLE `CSSPlan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_State_CSSPlan_idx` (`idState`),
  ADD KEY `Set_CSSPlan_idx` (`Set`);

--
-- Indici per le tabelle `CSSTimeline`
--
ALTER TABLE `CSSTimeline`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Set_CSSTimeline_idx` (`Set`),
  ADD KEY `fk_State_CSSTimeline_idx` (`idState`);

--
-- Indici per le tabelle `Goal`
--
ALTER TABLE `Goal`
  ADD PRIMARY KEY (`idGoal`);

--
-- Indici per le tabelle `Pair`
--
ALTER TABLE `Pair`
  ADD PRIMARY KEY (`idPair`),
  ADD KEY `fk_Pair_Timeline1_idx` (`precedesPair`),
  ADD KEY `fk_followsPair_idx` (`followsPair`);

--
-- Indici per le tabelle `Plan`
--
ALTER TABLE `Plan`
  ADD PRIMARY KEY (`idPlan`),
  ADD KEY `fk_Plan_Agent_idx` (`Agent_idAgent`),
  ADD KEY `fk_Plan_Goal1_idx` (`Goal_idGoal`),
  ADD KEY `fk_Plan_Action1_idx` (`Action_idAction`),
  ADD KEY `fk_Plan_TImeline1_idx` (`Timeline_idTimeline`),
  ADD KEY `fk_preconditionsPlan_idx` (`preconditionsPlan`),
  ADD KEY `fk_effectsPlan_idx` (`effectsPlan`);

--
-- Indici per le tabelle `Plan2Plan`
--
ALTER TABLE `Plan2Plan`
  ADD PRIMARY KEY (`idPlan2Plan`);

--
-- Indici per le tabelle `Plan2Value`
--
ALTER TABLE `Plan2Value`
  ADD PRIMARY KEY (`idPlan2Value`);

--
-- Indici per le tabelle `Project`
--
ALTER TABLE `Project`
  ADD PRIMARY KEY (`idProject`);

--
-- Indici per le tabelle `State`
--
ALTER TABLE `State`
  ADD PRIMARY KEY (`idState`);

--
-- Indici per le tabelle `SubplanOf`
--
ALTER TABLE `SubplanOf`
  ADD PRIMARY KEY (`idSubplanOf`);

--
-- Indici per le tabelle `Text`
--
ALTER TABLE `Text`
  ADD PRIMARY KEY (`idText`);

--
-- Indici per le tabelle `Timeline`
--
ALTER TABLE `Timeline`
  ADD PRIMARY KEY (`idTimeline`),
  ADD KEY `effectsTimeline_idx` (`effectsTimeline`),
  ADD KEY `preconditionsTimeline_idx` (`preconditionsTimeline`);

--
-- Indici per le tabelle `Unit`
--
ALTER TABLE `Unit`
  ADD PRIMARY KEY (`idUnit`);

--
-- Indici per le tabelle `Unit2Reference`
--
ALTER TABLE `Unit2Reference`
  ADD PRIMARY KEY (`idUnit2Reference`);

--
-- Indici per le tabelle `Unit_Annotator`
--
ALTER TABLE `Unit_Annotator`
  ADD PRIMARY KEY (`idUnit_Annotator`);

--
-- Indici per le tabelle `Value`
--
ALTER TABLE `Value`
  ADD PRIMARY KEY (`idValue`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `Action`
--
ALTER TABLE `Action`
  MODIFY `idAction` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Agent`
--
ALTER TABLE `Agent`
  MODIFY `idAgent` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Annotator`
--
ALTER TABLE `Annotator`
  MODIFY `idAnnotator` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Attitude`
--
ALTER TABLE `Attitude`
  MODIFY `idAttitude` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `CSSPlan`
--
ALTER TABLE `CSSPlan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `CSSTimeline`
--
ALTER TABLE `CSSTimeline`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Goal`
--
ALTER TABLE `Goal`
  MODIFY `idGoal` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Pair`
--
ALTER TABLE `Pair`
  MODIFY `idPair` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Plan`
--
ALTER TABLE `Plan`
  MODIFY `idPlan` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Plan2Plan`
--
ALTER TABLE `Plan2Plan`
  MODIFY `idPlan2Plan` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Plan2Value`
--
ALTER TABLE `Plan2Value`
  MODIFY `idPlan2Value` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Project`
--
ALTER TABLE `Project`
  MODIFY `idProject` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `State`
--
ALTER TABLE `State`
  MODIFY `idState` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `SubplanOf`
--
ALTER TABLE `SubplanOf`
  MODIFY `idSubplanOf` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Text`
--
ALTER TABLE `Text`
  MODIFY `idText` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Timeline`
--
ALTER TABLE `Timeline`
  MODIFY `idTimeline` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Unit`
--
ALTER TABLE `Unit`
  MODIFY `idUnit` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Unit2Reference`
--
ALTER TABLE `Unit2Reference`
  MODIFY `idUnit2Reference` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Unit_Annotator`
--
ALTER TABLE `Unit_Annotator`
  MODIFY `idUnit_Annotator` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `Value`
--
ALTER TABLE `Value`
  MODIFY `idValue` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
