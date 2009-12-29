#!/bin/bash
set -e
cd mungbean-java
../bin/sbt test package
cd ../mungbean-clojure
rm -f lib/mungbean*.jar
cp ../mungbean-java/target/scala_2.7.7/*.jar lib/
../bin/lein test
../bin/lein jar
cd ..

