#http://www.vineetmanohar.com/2009/11/3-ways-to-run-java-main-from-maven/
.PHONY: exec1 exec2 exec3 exec4 test test_only compile

compile:
	mvn compile

test:
	mvn compile test

test_only:
	mvn test

exec1:
	mvn exec:java -Dexec.mainClass="filewalker.App"

exec2:
	mvn exec:java -Dexec.mainClass="filewalker.App" -Dexec.args="~/"

exec3:
	mvn exec:java -Dexec.mainClass="filewalker.App" -Dexec.args="/happs/"

exec4:
	mvn exec:java -Dexec.mainClass="filewalker.App" -Dexec.classpathScope=runtime
