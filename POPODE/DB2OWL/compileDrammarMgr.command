DIR=`dirname "$0"`
cd "$DIR"
echo pwd is `pwd`

echo Using java version `java -version`

javac -sourcepath src -d bin -classpath "./lib/mysql-connector-java-5.1.5.jar" src/*.java -Xlint
