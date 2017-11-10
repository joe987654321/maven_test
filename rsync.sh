#!/bin/sh
date
#exit

echo "PWD: "`pwd`
PROJECT_NAME=test_with_maven
echo "PROJECT_NAME: $PROJECT_NAME"
echo "======="

#BUILD_NUMBER=9999

#~/apache-maven-3.2.3/bin/mvn clean compile
#~/apache-maven-3.2.3/bin/mvn -Dtest -DfailIfNoTests=false -Dbuild=$BUILD_NUMBER package

#exit

hs=
#hs="$hs ldev101.ac.corp.tw1.yahoo.com"
#hs="$hs api1.cards.ec.tcv.yahoo.com"
#hs="$hs api1.cards.ec.twv.yahoo.com"
#hs="$hs api2.cards.ec.twv.yahoo.com"
#hs="$hs build6.ec.corp.tw1.yahoo.com"
#hs="$hs b-api1.cards.ec.tw1.yahoo.com"
hs="$hs oxy-oxygen-0a52c74b.corp.sg3.yahoo.com"

for h in $hs; do
    echo "====$h===="
    DEST="$h:~/$PROJECT_NAME/"
    
    time rsync -PavxH --exclude 'target' --delete ./ "${DEST}"
#    time rsync -PavxH  --delete ./ "${DEST}"
done

exit
