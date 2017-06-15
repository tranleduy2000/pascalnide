
for file in  `ls`
do
    convert $file -resize 154x105 $file
done 
