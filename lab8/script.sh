#!/bin/sh
echo
for file in `find . -type f | grep -v .idea | grep -v build | grep -v bin | grep -v 'gradle' | grep -v git | grep -v DS | grep -v .log | grep -v .class | grep -v .jar`
do
 echo "// ${file}"
 cat $file
done
