class Advisor(object):
    
    def __init(self, name):
        self.name = name
    
    
    def approve_course_section(self, student, course_section):
        course = course_section.course
        course.when_requested(student)
        
        
    