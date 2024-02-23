import java.io.{BufferedWriter, FileWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class Exam(courseId: String, examDate: String, examTime: String, studentsEnrolled: List[String])

object Exam {
  private val examsFile = "C:\\Users\\user\\IdeaProjects\\School Management System\\files\\exams.txt"
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  def addExamToStudent(studentId: String, exam: Exam): Boolean = {
    val examString = s"${exam.courseId}|${exam.examDate}|${exam.examTime}|$studentId\n"
    try {
      val writer = new BufferedWriter(new FileWriter(examsFile, true))
      writer.write(examString)
      writer.close()
      true
    } catch {
      case e: Exception =>
        println(s"Failed to add exam to student: ${e.getMessage}")
        false
    }
  }

}
