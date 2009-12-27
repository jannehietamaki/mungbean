import sbt._

class MongoDriver(info: ProjectInfo) extends DefaultProject(info) {
      val objenesis = "org.objenesis" % "objenesis" % "1.2"
      val clojure = "org.clojure" % "clojure" % "1.0.0"

      val junit = "junit" % "junit" % "4.7" %  "test->default"       
      val jdave = "org.jdave" % "jdave-junit4" % "1.1" % "test->default"


      val lpRepo = "Laughing Panda repository" at "http://www.laughingpanda.org/maven2"
}

