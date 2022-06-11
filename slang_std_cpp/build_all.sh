#! /bin/bash

rm -Rf build
mkdir build

for i in *.cpp
do
    printf "Building %s... " ${i%.*}
    ./build.sh $i
    printf "Done\n"
done