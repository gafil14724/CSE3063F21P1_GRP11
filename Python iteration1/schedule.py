from course_section import CourseSection


class Schedule(object):
    
    DAYS = 5
    HOURS = 8
    
    def __init__(self, student):
        self.student = student
        self.program = []
        
    
    def add_to_program(self, course_section: CourseSection):
        course_program = course_section.course_program
        
        for i in range(self.HOURS):
            for j in range(self.DAYS):
                if course_program[i][j] is True:
                    self.program[j][j] = course_section
                
    
    def collided_sections(self, course_section: CourseSection):
        course_program = course_section.course_program
        collided_sections = []
        
        for i in range(self.HOURS):
            for j in range(self.DAYS):
                if self.program[i][j] is True and course_program[i][j] is True:
                    collided_sections.append(self.program[i][j])
            
        return collided_sections
    
    def is_collision(self, course_section: CourseSection):
        return len(self.collided_sections(course_section)) > 1 