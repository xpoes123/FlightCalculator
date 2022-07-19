run:compile  
        @java FrontEnd 
clean: 
        @rm -f *.class 
        @echo "Class files removed" 
test:compile 
        @java -jar junit5.jar -cp . --scan-classpath --include-classname=test
 
compile: clean 
        @javac -cp .:junit5.jar *.java 
        @echo "Finished"
