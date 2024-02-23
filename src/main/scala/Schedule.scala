import java.io.{BufferedWriter, FileWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class Schedule(studentId: String, userName: String, courseName: String, dateTime: LocalDateTime)

object Schedule {
  private val scheduleFile = "C:\\Users\\user\\IdeaProjects\\School Management System\\files\\schedule.txt"
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  def saveSchedule(schedule: Schedule): Boolean = {
    val scheduleString = s"${schedule.studentId}|${schedule.userName}|${schedule.courseName}|${schedule.dateTime.format(formatter)}\n"
    try {
      val writer = new BufferedWriter(new FileWriter(scheduleFile, true))
      writer.write(scheduleString)
      writer.close()
      true
    } catch {
      case e: Exception =>
        println(s"Failed to save schedule: ${e.getMessage}")
        false
    }
  }

  def sortScheduleByDateTime(): List[Schedule] = {
    try {
      val schedules = scala.io.Source.fromFile(scheduleFile)
        .getLines()
        .flatMap { line =>
          val Array(studentId, userName, courseName, dateTimeString) = line.split("\\|")
          val dateTime = LocalDateTime.parse(dateTimeString, formatter)
          Some(Schedule(studentId, userName, courseName, dateTime))
        }
        .toList

      val sortedSchedules = schedules.sortBy(_.dateTime)
      writeSchedulesToFile(sortedSchedules)
      sortedSchedules
    } catch {
      case e: Exception =>
        println(s"Failed to read schedule: ${e.getMessage}")
        List.empty[Schedule]
    }
  }

  private def writeSchedulesToFile(schedules: List[Schedule]): Unit = {
    try {
      val writer = new BufferedWriter(new FileWriter(scheduleFile, false))
      schedules.foreach { schedule =>
        val scheduleString = s"${schedule.studentId}|${schedule.userName}|${schedule.courseName}|${schedule.dateTime.format(formatter)}\n"
        writer.write(scheduleString)
      }
      writer.close()
    } catch {
      case e: Exception =>
        println(s"Failed to write sorted schedule to file: ${e.getMessage}")
    }
  }
}
