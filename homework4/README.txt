// Michael Covarrubias
// PID#: A12409694
// Naveen Ketagoda
// PID#: A10773459

(a) The perceptron classifier has the highest accuracy for examples that belong to class i.
- Looking at the confusion matrix we can denote that the classifier C_1 is the most
accurate classifier. Classifier 1. With only 12 miss classifications.

(b) The perceptron classifier has the least accuracy for examples that belong to class i.
- Looking at the confusion matrix we denote that the classifier with the least accuracy is
C_5, classifier 5. Which has 94 miss classifications.

(c) The perceptron classifier most often mistakenly classifies an example in class j as belonging to
class i, for i, j ∈ {1, 2, 3, 4, 5, 6} (i.e., excluding Don’t Know).
- The perceptron classifier most often mistakenly classifies data is in the location of the
confusion matrix where i = 6, and j = 5. C(6, 5).


For the situation when <x,w>=0, you can predict whatever we want,
we chose to predict -1, thus calculating the test error accordingly


1. Compile program
  javac perceptron.java
2. Run program
  java perceptron hw4train.txt hw4test.txt hw4dictionary.txt

3. output
TRAINING ERROR
                         Pass # 1
              Perceptron Error: 0.038532
                   Voted Error: 0.066972
                 Average Error: 0.077064
                ----------------
                  TRAINING ERROR
                         Pass # 2
              Perceptron Error: 0.035780
                   Voted Error: 0.028440
                 Average Error: 0.030275
                ----------------
                  TRAINING ERROR
                         Pass # 3
              Perceptron Error: 0.018349
                   Voted Error: 0.021101
                 Average Error: 0.016514
                ----------------
                  TRAINING ERROR
                         Pass # 4
              Perceptron Error: 0.016514
                   Voted Error: 0.013761
                 Average Error: 0.011927
                ----------------
Max Word / Coordinate (In Descnding Order)
                           file 438
                        program 466
                           line 203
Min Word / Coordinate (In Descnding Order)
                           game 393
                           team 469
                             he 78

                      TEST ERROR
                         Pass # 1
              Perceptron Error: 0.026525
                   Voted Error: 0.034483
                 Average Error: 0.039788
                ----------------
                      TEST ERROR
                         Pass # 2
              Perceptron Error: 0.023873
                   Voted Error: 0.021220
                 Average Error: 0.021220
                ----------------
                      TEST ERROR
                         Pass # 3
              Perceptron Error: 0.015915
                   Voted Error: 0.013263
                 Average Error: 0.013263
                ----------------
                      TEST ERROR
                         Pass # 4
              Perceptron Error: 0.007958
                   Voted Error: 0.013263
                 Average Error: 0.010610
                ----------------
                   Predict Count
                     Predict 1: 504   2   6   2   1   1
                     Predict 2:   8 475  20   7   7   8
                     Predict 3:   0   2 268   3   4   5
                     Predict 4:   9  10   8 445   5   3
                     Predict 5:   9  15  21   8 471  41
                     Predict 6:   6  13  17   4  24 228
            Predict DON'T KNOW:  47  76  42  95  27  53

              Predict Percentage
                     Predict 1: .9282 .0037 .0115 .0039 .0019 .0028
                     Predict 2: .0147 .8684 .0382 .0135 .0135 .0227
                     Predict 3: .0000 .0037 .5124 .0058 .0077 .0142
                     Predict 4: .0166 .0183 .0153 .8607 .0097 .0085
                     Predict 5: .0166 .0274 .0402 .0155 .9093 .1165
                     Predict 6: .0110 .0238 .0325 .0077 .0463 .6477
            Predict DON'T KNOW: .0866 .1389 .0803 .1838 .0521 .1506
