import sbt._

class MongoDriver(info: ProjectInfo) extends DefaultProject(info) {
      override def compileOrder = CompileOrder.JavaThenScala
      override def testFrameworks = super.testFrameworks ++ List(new TestFramework("com.novocode.junit.JUnitFrameworkNoMarker"))
      override def includeTest(test: String) = test.matches(".*Spec|.*IntegrationTest")


      val objenesis = "org.objenesis" % "objenesis" % "1.2"
      val clojure = "org.clojure" % "clojure" % "1.0.0"

      val junit = "junit" % "junit" % "4.7" %  "test->default"       
      val jdave = "org.jdave" % "jdave-junit4" % "1.1" % "test->default"
      val junitInterface = "com.novocode" % "junit-interface" % "0.3"

      val lpRepo = "Laughing Panda repository" at "http://www.laughingpanda.org/maven2"
}

