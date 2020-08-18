@echo off
rem # PROJECT   : Elbfisch.org
rem # MODULE    : launch.bat
rem # VERSION   : 1.0
rem # DATE      : 10/5/2016
rem # PURPOSE   : 
rem # AUTHOR    : Bernd Schuster, MSK Gesellschaft fuer Automatisierung mbH, Schenefeld
rem # REMARKS   : -
rem # CHANGES   : CH#n <Kuerzel> <datum> <Beschreibung>
rem # LOG       : $Log$
@echo on
java -cp ".\\lib\\*;.\\cfg\\" elbfisch.samples.opcua.client.ClientTester