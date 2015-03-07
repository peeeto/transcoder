#!/bin/bash

DIR=`dirname $(readlink $0) &> /dev/null` && DIR=`dirname $(readlink $0)` || DIR=.
java -jar "$DIR"/target/transcoder-*-SNAPSHOT.one-jar.jar 

