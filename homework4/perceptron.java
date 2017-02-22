// Michael Covarrubias
// PID#: A12409694
// Naveen Ketagoda
// PID#: A10773459

import java.io.*;
import java.util.*;
import java.lang.Math;

class perceptron {
  public static void main(String[] args){
    // declare file variables
    String train_string, test_string;
    double perceptron_error = 0;
    double voted_error = 0;
    double avg_error = 0;
    train_string = args[0];
    //test_string = args[1];

    File train_file = new File(train_string);
    //File test_file = new File(test_string);
    LinkedList<int[]> train_list  = new LinkedList<int[]>();
    LinkedList<int[]> test_list = new LinkedList<int[]>();
    LinkedList<int[]> w_list = new LinkedList<int[]>();
    train_list = input_file2(train_file);
    int [] w = new int[train_list.get(0).length];
    int [] w_init = new int[train_list.get(0).length];
    w = perceptron_algo(train_list, w_init);
    w_list = voted_algo(train_list, w_init);
    perceptron_error = perceptron_error(train_list, w);
    voted_error = voted_error(train_list, w_list);
    avg_error = average_error(train_list, w_list);
    System.out.format("%32s%7f%n", "Peceptron Error: ", perceptron_error);
    System.out.format("%32s%7f%n", "Voted Error: ", voted_error);
    System.out.format("%32s%7f%n", "Average Error: ", avg_error);
    //print_list(train_list);
    //test_list = input_file(test_string);

  }
  public static int [] perceptron_algo(LinkedList<int[]> list, int [] w_init){
    int [] w;
    int [] x;
    int temp;

    w = new int[list.get(0).length];
    x = new int[list.get(0).length];
    w = w_init;

    for (int i = 0; i < list.size(); i++){
      x = list.get(i);
      temp = dot_product(w, x);

      if (x[x.length - 1] == 2){
        temp  =  -1 * temp;
      }

      if (temp <= 0){
        w = add_vector(w, x, x[x.length - 1]);
      }

    }
    return w;
  }

  public static LinkedList<int[]> voted_algo(LinkedList<int[]> list, int [] w_init){
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
        w = add_vector(w, x, x[x.length - 1]);
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

  public static int [] add_vector(int [] w, int [] x, int sign){
    int [] w_new = new int[w.length];

    for (int i = 0; i < w.length - 1; i++){
      if (sign == 2){
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
    for (int [] n : list)
    {
      for (int j = 0; j < n.length; j++)
      {
        System.out.print(n[j] + " ");
      }
      System.out.print("\n");
    }
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

  public static double average_error(LinkedList<int[]> list, LinkedList<int[]> w_list){
    double average_error = 0;
    int [] w_curr = new int[list.get(0).length];
    int [] w_total = new int[list.get(0).length];
    int [] scaled = new int[list.get(0).length];
    int [] x = new int[list.get(0).length];
    int val = 0;
    int predict = 0;
    int mistakes = 0;
    int total = 0;


    for (int i = 0; i < w_list.size(); i++){
        w_curr = w_list.get(i);
        scaled = scale_vector(w_curr, w_curr[w_curr.length - 1]);
        w_total = add_vector(w_total, scaled, 1);
    }

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
}
