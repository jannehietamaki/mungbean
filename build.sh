#!/bin/bash
set -e
cd mungbean-java
../bin/sbt test package
cd ../mungbean-clojure
rm -f lib/mungbean*.jar
cp ../mungbean-java/target/scala_2.8.0/*.jar lib/
../bin/lein test
../bin/lein jar
cd ..

