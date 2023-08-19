SOURCES=SeamCarver.java

ZIP_SOURCES=SeamCarver

ZIPFILE=seam.zip

compile:
	javac -cp ../algs4.jar -Xlint:unchecked ${SOURCES}

zip:
	zip ${ZIPFILE} ${ZIP_SOURCES}

clean:
	rm -f *.class ${ZIPFILE} TAGS
