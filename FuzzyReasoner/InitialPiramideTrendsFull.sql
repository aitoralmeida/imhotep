DROP DATABASE IF EXISTS `PiramideTrendsFull`;
CREATE DATABASE `PiramideTrendsFull`;

USE PiramideTrendsFull;

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `PiramideTrendsFull`
--

-- --------------------------------------------------------

--
-- Table structure for table `Downloaded`
--

CREATE TABLE IF NOT EXISTS `Downloaded` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_name` char(255) NOT NULL,
  `wurfl_id` char(255) NOT NULL,
  `marketing_name` char(255) DEFAULT NULL,
  `brand_name` char(255) DEFAULT NULL,
  `model_name` char(255) DEFAULT NULL,
  `real_height` float DEFAULT NULL,
  `real_width` float DEFAULT NULL,
  `reso_height` int(11) DEFAULT NULL,
  `reso_width` int(11) DEFAULT NULL,
  `who` char(100) DEFAULT NULL,
  `when_started` datetime DEFAULT NULL,
  `region` char(100) DEFAULT NULL,
  `value` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_name` (`device_name`,`region`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14212 ;

-- --------------------------------------------------------

--
-- Table structure for table `Devices`
--

CREATE TABLE IF NOT EXISTS `Devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_name` char(255) NOT NULL,
  `wurfl_id` char(255) NOT NULL,
  `marketing_name` char(255) DEFAULT NULL,
  `brand_name` char(255) DEFAULT NULL,
  `model_name` char(255) DEFAULT NULL,
  `real_height` float DEFAULT NULL,
  `real_width` float DEFAULT NULL,
  `real_size` float DEFAULT NULL,
  `reso_height` int(11) DEFAULT NULL,
  `reso_width` int(11) DEFAULT NULL,
  `reso_size` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_name` (`device_name`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=66427 ;

-- --------------------------------------------------------

--
-- Table structure for table `Trends`
--

CREATE TABLE IF NOT EXISTS `Trends` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_name` char(255) NOT NULL,
  `region` char(100) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `value` float DEFAULT NULL,
  `error_margin` char(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_name` (`device_name`,`region`,`month`,`year`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9595097 ;


GRANT ALL PRIVILEGES ON PiramideTrendsFull.* TO 'piramide'@'localhost' IDENTIFIED BY 'piramide_password';
