import json

class RegistrationSystem(object):
    
    
    def __init__(self):
        self.data = self.read_input()
        self.total_students = []
        
        
    def read_input(self):
        file = open("inputs/input.json", "r")
        return json.load(file)
    
    
