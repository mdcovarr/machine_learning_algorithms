// Michael Covarrubias
// PID# A12409694
// Naveen Ketagoda
// PID# A10773459

import java.io.*;
import java.util.*;
import java.lang.Math;

public class projectionKNN
{
  static class proj_Vector
  {
      double label;
      double [] data;
  }

  static class Classifier
  {
      double label;
      double dist;
  }

  public static void main(String[] args)
  {
        int [] key = {1, 5, 9, 15};

    for (int keyIndex = 0; keyIndex < key.length; keyIndex++)
    {
        PriorityQueue<Classifier> knn = new PriorityQueue<Classifier>( key[keyIndex],
          new Comparator<Classifier> () {
              public double compare( Classifier x, Classifier y)
              {
                  return Double.compare(y.dist, x.dist);
              }
          }
      );

      File train_file = new File("hw2train.txt");
      File valid_file = new File("hw2validate.txt");
      File test_file = new File("hw2test.txt");
      //File proj_file = new File("projection.txt");

      LinkedList<proj_Vector> train_data = input_file(train_file);
      LinkedList<proj_Vector> valid_data = input_file(valid_file);
      LinkedList<proj_Vector> test_data = input_file(test_file);
      //LinkedList<Vector> proj_data = input_projection(proj_file);

      int error = 0;
      for (proj_Vector vec1: valid_data)
      {
          for (int j = 0; j < key[keyIndex]; j++)
          {
            Classifier cla = new Classifier();
            cla.label = 0;
            cla.dist = Double.MAX_VALUE;
            knn.add(cla);
          }

          for (proj_Vector vec2: train_data)
          {
              double x = distance(vec1.data, vec2.data);
              Classifier newClassifier = new Classifier();
              newClassifier.label  = vec2.label;
              newClassifier.dist = x;
              Classifier current = knn.peek();

              if (newClassifier.dist < current.dist)
              {
                knn.poll();
                knn.add(newClassifier);
              }

          }
          int [] allLabels = {0,0,0,0,0,0,0,0,0,0};

          for (Classifier k_neighbor: knn)
          {
            allLabels[(int)k_neighbor.label] += 1;
          }

          int chosenLabel = 0;

          for (int i = 0; i < allLabels.length; i++)
          {
              if (allLabels[i] > allLabels[chosenLabel])
              {
                  chosenLabel = i;
              }
          }

          if (vec1.label != chosenLabel)
          {
              error++;
          }

          knn.clear();
      }
      double numVectors = 1.0 * train_data.size();
      //System.out.println(numVectors);
      System.out.print("For " + key[keyIndex] + "-NN ");
      System.out.println("Validation Error: " + (error/numVectors));
    }
      return;
  }

  public static LinkedList<proj_Vector> input_projection(File file)
  {
    String input_line;
    String [] charArray;
    int stringLen;
    LinkedList<proj_Vector> output = new LinkedList<proj_Vector>();
    //Vector row;

    for (int j = 0; j < 20; j++)
    {
        proj_Vector v = new proj_Vector();
        v.data = new double[784];
        output.add(v);
    }

    try
    {
        FileReader fileReader = new FileReader(file);
        BufferedReader buffer = new BufferedReader(fileReader);
        input_line = buffer.readLine();
        int i = 0;
        while (input_line != null)
        {
            charArray = input_line.split(" ");
            stringLen = charArray.length;

            for (proj_Vector vec: output)
            {
                vec.data[i] = Double.parseDouble(charArray[i]);

            }

            i++;
            input_line = buffer.readLine();
        }
    }
    catch (IOException e)
    {
        throw new IllegalArgumentException("Unable to load file", e);
    }
    return output;
  }

  public static LinkedList<proj_Vector> input_file(File file)
  {
      String input_line;
      String [] charArray;
      int stringLen;
      LinkedList<proj_Vector> output = new LinkedList<proj_Vector>();

      proj_Vector row;

      try
      {
          FileReader fileReader = new FileReader(file);
          BufferedReader buffer = new BufferedReader(fileReader);
          input_line = buffer.readLine();

          while (input_line != null)
          {
              charArray = input_line.split(" ");
              stringLen = charArray.length;

              row = new proj_Vector();
              row.data = new double[stringLen - 1];

              for (int i = 0; i < stringLen - 1; i++)
              {
                row.data[i] = Double.parseDouble(charArray[i]);
              }
              row.label = Double.parseDouble(charArray[stringLen - 1]);
              output.addLast(row);
              input_line = buffer.readLine();
          }
      }
      catch (IOException e)
      {
          throw new IllegalArgumentException("Unable to load file", e);
      }
      return output;
  }

  public static double distance(double [] x1, double [] x2)
  {
      double total = 0;
      int len = x1.length;
      double distance;
      for (int i = 0; i < len; i++)
      {
          total = total + ((x1[i] - x2[i]) * (x1[i] - x2[i]));
      }

      distance = (int)Math.sqrt(total);
      return distance;
  }
}
