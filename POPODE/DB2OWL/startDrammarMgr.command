DIR=`dirname "$0"`
cd "$DIR"
echo pwd is `pwd`

echo Using java version `java -version`

java -Xmx512m -classpath "./bin:./lib/mysql-connector-java-5.1.5.jar" drammarManager > print_out.txt
