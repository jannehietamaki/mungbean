#!/bin/bash
cd mungbean-java
../bin/sbt update
cd ../mungbean-clojure
../bin/lein self-install
../bin/lein deps
cd .. 
