import sbt._

class MongoDriver(info: ProjectInfo) extends DefaultProject(info) {
      override def testFrameworks = super.testFrameworks ++ List(new TestFramework("com.novocode.junit.JUnitFrameworkNoMarker"))
      override def includeTest(test: String) = test.matches(".*Spec|.*IntegrationTest")

      val objenesis = "org.objenesis" % "objenesis" % "1.2"
      val clojure = "org.clojure" % "clojure" % "1.1.0"


      val junitInterface = "com.novocode" % "junit-interface" % "0.4" % "test->default"    
      val jdave = "com.github.mpeltonen" % "jdave-junit4" % "1.2-beta1" % "test->default"

      val lpRepo = "JDave snapshots" at "http://www.laughingpanda.org/maven2/"
      val clojureRepo = "Clojure snapshots" at "http://build.clojure.org/snapshots/"
}

