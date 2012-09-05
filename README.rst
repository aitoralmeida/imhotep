Introduction
============

Imhotep is a part of the `PIRAmIDE project <http://www.piramidepse.com/>`_ which
aims to ease the development of accessible and adaptable user interfaces. In
order to do so, developers write code defining some preprocessor directives,
taking into account both the user capabilities and the device characteristics.
Then, applications can be uploaded to a repository. Finally, users will use an
App Downloader to see the available applications and to download those
applications compiled for them.

One of the most interesting things of Imhotep is the level of expressivity of
the preprocessor directives. Developers can establish their own variables and
rules such as::

    IF screensize IS big AND resolution IS normal
    THEN video IS high;
    IF screensize IS big AND RESOLUTION IS big
    THEN video IS very_high;

Where the variables, rules, and possible values of the variables are defined by
the developer with the web based wizard. Furthermore, the concepts of resolution
is big is created by the system taking into account the information of the
mobile devices (provided by `WURFL <http://wurfl.sourceforge.net/>`_) and
pondering it with their popularity (with `Google Trends
<http://www.google.com/trends/>`_ data).

You can find more detailed information in http://www.morelab.deusto.es/imhotep/.

Architecture
============

Imhotep is divided into 4 different modules:

* App downloader: users will install this application in their mobile device to connect to the repository and download adapted applications.
* The REST server: it contains the application repository. Once users select a desired application, the server will:
  * Use the Fuzzy Knowledge-Eliciting Reasoner to infer new values with the user and device configuration and the variables and rules established by the developer of the application.
  * Call the Preprocessor to select only the code that the final user requires.
  * Delegate the compilation of the application to the Compiler Manager. Currently the REST server only supports Android, but other compilers could be easily added.
  * Store the compiled application in the Compilation Cache, so future requests will not require to pass through the whole process if they have the same configuration values.
* Wizard: developers will use the wizard to establish the variables and the possible values of those variables. It uses the trends database to show the different values given a concrete user and a concrete device.
* Capacity Tester: users will need to create a user profile with their capabilities. Capacity Tester will gather those capabilities by testing them.  Current application is just a proof of concept of how the Capacity Tester should work.

Wizard
~~~~~~

The wizard is a web application that can be used by developers to play with the
fuzzy values and create the proper profile and device configuration. It has been
developed using Google Web Toolkit and it has been publicly deployed at:

* http://www.morelab.deusto.es/imhotep/wizard/

Preprocessor
~~~~~~~~~~~~

The preprocessor directives define how the final source code must be generated,
providing conditions for certain regions of code to be added or skipped, and
adding Imhotep variables that the preprocessor will adapt for each compilation.
The preprocessor identifies the directives when they start by //# in languages
that support inline comments starting by //, such as Java, C# or C++, #// in
languages that support inline comments starting by #, such as Python or Perl,
and '// in VB.NET.

The preprocessor can avoid the compilation of fragments of code if certain
conditions are matched. These conditions can include calls to functions provided
by the system. Basic string and math functions are available, including
lowercase, trim, contains, round or sqrt, as well as functions to check if a
certain variable is available. The conditions can be embedded, as shown in the
image below. The syntax of the conditions is based on the syntax used by the
Python programming language.

Developers ask for user and device capabilities in these conditions. For
example, one directive could state that if the user is blind the application
should use a voice based interface. We have defined five categories for user
capabilities that can be seen below:

Piramide Application Downloader
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

In order to download applications, a client is required. We provide the App
downloader, an Android mobile application, which lets users select their profile
(at the moment selecting among Regular or Blind, this part should be integrated
with the Capacity Tester), and the URL of the REST Server (which defaults to our
sample repository). With this information, the downloader shows the applications
deployed in the remote repository and lets users select a version of an
application and the application will be compiled in the server and downloaded
and installed by the downloader.

Piramide REST Server
~~~~~~~~~~~~~~~~~~~~

The PIRAmIDE REST Server is a servlet application that contains the repository
of applications. Developers upload the applications to the repository, and users
will get the adapted binaries. In order to deploy the PIRAmIDE REST Server, the
PiramideRestServer?.war must be deployed, and the build.xml script in the deploy
directory must be called.

Capacity Tester
~~~~~~~~~~~~~~~

The Capacity Tester is an Android application which is a proof of concept of how
the Capacity Tester should work. In order to provide complex variables such as
piramide.user.font.size.min, the application will show a text in different text
sizes and the user will confirm if it can be read. In the same way, different
combinations of colours can cause problems, so they are tested. In the future,
other types of tests, for instance testing if the user can move the mobile
device as expected, should be implemented.

Awards
======

*Title:* Assisted City.
*Award:* Best application in the Navigate, within the Via Inteligente awards for
services oriented to citizens.
*Entity:* Via Inteligente
*Date:* March 7th, 2012
*Authors:* Aitor Almeida, Pablo Orduña, Eduardo Castillejo, Diego López-de-Ipiña
*Link:* http://www.viainteligente.com/premios2012.html

Publications
============


* Aitor Almeida, Pablo Orduña, Eduardo Castillejo, Diego López-de-Ipiña, Marcos
  Sacristan. A method for automatic generation of fuzzy membership functions for
  mobile device's characteristics based on Google Trends. Computers in Human
  Behaviour (Journal). Impact Factor (2011): 2.293 DOI:
  10.1016/j.chb.2012.06.005. 2012. 2012.
* Aitor Almeida, Pablo Orduña, Eduardo Castillejo, Diego López-de-Ipiña, Marcos
  Sacristán. An approach to automatic generation of fuzzy membership functions
  using popularity metrics. 4th World Summit on the Knowledge Society. Mykonos,
  Greece, September, 2011.
* Aitor Almeida, Pablo Orduña, Eduardo Castillejo, Diego López-de-Ipiña, Marcos
  Sacristán. Adaptative applications for heterogeneous intelligent environments.
  ICOST 2011: 9th International Conference on Smart Homes and Health Telematics.
  Montréal, Canada, June 2011. LNCS6719, Toward Useful Services for Elderly and
  People with Disabilities, Springer, ISBN: 978-3-642-21534-6, pp. 1-8
* Aitor Almeida, Pablo Orduña, Eduardo Castillejo, Diego Lopez-de-Ipiña, Marcos
  Sacristan. Imhotep: an approach to user and device conscious mobile
  applications Personal and Ubiquitous Computing (Journal). Springer. Impact
  Factor (2009): 1.554. ISSN: 1617-4909. DOI: 10.1007/s00779-010-0359-8. January
  2011.
* Aitor Almeida, Pablo Orduña, Eduardo Castillejo, Diego Lopez-de-Ipiña, Marcos
  Sacristan. A user-centric approach to adaptable mobile interfaces. Actas del
  II International Workshop of Ambient Assisted Living (IWAAL 2010), p.p.
  153-160 Valencia, Spain, September 7-10, 2010 (ISBN: 978-84-92812-67-7)

Acknowledgments
===============

This work has been supported by project grant TSI-020301-2008-2 (`PIRAmIDE
<http://www.piramidepse.com/>`_), funded by the Spanish `Ministerio de
Industria, Turismo y Comercio <http://www.mityc.es/>`_).

Authors would also like to acknowledge the work of the Open Source software used
on top of which this project was developed:

* jFuzzyLogic
* ZXing
* WURFL
* Jython
* Google Web Toolkit
* RESTlet
* Android
* Python
* MySQL
* Java

As well as other projects and services, such as

* Google Trends

