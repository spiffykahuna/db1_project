#!/bin/bash

java -jar lib/h2-1.3.160.jar -url 'jdbc:h2:~/.devclub-java/db;AUTO_SERVER=TRUE;USER=devclub;PASSWORD=devclub' -user devclub -password devclub
