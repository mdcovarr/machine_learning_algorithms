// Michael Covarrubias
// PID# A12409694
// Naveen Ketagoda
// PID# A10773459

import java.io.*;
import java.util.*;
import java.lang.Math;

public class KNN
{
  static class Vector
  {
      int label;
      int [] data;
  }

  static class Classifier
  {
      int label;
      int dist;
  }

  public static void main(String[] args)
  {
    int [] key = {1, 5, 9, 15};
    File train_file = new File("hw2train.txt");
    File valid_file = new File("hw2test.txt");

    LinkedList<Vector> train_data = input_file(train_file);
    LinkedList<Vector> valid_data = input_file(valid_file);


    for (int keyIndex = 0; keyIndex < key.length; keyIndex++)
    {

  PriorityQueue<Classifier> knn = new PriorityQueue<Classifier>( key[keyIndex],
          new Comparator<Classifier> () {
              public int compare( Classifier x, Classifier y)
              {
                  return Integer.compare(y.dist, x.dist);
              }
          }
      );

      int error = 0;
      for (Vector vec1: train_data)
      {
          for (int j = 0; j < key[keyIndex]; j++)
          {
            Classifier cla = new Classifier();
            cla.label = 0;
            cla.dist = Integer.MAX_VALUE;
            knn.add(cla);
          }

          for (Vector vec2: valid_data)
          {
              int x = distance(vec1.data, vec2.data);
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
            allLabels[k_neighbor.label] += 1;
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

  public static LinkedList<Vector> input_file(File file)
  {
      String input_line;
      String [] charArray;
      int stringLen;
      LinkedList<Vector> output = new LinkedList<Vector>();

      Vector row;

      try
      {
          FileReader fileReader = new FileReader(file);
          BufferedReader buffer = new BufferedReader(fileReader);
          input_line = buffer.readLine();
          while (input_line != null)
          {
              charArray = input_line.split(" ");
              stringLen = charArray.length;
              //row = new Integer[stringLen];
              row = new Vector();
              row.data = new int[stringLen - 1];

              for (int i = 0; i < stringLen - 1; i++)
              {
                row.data[i] = Integer.parseInt(charArray[i]);
              }
              row.label = Integer.parseInt(charArray[stringLen - 1]);
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

  public static int distance(int [] x1, int [] x2)
  {
      int total = 0;
      int len = x1.length;
      int distance;
      for (int i = 0; i < len; i++)
      {
          total = total + ((x1[i] - x2[i]) * (x1[i] - x2[i]));
      }

      distance = (int)Math.sqrt(total);
      return distance;
  }


}
