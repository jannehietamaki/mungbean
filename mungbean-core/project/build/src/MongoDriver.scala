import sbt._

class MongoDriver(info: ProjectInfo) extends DefaultProject(info) {
      val junit = "junit" % "junit" % "4.7" %  "test->default"       
      val jdave = "org.jdave" % "jdave-junit4" % "1.1" % "test->default"

      val lpRepo = "Laughing Panda repository" at "http://www.laughingpanda.org/maven2"
}

