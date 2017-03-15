# Michael Covarrubias
# PID#: A12409694
# Naveen Ketagoda
# PID#: A10773459

import numpy as np
import heapq
master_dictionary = dict()

class Kernel:

    def __init__(self, training_data):
        self.train_data = training_data
        self.w_t = []
        self.all_dicts = dict()
        self.dict_t = dict()
        self.max_set = set()

    def perceptron_algo(self, test_data):
        p = 5
        i = 0
        val = 0
        dict_s = master_dictionary
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

    def determine_w(self, test_data):
        p = 5
        i = 0
        y_t = 0
        val = 0
        dict_s = self.master_dict(p, test_data)
        dict_t = dict_s

        while i < len(self.train_data[:, 0]):
            x_t = self.train_data[i, 0]
            y_t = int(self.train_data[i, 1])
            val = self.dot_product(dict_s, dict_t, self.w_t, x_t, y_t, p)
            val = val * y_t

            if val <= 0:
                self.w_t.append(i)
                self.all_dicts[i] = self.dict_t
            i += 1

    def dot_product(self, dict_s, dict_t, w_t, x_t, y_t, p):
        total_sum = 0
        i = 0
        dict_t = dict.fromkeys(dict_t, 0)

        while i < len(x_t) - p + 1:
            v = x_t[i: i+p]
            dict_t[v] += 1
            i += 1

        for i in w_t:
            dict_s = self.all_dicts[i]
            total_sum += self.string_kernel(dict_s, dict_t, self.train_data[i, 0], x_t, int(self.train_data[i, 1]), y_t, p)

        self.dict_t = dict_t
        return total_sum

    def string_kernel(self, dict_s, dict_t, string_s, string_t, y_s, y_t, p):
        val = 0
        i = 0
        v_set = set()

        while i < len(string_s) - p + 1:
            v = string_s[i: i+p]
            v_set.add(v)

            i += 1

        for v in v_set:
            a = dict_s[v]
            b = dict_t[v]
            val += (a * b)
            self.max_set.add(a)
            #temp = a * b
            #if temp > 0:
            #    val += 1

        val *= y_s

        return val

    def master_dict(self, p, test_data):
        # need to make a dictionary with all possible strings of size p
        global master_dictionary
        string_set = set()
        train_strings = self.train_data[:, 0]
        test_strings = test_data[:, 0]

        for curr_string in train_strings:
            i = 0
            while i < len(curr_string) - p + 1:
                v = curr_string[i: i+p]
                string_set.add(v)
                i += 1
        for curr_string in test_strings:
            i = 0
            while i < len(curr_string) - p + 1:
                v = curr_string[i: i+p]
                string_set.add(v)
                i += 1

        master_dictionary = dict.fromkeys(string_set, 0)
        return master_dictionary

    def determine_max_values(self):
        max_vals = heapq.nlargest(2, self.max_set)
        max1_strings = list()
        max2_strings = list()

        for i in self.w_t:
            curr_dict = self.all_dicts[i]
            for v in curr_dict.keys():
                if curr_dict[v] == max_vals[0]:
                    max1_strings.append(v)
                if curr_dict[v] == max_vals[1]:
                    max2_strings.append(v)

        print '1st Maximum Values'
        print max1_strings
        print '2ns Maximum Values'
        print max2_strings


if __name__ == "__main__":

    # input file and create array
    train_file = open('hw5train.txt', 'r')
    training_data = np.array([line.rstrip().split(" ") for line in train_file])
    train_file.close

    test_file = open('hw5test.txt', 'r')
    test_data = np.array([line.rstrip().split(" ") for line in test_file])
    test_file.close

    k = Kernel(training_data)
    k.determine_w(test_data)
    k.perceptron_algo(test_data)
    k.determine_max_values()
