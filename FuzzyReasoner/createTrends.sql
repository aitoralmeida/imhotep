--
-- Copyright (C) 2005-2009 University of Deusto
-- All rights reserved.
--
-- This software is licensed as described in the file COPYING, which
-- you should have received as part of this distribution.
--
-- This software consists of contributions made by many individuals, 
-- listed below:
--
-- Author: Pablo Orduña <pablo@ordunya.com>
-- 

-- SQL for creating the database in a MySQL server
-- $ mysql -uroot -p < createTrends.sql

DROP DATABASE IF EXISTS `PiramideTrendsFullTest`;
CREATE DATABASE `PiramideTrendsFullTest` ;

use `PiramideTrendsFullTest`;

-- -------------------------------------------------------------------------
-- --------------             DATABASE SESSION            ------------------
-- -------------------------------------------------------------------------

CREATE TABLE Downloaded (
    id              INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	device_name     CHAR( 255 ) NOT NULL,
    wurfl_id        CHAR( 255 ) NOT NULL, 
    marketing_name  CHAR( 255 ),
    brand_name      CHAR( 255 ), 
    model_name      CHAR( 255 ),
    real_height     FLOAT,
    real_width      FLOAT,
    reso_height     INTEGER,
    reso_width      INTEGER,

    who             CHAR ( 100 ),
    when_started    DATETIME,

    region          CHAR ( 100 ),
    value           TEXT,

    UNIQUE ( device_name, region )
) ENGINE = MyISAM;

CREATE TABLE Devices (
    id              INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	device_name     CHAR( 255 ) NOT NULL,
    wurfl_id        CHAR( 255 ) NOT NULL, 
    marketing_name  CHAR( 255 ),
    brand_name      CHAR( 255 ), 
    model_name      CHAR( 255 ),
    real_height     FLOAT,
    real_width      FLOAT,
    real_size       FLOAT, -- real_width * real_height
    reso_height     INTEGER,
    reso_width      INTEGER,
    reso_size       INTEGER, -- reso_width * reso_height

    UNIQUE ( device_name )
) ENGINE = MyISAM;

CREATE TABLE Trends (
    id              INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	device_name     CHAR( 255 ) NOT NULL,

    region          CHAR ( 100 ),

    month           INTEGER,
    year            INTEGER,
    value           FLOAT,
    error_margin    CHAR ( 100 ),

    UNIQUE ( device_name, region, month, year ),
    FOREIGN KEY (device_name) REFERENCES Devices(device_name)
) ENGINE = MyISAM;

INSERT INTO Devices(device_name, wurfl_id, marketing_name, brand_name, model_name, real_height, real_width, real_size, reso_height, reso_width, reso_size) 
            VALUES ("htc snap s523", "htc_snap_s523_ver1", NULL, "htc", "snap s523", 0, 0, 0, 240, 320, 76800 );

INSERT INTO Devices(device_name, wurfl_id, marketing_name, brand_name, model_name, real_height, real_width, real_size, reso_height, reso_width, reso_size) 
            VALUES ("sony ericsson w880iv", "sonyericsson_w880iv_ver1", NULL, "sony ericsson", "w880iv", 37, 27, 999, 0, 0, 0 );

INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   1 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   1 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   1 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   1 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   1 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   1 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   1 , 2010 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   2 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   2 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   2 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   2 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   2 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   2 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   2 , 2010 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   3 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   3 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   3 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   3 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   3 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   3 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   4 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   4 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   4 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   4 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   4 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   4 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   5 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   5 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   5 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   5 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   5 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   5 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   6 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   6 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   6 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   6 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   6 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   6 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   7 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   7 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   7 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   7 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   7 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   7 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   8 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   8 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   8 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   8 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   8 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   8 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   9 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   9 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   9 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   9 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   9 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,   9 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  10 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  10 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  10 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  10 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  10 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  10 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  11 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  11 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  11 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  11 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  11 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  11 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  12 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  12 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  12 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  12 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  12 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "all"  ,  12 , 2009 ,   25 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   1 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   1 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   1 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   1 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   1 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   1 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   1 , 2010 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   2 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   2 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   2 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   2 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   2 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   2 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   2 , 2010 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   3 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   3 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   3 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   3 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   3 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   3 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   4 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   4 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   4 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   4 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   4 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   4 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   5 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   5 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   5 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   5 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   5 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   5 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   6 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   6 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   6 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   6 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   6 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   6 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   7 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   7 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   7 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   7 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   7 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   7 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   8 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   8 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   8 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   8 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   8 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   8 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   9 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   9 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   9 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   9 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   9 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,   9 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  10 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  10 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  10 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  10 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  10 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  10 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  11 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  11 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  11 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  11 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  11 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  11 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  12 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  12 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  12 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  12 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  12 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "es"   ,  12 , 2009 ,   50 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   1 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   1 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   1 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   1 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   1 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   1 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   1 , 2010 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   2 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   2 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   2 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   2 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   2 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   2 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   2 , 2010 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   3 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   3 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   3 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   3 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   3 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   3 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   4 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   4 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   4 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   4 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   4 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   4 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   5 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   5 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   5 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   5 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   5 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   5 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   6 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   6 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   6 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   6 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   6 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   6 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   7 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   7 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   7 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   7 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   7 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   7 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   8 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   8 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   8 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   8 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   8 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   8 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   9 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   9 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   9 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   9 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   9 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,   9 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  10 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  10 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  10 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  10 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  10 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  10 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  11 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  11 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  11 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  11 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  11 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  11 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  12 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  12 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  12 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  12 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  12 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "htc snap s523"    , "jp"   ,  12 , 2009 ,   100 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   1 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   1 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   1 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   1 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   1 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   1 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   1 , 2010 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   2 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   2 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   2 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   2 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   2 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   2 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   2 , 2010 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   3 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   3 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   3 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   3 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   3 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   3 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   4 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   4 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   4 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   4 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   4 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   4 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   5 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   5 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   5 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   5 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   5 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   5 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   6 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   6 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   6 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   6 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   6 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   6 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   7 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   7 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   7 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   7 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   7 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   7 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   8 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   8 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   8 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   8 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   8 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   8 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   9 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   9 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   9 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   9 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   9 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,   9 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  10 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  10 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  10 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  10 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  10 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  10 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  11 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  11 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  11 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  11 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  11 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  11 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  12 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  12 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  12 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  12 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  12 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "all"  ,  12 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   1 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   1 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   1 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   1 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   1 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   1 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   1 , 2010 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   2 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   2 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   2 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   2 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   2 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   2 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   2 , 2010 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   3 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   3 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   3 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   3 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   3 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   3 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   4 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   4 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   4 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   4 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   4 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   4 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   5 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   5 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   5 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   5 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   5 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   5 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   6 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   6 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   6 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   6 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   6 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   6 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   7 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   7 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   7 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   7 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   7 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   7 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   8 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   8 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   8 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   8 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   8 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   8 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   9 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   9 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   9 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   9 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   9 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,   9 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  10 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  10 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  10 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  10 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  10 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  10 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  11 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  11 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  11 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  11 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  11 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  11 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  12 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  12 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  12 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  12 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  12 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "es"   ,  12 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   1 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   1 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   1 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   1 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   1 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   1 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   1 , 2010 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   2 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   2 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   2 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   2 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   2 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   2 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   2 , 2010 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   3 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   3 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   3 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   3 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   3 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   3 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   4 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   4 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   4 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   4 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   4 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   4 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   5 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   5 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   5 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   5 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   5 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   5 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   6 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   6 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   6 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   6 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   6 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   6 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   7 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   7 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   7 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   7 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   7 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   7 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   8 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   8 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   8 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   8 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   8 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   8 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   9 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   9 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   9 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   9 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   9 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,   9 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  10 , 2004 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  10 , 2005 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  10 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  10 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  10 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  10 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  11 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  11 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  11 , 2006 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  11 , 2007 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  11 , 2008 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  11 , 2009 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  12 , 2004 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  12 , 2005 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  12 , 2006 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  12 , 2007 ,   0 , ">10%; >10%; >10%; >10%; >10%");
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  12 , 2008 ,   0 , ">10%; >10%; >10%; >10%"   );
INSERT INTO Trends(device_name, region, month, year, value, error_margin) VALUES( "sony ericsson w880iv" , "jp"   ,  12 , 2009 ,   0 , ">10%; >10%; >10%; >10%"   );

GRANT SELECT, UPDATE, INSERT, DELETE ON * 
 	TO piramide@localhost IDENTIFIED BY 'piramide_password';

