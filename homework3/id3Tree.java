// Michael Covarrubias
// PID#: A12409694
// Naveen Ketagoda
// PID#: A10773459

import java.io.*;
import java.util.*;
import java.lang.Math;

class Node
{
  int feature;
  double cutVal;
  LinkedList<int[]> list;
  int predictLabel;
  Node parent;
  Node l_child;
  Node r_child;
  boolean isLeaf = false;

  public Node(Node parent, LinkedList<int[]> list)
  {
    this.parent = parent;
    this.list = list;
    this.predictLabel = -1;
    this.l_child = null;
    this.r_child = null;
    this.feature = -1;
    this.cutVal = -1;
  }
}

public class id3Tree
{

  public static void main(String[] args)
  {
    int count = 0;
    Queue<Node> nodes = new LinkedList<Node>();
    Node curr;
    Node root;
    String trainString = args[0];
    //String validString = args[1];

    File train_file = new File(trainString);
    //File valid_file = new File(validString);

    //LinkedList<int[]> train_data = new LinkedList<int[]>();
    LinkedList<int[]> train_data = input_file(train_file);
    Node rootNode = new Node(null, train_data);
    double node_entropy;
    //calculate_entropy(rootNode);
    LinkedList<HashSet<Integer>> unsortedVals = calculate_attribute_values(rootNode.list);
    // now we need to sort the values
    LinkedList<int[]> sortedVals = sort_vals(unsortedVals);
    calculate_condition_entropy(rootNode);//, sortedVals);
    //LinkedList<int[]> valid_data = input_file(valid_file);
    nodes.add(rootNode);
    curr = nodes.peek();
    root = nodes.peek();

    while (curr != null)
    {


        print_list(curr.list);
        System.out.print("-----------------------------------------------------------------------------------\n");
        nodes.poll();
        System.out.print("Node: \n" + "List Size: " + curr.list.size() + "\n");
        System.out.print("Prediction: " + curr.predictLabel + "\n");
        System.out.print("Feature: " + (curr.feature + 1) + "\n");
        System.out.print("Cut Value: " + curr.cutVal + "\n");
        curr = calculate_condition_entropy(curr);//sortedVals);
        if (curr.l_child != null)
        {
          if (curr.l_child.predictLabel == -1)
          {
            nodes.add(curr.l_child);
          }
        }
        if (curr.r_child != null)
        {
          if (curr.r_child.predictLabel == -1)
          {
            nodes.add(curr.r_child);
          }
        }
        curr = nodes.peek();

    }

    System.out.println("FINISHED");

  }

  public static LinkedList<int[]> input_file(File file)
  {
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

          val = Double.parseDouble(charArray[i]);
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
  } // this method uploads all data

  public static double calculate_entropy(Node root)
  {
    double total_count = 0;
    double one_count = 0;
    double zero_count = 0;
    double p1, p0;
    double entropy;
    LinkedList<int[]> instances = root.list;

    for (int [] row: instances)
    {
      if (row[row.length - 1] == 1)
      {
        one_count += 1;
      }
      else
      {
        zero_count += 1;
      }
      total_count += 1;
    }

    p1 = one_count / total_count;
    p0 = zero_count / total_count;
    entropy = (-p1*Math.log(p1)) + (-p0*Math.log(p0));
    System.out.println(entropy);
    return entropy;
  }

  public static Node calculate_condition_entropy(Node root)//LinkedList<int[]> sortedVals)
  {
    // return an integer HastSet for every attribute x_0 to x_22
    // with a set of all values x_i equals
    LinkedList<HashSet<Integer>> unsortedVals = calculate_attribute_values(root.list);
    // now we need to sort the values
    LinkedList<int[]> sortedVals = sort_vals(unsortedVals);
    int count = 0;
    int feature;
    double cutVal;
    // varibles for splitting at a node
    LinkedList<int[]> list_z1;
    LinkedList<int[]> list_z0;
    LinkedList<int[]> list_z1_key;
    LinkedList<int[]> list_z0_key;
    int zero_z1;
    int one_z1;
    int zero_z0;
    int one_z0;
    double entropy_z1;
    double entropy_z0;
    double entropy = 100;
    int [] curr_sort;
    int [] curr;
    int featureKey = 30;
    double entropyKey = 100;
    double cutValKey = 1000000.00;
    int one_z1_key = 0;
    int zero_z1_key = 0;
    int one_z0_key = 0;
    int zero_z0_key = 0;
    list_z1_key = new LinkedList<int[]>();
    list_z0_key = new LinkedList<int[]>();

    for (int x = 0; x < sortedVals.size(); x++)
    {
      one_z1 = 0; zero_z1 = 0; one_z0 = 0; zero_z0 = 0;
      curr_sort = sortedVals.get(x);
      feature = x;
      for (int i = 0; i < curr_sort.length - 1; i++)
      {
        cutVal = (double)(curr_sort[i] + (curr_sort[i + 1] - curr_sort[i])/2.0);
        list_z1 = new LinkedList<int[]>();
        list_z0 = new LinkedList<int[]>();
        // loop to separate all vectors by cut value
        for (int j = 0; j < root.list.size(); j++)
        {
          curr = root.list.get(j);
          if (curr[feature] < cutVal)
          {
            list_z1.add(curr);
            if (curr[curr.length - 1] == 1)
            {
              one_z1 += 1;
            }
            else
            {
              zero_z1 += 1;
            }
          }
          else
          {
            list_z0.add(curr);
            if (curr[curr.length - 1] == 1)
            {
              one_z0 += 1;
            }
            else
            {
              zero_z0 += 1;
            }
          }
        } // end of iterating through all vector values
        double prob_zero_z1 = (1.0*zero_z1)/(1.0*zero_z1+one_z1);
        double prob_one_z1 = (1.0*one_z1)/(1.0*zero_z1+one_z1);
        double prob_zero_z0 = (1.0*zero_z0)/(1.0*zero_z0+one_z0);
        double prob_one_z0 = (1.0*one_z0)/(1.0*zero_z0+one_z0);
        entropy_z1 = (-prob_zero_z1*Math.log(prob_zero_z1)) + (-prob_one_z1*Math.log(prob_one_z1));
        entropy_z0 = (-prob_zero_z0*Math.log(prob_zero_z0)) + (-prob_one_z0*Math.log(prob_one_z0));
        double size_z0 = list_z0.size();
        double size_z1 = list_z1.size();
        double size_total = size_z1 + size_z0;
        entropy = (size_z1/size_total)*entropy_z1 + (size_z0/size_total)*entropy_z0;
        //System.out.println(entropy);
        if (entropy < entropyKey)
        {
          entropyKey = entropy;
          featureKey = feature;
          cutValKey = cutVal;
          list_z1_key = list_z1;
          list_z0_key = list_z0;
          one_z1_key = one_z1;
          zero_z1_key = zero_z1;
          one_z0_key = one_z0;
          zero_z0_key = zero_z0;

        }

      }
    } // end of checking all possible cuts
    root.feature = featureKey;
    root.cutVal = cutValKey;
    // 2 determined children of decision tree node
    Node l_child;// = new Node(root, list_z1_key);
    Node r_child;// = new Node(root, list_z0_key);
    if (list_z1_key.size() > 0)
    {
      l_child = new Node(root, list_z1_key);
      //l_child.list = list_z1_key;
      //System.out.println("z1: " + list_z1_key.size() + "\n");
      if (one_z1_key == 0)
      {
        l_child.isLeaf = true;
        l_child.predictLabel = 0;
      }
      else if (zero_z1_key == 0)
      {
        l_child.isLeaf = true;
        l_child.predictLabel = 1;
      }
      root.l_child = l_child;
    }
    if (list_z0_key.size() > 0)
    {
      r_child = new Node(root, list_z0_key);
      //r_child.list = list_z0_key;
      //System.out.println("z0: " + list_z0_key.size() + "\n");
      if (one_z0_key == 0)
      {
        r_child.isLeaf = true;
        r_child.predictLabel = 0;
      }
      else if (zero_z0_key == 0)
      {
        r_child.isLeaf = true;
        r_child.predictLabel = 1;
      }
      root.r_child = r_child;
    }

    // implement creating the children of the node
    //System.out.print(featureKey + "\n");
    //System.out.print(entropyKey + "\n");
    //System.out.print(cutValKey + "\n");

    return root;
  }

  public static LinkedList<HashSet<Integer>> calculate_attribute_values(LinkedList<int[]> list)
  {
    // a HashSet for every attribute (22 of them) and all the values the can be
    // in order to determine the split values
    LinkedList<HashSet<Integer>> unsortedVals = new LinkedList<HashSet<Integer>>();
    HashSet<Integer> attribute;
    int [] row;

    for(int i = 0; i < 22; i++)
    {
      attribute = new HashSet<Integer>();

      for (int j = 0; j < list.size(); j++)
      {
        row = list.get(j);
        attribute.add(row[i]);
      }
      unsortedVals.add(attribute);
    }

    return unsortedVals;
  }

  public static LinkedList<int[]> sort_vals (LinkedList<HashSet<Integer>> unsortedVals)
  {
    LinkedList<int[]> sortedValues = new LinkedList<int[]>();
    int [] col;
    int [] sortedInts;
    HashSet<Integer> attr_i = new HashSet<Integer>();
    int j = 0;


    for (int i = 0; i < unsortedVals.size(); i++)
    {
      attr_i = unsortedVals.get(i);
      col = new int[attr_i.size()];
      sortedInts = new int[attr_i.size()];
      j = 0;
      for (Integer n : attr_i)
      {
        col[j] = (int)n;
        j++;
      }

      sortedInts = selection_sort(col);

      sortedValues.add(sortedInts);
    }
    return sortedValues;
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

  public static void print_list(LinkedList<int []> list)
  {
    for (int [] n : list)
    {
      for (int j = 0; j < n.length; j++)
      {
        System.out.print(n[j] + " ");
      }
      System.out.print("\n");
    }
  }
} // end of class
