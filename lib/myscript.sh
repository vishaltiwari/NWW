#!/bin/sh
# my first script, 
# copyright, year, Author
 
# plot current region settings
g.region -p
echo "test string print" 

#get all the DEPTH maps
dir=`pwd`
echo $dir

cd /home/vishal/grassdata/spearfish/PERMANENT/colr
FILES=DEPTH.*
#cd $dir
#echo `pwd`
#echo $FILES . "\n"
i=0
for f in $FILES
do
#    echo $f
    INPUT=$f"@PERMANENT"
    OUTPUT=/home/vishal/Desktop/Grass_Output/images8/$i".tif"
    i=$((i+1))
    echo $INPUT
    echo $OUTPUT
    r.out.gdal input=$INPUT output=$OUTPUT
    echo "done exporting"
done
# leave with exit status 0 which means "ok":
exit 0
