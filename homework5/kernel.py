# Michael Covarrubias
# PID#: A12409694
# Naveen Ketagoda
# PID#: A10773459

import numpy as np

class Kernel:

    def __init__(self, training_data):
        self.train_data = training_data
        self.w_t = []

    def perceptron_algo(self, test_data):
        p = 3
        i = 0
        val = 0
        dict_s = self.master_dict(p)
        dict_t = dict_s
        predict = 0
        error = 0
        count = 0

        while i < len(test_data[:, 0]):
            x_t = test_data[i, 0]
            y_t = int(test_data[i, 1])
            val = self.dot_product(dict_s, dict_t, self.w_t, x_t, y_t, p)

            if val <= 0:
                predict = -1
            else:
                predict = 1
            if predict != y_t:
                error += 1
            count += 1
            i += 1

        total_error = (1.0 * error) / (1.0 * count)
        print(total_error)

    def determine_w(self):
        p = 3
        i = 0
        y_t = 0
        val = 0
        dict_s = self.master_dict(p)
        dict_t = dict_s

        while i < len(self.train_data[:, 0]):
            x_t = self.train_data[i, 0]
            y_t = int(self.train_data[i, 1])
            val = self.dot_product(dict_s, dict_t, self.w_t, x_t, y_t, p)
            # added new code val = val * y_t
            val = val * y_t

            if val <= 0:
                self.w_t.append(i)
            i += 1

    def dot_product(self, dict_s, dict_t, w_t, x_t, y_t, p):
        total_sum = 0

        for i in w_t:
            dict_s = dict.fromkeys(dict_s, 0)
            dict_t = dict.fromkeys(dict_t, 0)
            total_sum += self.string_kernel(dict_s, dict_t, self.train_data[i, 0], x_t, int(self.train_data[i, 1]), y_t, p)
        #total_sum = total_sum * y_t

        return total_sum

    def string_kernel(self, dict_s, dict_t, string_s, string_t, y_s, y_t, p):
        val = 0
        i = 0
        v_set = set()

        while i < len(string_s) - p + 1:
            v = string_s[i: i+p]
            v_set.add(v)
            dict_s[v] += 1
            i += 1
        i = 0
        while i < len(string_t) - p + 1:
            v = string_t[i: i+p]
            dict_t[v] += 1
            i += 1
        i = 0

        for v in v_set:
            a = dict_s[v]
            b = dict_t[v]
            val += (a * b)

        '''
        for v in dict_s.iterkeys():
            a = dict_s[v]
            b = dict_t[v]
            val += (a * b)
        '''

        val *= y_s

        return val

    def master_dict(self, p):
        # need to make a dictionary with all possible strings of size p
        string_set = set()
        train_strings = self.train_data[:, 0]
        for curr_string in train_strings:
            i = 0
            while i < len(curr_string) - p + 1:
                v = curr_string[i: i+p]
                string_set.add(v)
                i += 1
        dictionary = dict.fromkeys(string_set, 0)
        print len(dictionary)
        return dictionary

if __name__ == "__main__":

    # input file and create array
    train_file = open('hw5train.txt', 'r')
    training_data = np.array([line.rstrip().split(" ") for line in train_file])
    train_file.close

    k = Kernel(training_data)
    k.determine_w()
    k.perceptron_algo(training_data)
