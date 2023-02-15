DIR=`dirname "$0"`
cd "$DIR"
echo pwd is `pwd`

echo Using java version `java -version`

javac -sourcepath src -d classes -classpath "./lib/commons-io-2.4.jar:./lib/arq-2.8.7.jar:./lib/icu4j-3.4.4.jar:./lib/iri-0.8.jar:./lib/jena-2.6.4-tests.jar:./lib/jena-2.6.4.jar:./lib/junit-4.5.jar:./lib/log4j-1.2.13.jar:./lib/lucene-core-2.3.1.jar:./lib/slf4j-api-1.5.8.jar:./lib/slf4j-log4j12-1.5.8.jar:./lib/stax-api-1.0.1.jar:./lib/wstx-asl-3.2.9.jar:./lib/xercesImpl-2.7.1.jar" src/*.java -Xlint
