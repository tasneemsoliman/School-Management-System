case class Course(courseId: String, courseName: String, teacherId: String, studentsEnrolled: List[String], schedule: String)

object Course {
  private val coursesFile = "C:\\Users\\user\\IdeaProjects\\School Management System\\files\\courses.txt"

  def fromString(courseString: String): Option[Course] = {
    val fields = courseString.split("\\|")
    if (fields.length == 5) {
      val Array(courseId, courseName, teacherId, studentsEnrolledStr, schedule) = fields
      val studentsEnrolled = studentsEnrolledStr.split(",").toList
      Some(Course(courseId, courseName, teacherId, studentsEnrolled, schedule))
    } else {
      None
    }
  }

  def addCourse(course: Course): Boolean = {
    val coursesFile = "C:\\Users\\user\\IdeaProjects\\School Management System\\files\\courses.txt"
    val courseString = s"${course.courseId}|${course.courseName}|${course.teacherId}|${course.studentsEnrolled.mkString(",")}|${course.schedule}\n"

    val existingCourses = scala.io.Source.fromFile(coursesFile).getLines().toList
    val courseIds = existingCourses.map(_.split("\\|")(0))

    if (courseIds.contains(course.courseId)) {
      println(s"Course with ID ${course.courseId} already exists.")
      false
    } else {
      try {
        val writer = new java.io.BufferedWriter(new java.io.FileWriter(coursesFile, true))
        writer.write(courseString)
        writer.close()
        true
      } catch {
        case e: Exception =>
          println(s"Failed to add course: ${e.getMessage}")
          false
      }
    }
  }


  def getCoursesForStudent(studentId: String): List[Course] = {
    val source = scala.io.Source.fromFile(coursesFile)
    try {
      val courses = source.getLines().flatMap { line =>
        fromString(line) match {
          case Some(course) if course.studentsEnrolled.contains(studentId) => Some(course)
          case _ => None
        }
      }.toList
      courses
    } finally {
      source.close()
    }
  }
}
