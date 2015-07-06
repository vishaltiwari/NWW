#!/bin/bash

FILES=/home/vishal/Desktop/Grass_Output/images8/*

for f in $FILES
do
    echo "Processing file $f"
    python gdalcopyproj.py $1 $f
done
