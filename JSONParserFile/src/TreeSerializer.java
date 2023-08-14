import java.sql.SQLOutput;
import java.util.*;

// Definition of the TreeSerializer interface.
public interface TreeSerializer {
        String serialize(Node root);

        Node deserialize(String str);
    }

// Implementation of the TreeSerializer interface using preorder traversal.
class PreorderSerialize implements TreeSerializer {

        int index=0  ;


        @Override
        public String serialize(Node root) {

                // List to store serialized values.
                List<String> serializeValueList = new ArrayList<>();

                if ( root ==null){
                     throw new  RuntimeException("Tree is empty");
                }

                // Map to track visited nodes during serialization.
                Map<Node,String> visitedNode = new HashMap<>();

                // Perform serialization using DFS.
                serializePreorderDfs(root, serializeValueList,visitedNode);


                return String.join("->", serializeValueList);
        }


         // Recursive method to perform preorder serialization using DFS.
         public void serializePreorderDfs(Node root, List<String> serializeValueList, Map<Node,String> visitedNode) {

                if (root == null) {
                    serializeValueList.add("null"); // added "null" value for null nodes.
                    return;
                }

                  String  preorderString = String.valueOf(root.num);
                if( visitedNode.containsKey(root) ){
                      throw new RuntimeException("Tree is Cyclic "); //Detect cyclic tree and throw runtime error.
                  }


                visitedNode.put(root,preorderString);

                 //System.out.println("@@root.num ::"+root.num);
                 //System.out.println("@@root node "+root);

                 serializeValueList.add(preorderString); // add current node values to list.
                 serializePreorderDfs(root.left, serializeValueList,visitedNode); // Recursive call to add left subtree.
                 serializePreorderDfs(root.right, serializeValueList,visitedNode); // Recursive call to add right subtree.

        }

          @Override
          public Node deserialize(String str) {

                  // Split the serialized string into an array of values.
                  String[] serializeString = str.split("->");

                  // Perform deserialization using DFS and return the root node.
                  return deserializePreorderDfs(serializeString);

          }

          // Recursive method to perform preorder deserialization using DFS.

         public Node deserializePreorderDfs(String[] serializeString){

                    //System.out.println("@@index:: "+index);
                   String serializeStr = serializeString[index];

                   if ( serializeStr.equals("null") ){
                        index++;
                        return null;
                    }

                   Node node = new Node(Integer.parseInt(serializeStr));
                   index++;

                   // Recursive call to add left subtree.
                   node.left = deserializePreorderDfs(serializeString) ;

                   // Recursive call to add right subtree
                   node.right = deserializePreorderDfs(serializeString);

                   return node;
          }


    }

