# Michael Covarrubias
# PID#: A12409694
# Naveen Ketagoda
# PID#: A10773459

import numpy as np

class boost:

    def __init__(self, train_data, string_dict):
        self.train_data = train_data
        self.string_dict = string_dict
        self.sample_weights = np.zeros(shape=(len(self.train_data[:,0]), 1))

    def boost_algo(self):
        pos_h_t = 100
        neg_h_t = 100
        min_pos_error = 1.0
        min_neg_error = 1.0
        i = 0
        j = 0
        init_weight = 1.0 / len(self.train_data[:, 0])
        pos_e_t = 0.0
        neg_e_t = 0.0
        pos_alpha_t = 0.0
        neg_alpha_t = 0.0

        # iterating through all the words in the dictionary file
        while j < len(self.string_dict[:, 0]):
            # determine h_t
            while i < len(self.train_data[:, 0]):

                # determining values for positive classifier
                if (int(self.train_data[i, j]) == 1):
                    pos_predict = 1
                else:
                    pos_predict = -1
                # determine positive e_t
                if (pos_predict != int(self.train_data[i, len(self.train_data[0, :]) - 1])):
                    pos_e_t += init_weight

                # determining  values for negative classifier
                if (int(self.train_data[i, j]) == 0):
                    neg_predict = 1
                else:
                    neg_predict = -1
                # determining negative e_t
                if (neg_predict != int(self.train_data[i, len(self.train_data[0,:]) - 1])):
                    neg_e_t += init_weight

                i += 1

            #determine positive alpha
            if pos_e_t < min_pos_error:
                pos_h_t = j
                min_pos_error = pos_e_t
                pos_alpha_t = 0.5 * np.log((1.0 - pos_e_t) / pos_e_t)
            # determine negative alpha
            if neg_e_t < min_neg_error:
                neg_h_t = j
                min_neg_error = neg_e_t
                neg_alpha_t = 0.5 * np.log((1.0 - neg_e_t) / neg_e_t)

            # calculate D_t+1() for all sample points
            neg_e_t = 0.0
            pos_e_t = 0.0
            i = 0
            j += 1
        print ('Positive h_t: ' + str(pos_h_t))
        print ('Positive error: ' + str(min_pos_error))
        print ('Negative h_t: ' + str(neg_h_t))
        print ('Negative error: ' + str(min_neg_error))

    def final_classifier(self):
        # H(x) is the sign(summation alpha_t * h_t(x) from 1, ..., T)
        print('just so it can compile')

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

    b = boost(train_data, string_dict)
    b.boost_algo()