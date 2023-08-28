public class TreeSerializerMain {

    public static void main(String[] args) {

        // Create a binary Tree for the given examples.
        Node rootNode = new Node(1);
       rootNode.left = new Node(2);
        rootNode.left.right = new Node(5);
        rootNode.left.left = new Node(7);
        rootNode.left.left.left = new Node(4);

        rootNode.right = new Node(1);
        rootNode.right.right = new Node(28);

        // Note: The following line would introduce a cycle, creating a cyclic tree.
        //rootNode.left.right = rootNode.right;

        // Create an instance of PreorderSerialize to implement TreeSerializer interface.
        PreorderSerialize preorder = new PreorderSerialize();

        try{
            // Serialize the binary tree and print the serialized string.
            String serializeString =  preorder.serialize(rootNode);
            System.out.println("@@ Serialized Tree :: "+serializeString);

            // Deserialize the serialized string back to a tree and print the serialized version of the deserialized tree.
            Node deserialize =  preorder.deserialize(serializeString);
            System.out.println("@@ Deserialized root node :: "+ preorder.serialize(deserialize) );
        }
        catch(RuntimeException e){

            // Handle the case where a cyclic tree is detected during deserialization.
            System.out.println(":: "+e.getMessage());

        }

    }

}

