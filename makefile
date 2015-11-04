
build:
	-mkdir  classes
	javac -d classes src/*.java
	cd classes  && jar -cvmf ../manifest.txt ../dilbert.jar  *.class



clean:
	-rm -rf classes
	-rm dilbert.jar
