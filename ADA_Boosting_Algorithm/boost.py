import numpy as np
import math

class boost:

    def __init__(self, train_data, string_dict, test_data):
        self.train_data = train_data
        self.string_dict = string_dict
        self.test_data = test_data
        self.weights = np.zeros(shape=(len(self.train_data[:, 0]), 1))
        self.class_list = []

        i = 0
        init_weight = 1.0 / len(self.train_data[:, 0])
        while i < len(self.weights[:, 0]):
            self.weights[i, 0] = init_weight
            i += 1

    def boost_algorithm(self):
        t = 0
        ''' below are the chosen weak learner variables '''
        h_t = 0
        e_t = 0.0

        while t < 20:
            ''' need to reset all the values for each round of boosting '''
            print('Round ' + str(t+1))
            i = 0
            j = 0
            pos_e_t = 1.0
            neg_e_t = 1.0
            pos_h_t = 100
            neg_h_t = 100
            curr_pos_e_t = 0.0
            curr_neg_e_t = 0.0
            is_positive = False
            h_t = 0
            e_t = 0.0
            ''' iterating through all the words in the dictionary file '''
            while j < len(self.string_dict[:, 0]):
                ''' determine best h_t(+) and h_t(-) '''
                while i < len(self.train_data[:, 0]):

                    ''' determining values for positive classifier '''
                    if int(self.train_data[i, j]) == 1:
                        pos_predict = 1
                    else:
                        pos_predict = -1
                    if pos_predict != int(self.train_data[i, len(self.train_data[0, :]) - 1]):
                        curr_pos_e_t += self.weights[i, 0]

                    ''' determining values for negative classifier '''
                    if int(self.train_data[i, j]) == 0:
                        neg_predict = 1
                    else:
                        neg_predict = -1
                    if neg_predict != int(self.train_data[i, len(self.train_data[0, :]) - 1]):
                        curr_neg_e_t += self.weights[i, 0]

                    i += 1
                ''' determine best positive h_t '''
                if curr_pos_e_t < pos_e_t:
                    pos_h_t = j
                    pos_e_t = curr_pos_e_t
                    is_positive = True
                ''' determine best negative h_t '''
                if curr_neg_e_t < neg_e_t:
                    neg_h_t = j
                    neg_e_t = curr_neg_e_t
                    is_positive = False

                curr_neg_e_t = 0.0
                curr_pos_e_t = 0.0
                i = 0
                j += 1

            ''' choose between h_t(+) or h_t(-)'''
            if pos_e_t < neg_e_t:
                e_t = pos_e_t
                h_t = pos_h_t
                is_positive = True
            else:
                e_t = neg_e_t
                h_t = neg_h_t
                is_positive = False

            self.determine_new_weights(h_t, e_t, is_positive)
            t += 1

    def determine_new_weights(self, h_t, e_t, is_positive):
        alpha_t = 0.5 * np.log((1.0 - e_t) / e_t)
        z_t = 0.0
        predict = 0

        i = 0
        while i < len(self.weights[:, 0]):
            if is_positive:
                if int(self.train_data[i, h_t]) == 1:
                    predict = 1
                else:
                    predict = -1
            else:
                if int(self.train_data[i, h_t]) == 0:
                    predict = 1
                else:
                    predict = -1

            val = self.weights[i, 0] * math.exp(-(alpha_t * predict * int(self.train_data[i, len(self.train_data[0, :]) - 1])))
            self.weights[i, 0] = val
            z_t += self.weights[i, 0]
            i += 1

        i = 0
        while i < len(self.weights[:, 0]):
            val = self.weights[i, 0] / z_t
            self.weights[i, 0] = val
            i += 1

        self.add_classifier(alpha_t, h_t, is_positive)

        print ('e_t: ' + str(e_t))
        print ('alpha_t: ' + str(alpha_t))
        print ('h_t: ' + str(h_t))
        print ('z_t: ' + str(z_t))
        self.determine_test_classifier()
        print ('-------------------------------------')


    def add_classifier(self, alpha_t, h_t, is_positive):
        if is_positive:
            row = [alpha_t, h_t, 1]
        else:
            row = [alpha_t, h_t, 0]
        self.class_list.append(row)

    def determine_classifier(self):
        # H(x) is the sign(summation alpha_t * h_t(x) from 1, ..., T)
        i = 0
        total_sum = 0.0
        val = 0
        predict = 0
        total = 0
        mistakes = 0
        error = 0.0

        while i < len(self.train_data[:, 0]):
            for row in self.class_list:

                if int(row[2]) == 1:
                    if int(self.train_data[i, int(row[1])]) == 1:
                        val = 1
                    else:
                        val = -1
                else:
                    if int(self.train_data[i, int(row[1])]) == 0:
                        val = 1
                    else:
                        val = -1
                total_sum += (row[0] * val)

            if total_sum > 0.0:
                predict = 1
            else:
                predict = -1
            if predict != int(self.train_data[i, len(self.train_data[0, :]) - 1]):
                mistakes += 1
            total_sum = 0.0
            total += 1
            i += 1
        error = (1.0 * mistakes) / (1.0 * total)
        print ('Total Error: ' + str(error))

    def determine_test_classifier(self):
        # H(x) is the sign(summation alpha_t * h_t(x) from 1, ..., T)
        i = 0
        total_sum = 0.0
        val = 0
        predict = 0
        total = 0
        mistakes = 0
        error = 0.0

        while i < len(self.test_data[:, 0]):
            for row in self.class_list:

                if int(row[2]) == 1:
                    if int(self.test_data[i, int(row[1])]) == 1:
                        val = 1
                    else:
                        val = -1
                else:
                    if int(self.test_data[i, int(row[1])]) == 0:
                        val = 1
                    else:
                        val = -1
                total_sum += (row[0] * val)

            if total_sum > 0.0:
                predict = 1
            else:
                predict = -1
            if predict != int(self.test_data[i, len(self.test_data[0, :]) - 1]):
                mistakes += 1
            total_sum = 0.0
            total += 1
            i += 1
        error = (1.0 * mistakes) / (1.0 * total)
        print ('Total Test Error: ' + str(error))

if __name__ == "__main__":
    train_file = open('hw6train.txt', 'r')
    train_data = np.array([line.rstrip().split(" ") for line in train_file])
    train_file.close

    test_file = open('hw6test.txt', 'r')
    test_data = np.array([line.rstrip().split(" ") for line in test_file])
    test_file.close

    dict_file = open('hw6dictionary.txt', 'r')
    string_dict = np.array([line.rstrip().split(" ") for line in dict_file])
    dict_file.close

    b = boost(train_data, string_dict, test_data)
    b.boost_algorithm()
