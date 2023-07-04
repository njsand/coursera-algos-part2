SOURCES=WordNet.java SAP.java

ZIP_SOURCES=WordNet.java SAP.java

ZIPFILE=wordnet.zip

compile:
	javac -cp ../algs4.jar -Xlint:unchecked ${SOURCES}

zip:
	zip ${ZIPFILE} ${ZIP_SOURCES}

clean:
	rm -f *.class ${ZIPFILE} TAGS
