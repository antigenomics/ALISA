#
rm -r docs/
mvn javadoc:javadoc
mv target/site/apidocs/ docs/javadoc/
