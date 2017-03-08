import numpy as np
import re
from pprint import pprint

class Kernel:

    def __init__(self, dictionary, training_data):
        self.string_dict = dictionary
        self.train_data = training_data

    def occurrences(self, s1, substring):
        return len(re.findall('(?={0})'.format(re.escape(substring)), s1))

    def perceptron(self):
        p = 2
        i = 0
        w_t = []
        w_init = ''
        y_t = 0
        val = 0

        while i < len(self.train_data[:, 0]):
            x_t = self.train_data[i, 0]
            y_t = int(self.train_data[i, 1])
            val = self.dot_product(w_t, x_t, y_t, p)

            if val <= 0:
                w_t.append(i)
            i += 1
        print w_t[:]
        print('\n')
        print(len(w_t))

    def dot_product(self, w_t, x_t, y_t, p):
        total_sum = 0

        for i in w_t:
            total_sum += self.string_kernel(self.train_data[i, 0], x_t, int(self.train_data[i, 1]), y_t, p)
        total_sum = total_sum * y_t
        return total_sum

    def string_kernel(self, string_s, string_t, y_s, y_t, p):
        i = 0
        self.string_dict = dict.fromkeys(self.string_dict, 0)

        while i < len(string_s) - p + 1:
            # let v = s1[i]
            v = string_s[i: i+p]
            # check for double counting first
            if self.string_dict[v] == 0:
                # checking if v is a substring t
                a = self.occurrences(string_s, v)
                b = self.occurrences(string_t, v)
                if a > 0 and b > 0:
                    self.string_dict[v] = a * b
            i += 1
        # just a print statement to help debug
        val = sum(self.string_dict.itervalues()) * y_s

        return val

if __name__ == "__main__":

    # input file and create array
    train_file = open('hw5train.txt', 'r')
    training_data = np.array([line.rstrip().split(" ") for line in train_file])
    train_file.close

    s = open('hw5train.txt', 'r').read()
    # create the dictionary
    string_set = set(s)
    string_set.remove('\n')
    string_set.remove(' ')
    string_set.remove('-')
    string_set.remove('+')
    string_set.remove('1')
    dictionary = dict.fromkeys(string_set, 0)

    #k = Kernel(dictionary, training_data)
    #k.perceptron()
    #str = 'hello world'
    #print(str[2:4])

