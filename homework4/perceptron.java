// Michael Covarrubias
// PID#: A12409694
// Naveen Ketagoda
// PID#: A10773459

import java.io.*;
import java.util.*;
import java.lang.Math;
import java.text.DecimalFormat;

class perceptron {
  public static void main(String[] args){
    // declare file variables
    String train_string, test_string, dict_string;
    String error;
    double perceptron_error = 0;
    double voted_error = 0;
    double avg_error = 0;
    train_string = args[0];
    test_string = args[1];
    dict_string = args[2];


    File train_file = new File(train_string);
    File test_file = new File(test_string);
    File dict_file = new File(dict_string);
    LinkedList<int[]> train_list  = new LinkedList<int[]>();
    LinkedList<int[]> test_list = new LinkedList<int[]>();
    LinkedList<int[]> w_list = new LinkedList<int[]>();
    LinkedList<String> dict_list = new LinkedList<String>();
    train_list = input_file2(train_file);
    test_list = input_file2(test_file);
    dict_list = input_dictionary(dict_file);
    int [] w = new int[train_list.get(0).length];
    int [] w_init = new int[train_list.get(0).length];
    int [] w_avg = new int[train_list.get(0).length];
    int [] third_avg = new int[train_list.get(0).length];
    error = "TRAINING ERROR";

    for (int i = 1; i < 5; i++){
      w = perceptron_algo(train_list, w_init, 1);
      w_list = voted_algo(train_list, w_init, 1);
      perceptron_error = perceptron_error(train_list, w);
      voted_error = voted_error(train_list, w_list);
      w_avg = determine_average_w(w_list);

      if (i == 4){
        third_avg = w_avg;
      }

      avg_error = average_error(train_list, w_avg);
      System.out.format("%32s%n", error);
      System.out.format("%32s%d%n", "Pass # ", i);
      System.out.format("%32s%7f%n", "Peceptron Error: ", perceptron_error);
      System.out.format("%32s%7f%n", "Voted Error: ", voted_error);
      System.out.format("%32s%7f%n", "Average Error: ", avg_error);
      System.out.format("%32s%n", "----------------");
      w_init = w;
    }
    determine_min_max(third_avg, dict_list);
    System.out.println();
    error = "TEST ERROR";

    for (int i = 1; i < 5; i++){
      w = perceptron_algo(test_list, w_init, 1);
      w_list = voted_algo(test_list, w_init, 1);
      perceptron_error = perceptron_error(test_list, w);
      voted_error = voted_error(test_list, w_list);
      w_avg = determine_average_w(w_list);
      avg_error = average_error(test_list, w_avg);
      System.out.format("%32s%n", error);
      System.out.format("%32s%d%n", "Pass # ", i);
      System.out.format("%32s%7f%n", "Peceptron Error: ", perceptron_error);
      System.out.format("%32s%7f%n", "Voted Error: ", voted_error);
      System.out.format("%32s%7f%n", "Average Error: ", avg_error);
      System.out.format("%32s%n", "----------------");
      w_init = w;
    }

    // TODO: Start 3.3 here
    LinkedList<int[]> classifier_list = new LinkedList<int[]>();
    LinkedList<int[]> total_list  = new LinkedList<int[]>();
    total_list = input_file(train_file);
    w_init = new int[train_list.get(0).length];

    for (int j = 0; j < 6; j++){
      w = perceptron_algo(total_list, w_init, j+1);
      classifier_list.add(w);
    }

    int [] classifier_total = new int[6];
    classifier_total = sum_up_classifiers(total_list);

    LinkedList<int[]> predictions = new LinkedList<int[]>();
    LinkedList<double[]> prediction_percent = new LinkedList<double[]>();
    predictions = perceptron_error_matrix(total_list, classifier_list);
    prediction_percent = calculate_percentage(predictions, classifier_total);

  } // end of main

  public static int [] perceptron_algo(LinkedList<int[]> list, int [] w_init, int identifier){
    int [] w;
    int [] x;
    int temp;

    w = new int[list.get(0).length];
    x = new int[list.get(0).length];
    w = w_init;

    for (int i = 0; i < list.size(); i++){
      x = list.get(i);
      temp = dot_product(w, x);

      if (x[x.length - 1] != identifier){
        temp  =  -1 * temp;
      }

      if (temp <= 0){
        w = add_vector(w, x, x[x.length - 1], identifier);
      }

    }
    return w;
  }

  public static LinkedList<int[]> voted_algo(LinkedList<int[]> list, int [] w_init, int identifier){
    int [] w;
    int [] x;
    int temp;
    LinkedList<int[]> w_list = new LinkedList<int[]>();
    int m = 1;
    int c = 1;

    w = new int[list.get(0).length];
    x = new int[list.get(0).length];
    w = w_init;

    for (int i = 0; i < list.size(); i++){
      x = list.get(i);
      temp = dot_product(w,x);

      if (x[x.length - 1] == 2){
        temp = -1 * temp;
      }
      if (temp <= 0){
        w[w.length - 1] = c;
        w_list.add(w);
        w = add_vector(w, x, x[x.length - 1], identifier);
        m += 1;
        c = 1;
      }
      else{
        c += 1;
      }
    }
    return w_list;
  }

  public static int dot_product(int [] w, int [] x){
    int sum = 0;
    int product = 0;

    if (w.length != x.length){
      System.out.println("Vectors are not the same size!");
      return -1;
    }

    for (int i = 0; i < x.length - 1; i++){
      product = w[i] * x[i];
      sum = sum + product;
    }

    return sum;
  }

  public static int [] add_vector(int [] w, int [] x, int sign, int identifier){
    int [] w_new = new int[w.length];

    for (int i = 0; i < w.length - 1; i++){
      if (sign != identifier){
        w_new[i] = w[i] - x[i];
      }
      else{
        w_new[i] = w[i] + x[i];
      }
    }
    return w_new;
  }

  public static int [] scale_vector(int [] w, int c){
    int [] curr = new int[w.length];

    for (int i = 0; i < w.length - 1; i++){
      curr[i] = c * w[i];
    }
    return curr;
  }

  public static void print_list(LinkedList<int[]> list){
    int count = 1;
    String line;
    System.out.format("%32s%n", "Predict Count");

    for (int [] n : list)
    {
      if (count < 7){
        line = "Predict " + count + ": ";
        System.out.format("%32s", line);
      }
      else{
        line = "Predict DON'T KNOW: ";
        System.out.format("%32s", line);
      }

      for (int j = 0; j < n.length; j++)
      {
        System.out.format("%d%s", n[j], " ");
      }
      System.out.print("\n");
      count++;
    }
    System.out.println();
  }

  public static void print_percent(LinkedList<double[]> list){
    DecimalFormat numberFormat = new DecimalFormat("#.0000");
    int count = 1;
    String line;
    System.out.format("%32s%n", "Predict Percentage");

    for (double [] n : list)
    {
      if (count < 7){
        line = "Predict " + count + ": ";
        System.out.format("%32s", line);
      }
      else{
        line = "Predict DON'T KNOW: ";
        System.out.format("%32s", line);
      }

      for (int j = 0; j < n.length; j++)
      {
        System.out.print(numberFormat.format(n[j]) + " ");
      }
      System.out.print("\n");
      count++;
    }
    System.out.println();
  }

  public static LinkedList<int[]> input_file(File file){
    String input_line;
    String [] charArray;
    int stringLen;
    LinkedList<int[]> list = new LinkedList<int[]>();
    int[] row;
    double val;

    try
    {
      FileReader fileReader = new FileReader(file);
      BufferedReader buffer = new BufferedReader(fileReader);
      input_line = buffer.readLine();

      while (input_line != null)
      {
        charArray = input_line.split(" ");
        stringLen = charArray.length;
        row = new int[stringLen];

        for (int i = 0; i < stringLen; i++)
        {

          val = Integer.parseInt(charArray[i]);
          row[i] = (int)val;
        }
        list.add(row);
        input_line = buffer.readLine();
      }
    }
      catch (IOException e)
      {
          throw new IllegalArgumentException("Unable to load file", e);
      }
    return list;
  }

  public static LinkedList<int[]> input_file2(File file){
    String input_line;
    String [] charArray;
    int stringLen;
    LinkedList<int[]> list = new LinkedList<int[]>();
    int[] row;
    double val;

    try
    {
      FileReader fileReader = new FileReader(file);
      BufferedReader buffer = new BufferedReader(fileReader);
      input_line = buffer.readLine();

      while (input_line != null)
      {
        charArray = input_line.split(" ");
        stringLen = charArray.length;
        row = new int[stringLen];

        for (int i = 0; i < stringLen; i++)
        {

          val = Integer.parseInt(charArray[i]);
          row[i] = (int)val;
        }
        if ((row[row.length - 1] == 1) || (row[row.length - 1] == 2)){
          list.add(row);
        }
        input_line = buffer.readLine();
      }
    }
      catch (IOException e)
      {
          throw new IllegalArgumentException("Unable to load file", e);
      }
    return list;
  }

  public static LinkedList<String> input_dictionary(File file){
    String input_line;
    LinkedList<String> dict = new LinkedList<String>();

    try
    {
      FileReader fileReader = new FileReader(file);
      BufferedReader buffer = new BufferedReader(fileReader);
      input_line = buffer.readLine();

      while (input_line != null)
      {
        dict.add(input_line);
        input_line = buffer.readLine();
      }
    }
    catch (IOException e)
    {
          throw new IllegalArgumentException("Unable to load file", e);
    }

    return dict;
  }

  public static double perceptron_error(LinkedList<int[]> list, int[] w){
    int mistakes = 0;
    int total = 0;
    int val = 0;
    double total_error = 0;
    int predict = 0;
    int [] x = new int[w.length];

    for (int i = 0; i < list.size(); i++){
      x = list.get(i);
      val = dot_product(w, x);

      if (val < 0){
        predict = 2;
      }
      else{
        predict = 1;
      }

      if (predict != x[x.length - 1]){
        mistakes++;
      }
      total++;
    }

    total_error = (mistakes * 1.0)/total;
    return total_error;
  }

  public static double voted_error(LinkedList<int[]> list, LinkedList<int[]> w_list){
    int mistakes = 0;
    int total = 0;
    int val = 0;
    int sum = 0;
    double total_error = 0;
    int predict = 0;
    int [] x = new int[list.get(0).length];
    int [] w = new int[list.get(0).length];

    for (int i = 0; i < list.size(); i++){
      x = list.get(i);

      for (int j = 0; j < w_list.size(); j++){
        w = w_list.get(j);
        val = dot_product(w, x);
        if (val < 0){
          sum = sum - w[w.length - 1];
        }
        else{
          sum = sum + w[w.length - 1];
        }
      }
      if (sum < 0){
        predict = 2;
      }
      else{
        predict = 1;
      }
      if (predict != x[x.length - 1]){
        mistakes++;
      }
      total++;
      sum = 0;
    }
    total_error = (mistakes * 1.0)/total;
    return total_error;
  }

  public static double average_error(LinkedList<int[]> list, int[] w_total){
    double average_error = 0;
    int [] x = new int[list.get(0).length];
    int val = 0;
    int predict = 0;
    int mistakes = 0;
    int total = 0;

    for (int j = 0; j < list.size(); j++){
      x = list.get(j);
      val = dot_product(w_total, x);

      if (val < 0){
        predict = 2;
      }
      else{
        predict = 1;
      }

      if (predict != x[x.length - 1]){
        mistakes++;
      }
      total++;
    }

    average_error = (mistakes * 1.0)/total;
    return average_error;
  }

  public static int [] determine_average_w(LinkedList<int[]> w_list){
    int [] w_curr = new int[w_list.get(0).length];
    int [] scaled = new int[w_list.get(0).length];
    int [] w_total = new int[w_list.get(0).length];

    for (int i = 0; i < w_list.size(); i++){
        w_curr = w_list.get(i);
        scaled = scale_vector(w_curr, w_curr[w_curr.length - 1]);
        w_total = add_vector(w_total, scaled, 1, 1);
    }

    return w_total;
  }

  public static void determine_min_max(int[] w, LinkedList<String> dict){
    int [] vals = new int[w.length - 1];
    int [] sorted = new int[w.length - 1];
    int [] min_max = new int[6];
    int index = 0;
    HashMap<Integer, Integer> index_dict = new HashMap<Integer, Integer>();

    for (int i = 0; i < vals.length; i++){
      vals[i] = w[i];
      index_dict.put(vals[i], i);
    }

    sorted = selection_sort(vals);

    for (int i = 0; i < 3; i++){
      min_max[i] = sorted[i];
      min_max[min_max.length - 1 - i] = sorted[sorted.length - 1 - i];
    }

    System.out.format("%32s%n", "Max Word / Coordinate (In Descnding Order)");
    index = index_dict.get(min_max[5]);
    System.out.format("%32s%d%n", dict.get(index), index);
    index = index_dict.get(min_max[4]);
    System.out.format("%32s%d%n", dict.get(index), index);
    index = index_dict.get(min_max[3]);
    System.out.format("%32s%d%n", dict.get(index), index);

    System.out.format("%32s%n", "Min Word / Coordinate (In Descnding Order)");
    index = index_dict.get(min_max[2]);
    System.out.format("%32s%d%n", dict.get(index), index);
    index = index_dict.get(min_max[1]);
    System.out.format("%32s%d%n", dict.get(index), index);
    index = index_dict.get(min_max[0]);
    System.out.format("%32s%d%n", dict.get(index), index);
    return;
  }

  public static int[] selection_sort (int[] arr)
  {

    for (int i = 0; i < arr.length - 1; i++)
    {
      int index = i;
      for (int j = i + 1; j < arr.length; j++)
      {
        if (arr[j] < arr[index])
        {
          index = j;
        }
      }
        int smallerNum = arr[index];
        arr[index] = arr[i];
        arr[i] = smallerNum;
    }

    return arr;
  }

  public static LinkedList<int[]> perceptron_error_matrix(LinkedList<int[]> list, LinkedList<int[]> classifier_list){
    int mistakes = 0;
    int total = 0;
    int val = 0;
    double total_error = 0;
    int predict = 0;
    LinkedList<int[]> predictions = new LinkedList<int[]>();
    int [] x = new int[classifier_list.get(0).length];
    int [] w = new int[classifier_list.get(0).length];
    int count = 0, curr_j = 0, curr_k = 0;

    for (int k = 0; k < 7; k++){
      int [] row = new int[6];
      predictions.add(row);
    }

    for (int i = 0; i < list.size(); i++){
      x = list.get(i);

      for (int j = 0; j < classifier_list.size(); j++){
        w = classifier_list.get(j);
        val = dot_product(w, x);

        if (val < 0){

        }
        else{
          curr_j = j;
          curr_k = x[x.length - 1] - 1;
          //predictions.get(j)[x[x.length - 1] - 1] += 1;
          count++;
        }

      }
      if (count > 1){
        predictions.get(6)[curr_k] += 1;
      }
      else{
        predictions.get(curr_j)[curr_k] += 1;
      }
      count = 0;
    }
    print_list(predictions);
    return predictions;
  }
  public static int [] sum_up_classifiers(LinkedList<int[]> list){
    int [] classifier_total = new int[6];
    int [] x = new int[list.get(0).length];

    for (int i = 0; i < list.size(); i++){
      x = list.get(i);
      classifier_total[x[x.length - 1] - 1] += 1;

    }
    return classifier_total;
  }

  public static LinkedList<double[]> calculate_percentage(LinkedList<int[]> predictions, int[] classifier_total){
    LinkedList<double[]> prediction_percent = new LinkedList<double[]>();

    for (int k = 0; k < predictions.size(); k++){
      double [] row = new double[classifier_total.length];
      prediction_percent.add(row);
    }

    for (int i = 0; i < predictions.size(); i++){
      for (int j = 0; j < classifier_total.length; j++){
        prediction_percent.get(i)[j] = (predictions.get(i)[j] * 1.0) / classifier_total[j];
      }
    }
    print_percent(prediction_percent);
    return prediction_percent;
  }

} // end of class
