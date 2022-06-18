#! /bin/bash

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

rm -Rf build
mkdir build

for dir in ./*/
do
    if [ "$dir" = "./build/" ]; then
        continue
    fi;
    rm -Rf build/${dir}
    mkdir build/${dir}
    printf "Building ${YELLOW}%s${NC}\b modules...\n" ${dir#*/}
    for i in ${dir%/*}/*.cpp
    do
        printf "\tBuilding %s...\t" ${i%.*}
        ./build.sh $i
        printf "${GREEN}Done${NC}\n"
    done
done
